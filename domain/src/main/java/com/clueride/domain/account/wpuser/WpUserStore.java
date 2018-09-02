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
 * Knows how to persist and retrieve WordPress User instances.
 */
public interface WpUserStore {

    /**
     * Given an Email Address (wrapped in an EmailPrincipal), provide the matching WordPress / BadgeOS info.
     * @param emailPrincipal representing a particular email address.
     * @return Builder for the WpUser class.
     */
    WpUser.Builder getWpUser(EmailPrincipal emailPrincipal);

    /**
     * Create new set of records for the given WordPress User instance.
     * @param builder mutable object representing complete set of data we have on this User.
     * @return the instance originally passed in.
     */
    WpUser.Builder createWpUser(WpUser.Builder builder);

}
