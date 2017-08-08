package com.clueride.token;
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

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import com.clueride.CoreGuiceModuleTest;
import com.clueride.config.ConfigService;
import com.clueride.domain.account.Member;
import com.clueride.exc.RecordNotFoundException;
import com.clueride.member.MemberService;
import com.clueride.principal.PrincipalService;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Exercises the TokenServiceJwtTest class.
 */
@Guice(modules=CoreGuiceModuleTest.class)
public class TokenServiceJwtTest {
    private String secret;
    private String issuer;
    private Map<TokenType,String> tokenMap;
    private TokenServiceJwt toTest;

    @Inject
    private Provider<TokenServiceJwt> toTestProvider;

    @Inject
    private ConfigService configService;

    @Inject
    private Provider<JWTCreator.Builder> jwtProvider;

    @Inject
    private Member member;

    @Inject
    private MemberService memberService;

    @Inject
    private Principal principal;

    @Inject
    private PrincipalService principalService;

    @BeforeMethod
    public void setUp() throws Exception {
        secret = configService.get("token.jwt.secret");
        issuer = configService.get("token.jwt.issuer");
        toTest = toTestProvider.get();
        tokenMap = buildTokenTypeMap();
    }

//    @Test
    public void testGenerateSignedToken() throws Exception {
        String expected = tokenMap.get(TokenType.OK);
        String actual = toTest.generateSignedToken();
        assertEquals(actual, expected);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testVerifyToken_null() throws Exception {
        toTest.verifyToken(null);
    }

    @Test
    public void testGenerateTokenForPrincipal() throws Exception {
        /* train mocks */
        when(memberService.getMemberByEmail(anyString())).thenReturn(member);
        when(principalService.getNewPrincipal()).thenReturn(principal);

        /* make call */
        String actual = toTest.generateTokenForNewPrincipal();
        assertNotNull(actual);
    }

    @Test
    public void testVerifyToken() throws Exception {
        toTest.verifyToken(tokenMap.get(TokenType.OK));
    }

    @Test
    public void testValidateToken_OK() throws Exception {
        toTest.validateToken(tokenMap.get(TokenType.OK));
    }

    @Test(expectedExceptions = JWTVerificationException.class)
    public void testValidateToken_expired() throws Exception {
        toTest.validateToken(tokenMap.get(TokenType.EXPIRED));
    }

    @Test(expectedExceptions = InvalidClaimException.class)
    public void testValidateToken_wrongIssuer() throws Exception {
        toTest.validateToken(tokenMap.get(TokenType.BAD_ISSUER));
    }

    @Test(expectedExceptions = RecordNotFoundException.class)
    public void testValidateToken_principalNotFound() throws Exception {
        doThrow(RecordNotFoundException.class).when(principalService).validate(anyString());
        toTest.validateToken(tokenMap.get(TokenType.PRINCIPAL_NOT_FOUND));
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testValidateToken_badJti() throws Exception {
        toTest.validateToken(tokenMap.get(TokenType.INVALID_JTI));
    }

    @Test
    public void testGetName() throws Exception {
        /* setup */
        String expected = member.getEmailAddress();

        /* make call */
        String actual = toTest.getNameFromToken(
                tokenMap.get(TokenType.OK)
        );

        /* verify results */
        assertEquals(actual, expected);
    }

    private Map<TokenType,String> buildTokenTypeMap() {
        Map<TokenType, String> map = new HashMap<>();

        try {
            map.put(
                    TokenType.OK,
                    jwtProvider.get()
                            .withIssuer(issuer)
                            .sign(Algorithm.HMAC256(secret))
            );

            map.put(
                    TokenType.PRINCIPAL_NOT_FOUND,
                    jwtProvider.get()
                            .withClaim(CustomClaim.EMAIL, "not.quite@clueride.com")
                            .withIssuer(issuer)
                            .sign(Algorithm.HMAC256(secret))
            );

            map.put(
                    TokenType.BAD_ISSUER,
                    jwtProvider.get()
                            .withIssuer("aint no issuer")
                            .sign(Algorithm.HMAC256(secret))
            );

            Date now = new Date();
            Date yesterday = new Date(now.getTime() - 86400000);

            map.put(
                    TokenType.EXPIRED,
                    jwtProvider.get()
                            .withExpiresAt(yesterday)
                            .withIssuer(issuer)
                            .sign(Algorithm.HMAC256(secret))
            );

            map.put(
                    TokenType.INVALID_JTI,
                    jwtProvider.get()
                            .withJWTId("messedUp")
                            .withIssuer(issuer)
                            .sign(Algorithm.HMAC256(secret))
            );

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return map;
    }

    /* Easy way to map a set of tokens for testing. These are the keys to the map whose value is a token. */
    private enum TokenType {
        OK,
        BAD_ISSUER,
        BAD_ALGORITHM,
        EXPIRED,
        PRINCIPAL_NOT_FOUND,
        INVALID_JTI
    }
}