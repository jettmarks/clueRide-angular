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
package com.clueride.service;

import java.io.UnsupportedEncodingException;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import static com.auth0.jwt.algorithms.Algorithm.HMAC256;
import static java.util.Objects.requireNonNull;

/**
 * JWT implementation of Token Service.
 */
public class TokenServiceJwt implements TokenService {

    private static final String ISSUER = "clueride.com";
    private static Algorithm ALGORITHM = null;

    public TokenServiceJwt() {
        if (ALGORITHM == null) {
            try {
                // TODO: Improve my secret
                ALGORITHM = HMAC256("notMuchOfASecret");
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
        JWTVerifier verifier = JWT.require(ALGORITHM)
                .withIssuer(ISSUER)
                .build(); //Reusable verifier instance
        return verifier.verify(token);
    }

    @Override
    public void validateToken(String token) {
        DecodedJWT jwt = verifyToken(token);
        Claim principalClaim = jwt.getHeaderClaim("principal");
        if (principalClaim.isNull()) {
            throw new InvalidClaimException("Unable to verify token's principal");
        }
    }
}
