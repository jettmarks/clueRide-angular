/*
 * Copyright 2018 Jett Marks
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
 * Created by jett on 10/13/18.
 */
package com.clueride.auth.access;

import java.util.concurrent.ExecutionException;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;

import com.clueride.auth.identity.ClueRideIdentity;
import com.clueride.auth.identity.IdentityStore;
import com.clueride.exc.RecordNotFoundException;

/**
 * Default implementation of AccessTokenService.
 *
 * Provides caching of the results as well as retrieval of results
 * that are not in the cache.
 */
public class AccessTokenServiceImpl implements AccessTokenService {
    private static IdentityStore identityStore;

    // TODO: CA-381 Expire Tokens
    private static LoadingCache<String, ClueRideIdentity> identityCache =
            CacheBuilder.newBuilder()
                    .build(
                            new CacheLoader<String, ClueRideIdentity>() {
                                @Override
                                public ClueRideIdentity load(@Nonnull String key) throws Exception {
                                    return identityStore.getIdentity(key);
                                }
                            }
                    );

    /**
     * Injectable constructor.
     *
     * @param identityStoreInjected Service for retrieval of User Info from an access token.
     */
    @Inject
    public AccessTokenServiceImpl(
            @Nonnull IdentityStore identityStoreInjected
    ) {
        identityStore = identityStoreInjected;
    }

    @Override
    public ClueRideIdentity getIdentity(AccessToken accessToken) {
        return getIdentity(accessToken.getToken());
    }

    @Override
    public ClueRideIdentity getIdentity(String token) {
        try {
            return identityCache.get(token);
        } catch (ExecutionException | UncheckedExecutionException e) {
            throw new RecordNotFoundException(e.getMessage());
        }
    }

    @Override
    public String getPrincipalString(String token) {
        return String.valueOf(getIdentity(token).getEmail());
    }

    @Override
    public void emptyCache() {
        identityCache.invalidateAll();
    }

}
