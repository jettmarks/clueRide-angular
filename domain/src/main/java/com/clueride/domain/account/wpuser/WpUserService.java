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

import com.clueride.domain.account.principal.EmailPrincipal;

/**
 * Manages WordPress User account information.
 */
public interface WpUserService {

    /**
     * Given an Email Address (wrapped in a Principal object), provide the matching WordPress and BadgeOS User Account
     * information.
     * @return WpUser record matching the email address.
     */
    WpUser getUserByEmail(EmailPrincipal emailPrincipal);

    /**
     * Given a fully-populated WpUser instance, persist this to the BadgeOS system.
     * @param wpUser to be created.
     * @return The instance with an ID from the database.
     */
    WpUser createUser(WpUser.Builder wpUser);

}
