/*
 * Copyright 2017 Jett Marks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created by jett on 7/25/17.
 */
package com.clueride.token;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.log4j.Logger;

import com.clueride.config.ConfigService;
import com.clueride.domain.account.member.Member;
import com.clueride.member.MemberService;
import com.clueride.principal.PrincipalService;
import static com.auth0.jwt.algorithms.Algorithm.HMAC256;
import static java.util.Objects.requireNonNull;

/**
 * JWT implementation of Token Service.
 */
public class TokenServiceJwt implements TokenService {
    private static final Logger LOGGER = Logger.getLogger(TokenServiceJwt.class);

    private static final String ISSUER = "clueride.com";
    private static Algorithm ALGORITHM = null;
    private static JWTVerifier verifier;

    private final PrincipalService principalService;
    private final JtiService jtiService;
    private final MemberService memberService;

    /**
     * Injectable constructor
     * @param principalService provides Principals to use within the payload of the token.
     * @param jtiService provides JWT IDs and registers them.
     * @param memberService provides member information.
     * @param configService provides configuration information.
     */
    @Inject
    public TokenServiceJwt(
            PrincipalService principalService,
            JtiService jtiService,
            MemberService memberService,
            ConfigService configService
    ) {
        this.principalService = principalService;
        this.jtiService = jtiService;
        this.memberService = memberService;

        String secret = configService.get("token.jwt.secret");
        requireNonNull(secret, "Configuration missing for 'token.jwt.secret'");
        if (ALGORITHM == null) {
            try {
                ALGORITHM = HMAC256(secret);
                verifier = JWT.require(ALGORITHM)
                        .withIssuer(ISSUER)
                        .build(); //Reusable verifier instance
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String generateSignedToken() {
        String token = "guest";
        try {
            token = JWT.create()
                    .withIssuer(ISSUER)
                    .sign(ALGORITHM);
        } catch (JWTCreationException exception){
            //Invalid Signing configuration / Couldn't convert Claims.
        }
        return token;
    }

    @Override
    public DecodedJWT verifyToken(String token) {
        requireNonNull(token, "Token can't be null or empty");
        return verifier.verify(token);
    }

    @Override
    public void validateToken(String token) {
        DecodedJWT jwt = verifyToken(token);
        String jtiId = jwt.getId();
        // TODO: CA-315 Decide if we want to configure the JTI check; currently commented out.
        // jtiService.validateId(jtiId);

        Claim emailClaim = jwt.getHeaderClaim(CustomClaim.EMAIL);
        Claim badgesClaim = jwt.getHeaderClaim(CustomClaim.BADGES);

        if (emailClaim.isNull()) {
            LOGGER.warn("Unable to verify token's email");
            throw new InvalidClaimException("Unable to verify token's email");
        }

        principalService.validate(emailClaim.asString());

        if (badgesClaim.isNull()) {
            LOGGER.warn("Unable to verify token's badge set");
            throw new InvalidClaimException("Unable to verify token's badge set");
        }
    }

    @Override
    public String generateTokenForNewPrincipal() {
        // May be going in the wrong direction; look at the best practices post
        Principal newPrincipal = principalService.getNewPrincipal();
        requireNonNull(newPrincipal, "Unable to generate new Principal");
        memberService.createNewMemberWithEmail(newPrincipal.getName());
        return generateTokenForExistingPrincipal(newPrincipal, true);
    }

    @Override
    public String generateTokenForExistingPrincipal(Principal principal, boolean isGuest) {
        Member member = memberService.getMemberByEmail(principal.getName());

        Map<String, Object> headerClaims = new HashMap<>();
        headerClaims.put(CustomClaim.BADGES, member.getBadges());
        headerClaims.put(CustomClaim.EMAIL, principal.getName());
        headerClaims.put(CustomClaim.GUEST, isGuest);

        return JWT.create()
                .withIssuer(ISSUER)
                .withJWTId(jtiService.registerNewId())
                .withHeader(headerClaims)
                .sign(ALGORITHM);
    }

    @Override
    public String getNameFromToken(String token) {
        DecodedJWT jwt = verifyToken(token);
        Claim emailClaim = jwt.getHeaderClaim(CustomClaim.EMAIL);
        if (emailClaim.isNull()) {
            throw new IllegalStateException("Provided token doesn't have a EMAIL claim");
        }
        return emailClaim.asString();
    }

    /** Tokens without the GUEST claim throw an exception. */
    @Override
    public boolean isGuestToken(String token) {
        DecodedJWT jwt = verifyToken(token);
        Claim claim = jwt.getHeaderClaim(CustomClaim.GUEST);
        if (claim.isNull()) {
            throw new IllegalStateException("Provided token doesn't have a GUEST claim");
        }
        return claim.asBoolean();
    }

}
