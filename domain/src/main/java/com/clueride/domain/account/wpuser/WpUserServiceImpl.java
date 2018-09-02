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
 * Created by jett on 9/1/18.
 */
package com.clueride.domain.account.wpuser;

import java.sql.Timestamp;
import java.util.Date;

import javax.inject.Inject;

import com.clueride.domain.account.principal.EmailPrincipal;

/**
 * Default implementation of WpUser Service.
 */
public class WpUserServiceImpl implements WpUserService {
    private final WpUserStore wpUserStore;

    @Inject
    public WpUserServiceImpl(
            WpUserStore wpUserStore
    ) {
        this.wpUserStore = wpUserStore;
    }

    @Override
    public WpUser getUserByEmail(EmailPrincipal emailPrincipal) {
        return this.wpUserStore.getWpUser(emailPrincipal).build();
    }

    @Override
    public WpUser createUser(WpUser.Builder wpUser) {
        /* Don't care what we've been told, we're using our own timestamp for creation. */
        wpUser.withActivatedTimestamp(new Timestamp(new Date().getTime()));
        return this.wpUserStore.createWpUser(wpUser).build();
    }

}
