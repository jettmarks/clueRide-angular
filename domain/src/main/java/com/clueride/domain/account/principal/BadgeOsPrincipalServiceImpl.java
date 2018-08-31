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
 * Created by jett on 8/28/18.
 */
package com.clueride.domain.account.principal;

import javax.inject.Inject;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.log4j.Logger;

import static java.util.Objects.requireNonNull;

/**
 * Implementation that simply maps between what is in BadgeOS for a given email address.
 */
public class BadgeOsPrincipalServiceImpl implements BadgeOsPrincipalService {
    private static final Logger LOGGER = Logger.getLogger(BadgeOsPrincipalServiceImpl.class);
    private final BadgeOsPrincipalStore badgeOsPrincipalStore;

    @Inject
    public BadgeOsPrincipalServiceImpl(
            BadgeOsPrincipalStore badgeOsPrincipalStore
    ) {
        this.badgeOsPrincipalStore = badgeOsPrincipalStore;
    }

    @Override
    public BadgeOsPrincipal getBadgeOsPrincipal(InternetAddress emailAddress) {
        requireNonNull(emailAddress, "Email Address must be non-null");

        return badgeOsPrincipalStore.getBadgeOsPrincipalForEmailAddress(
                emailAddress
        ).build();
    }

    @Override
    public BadgeOsPrincipal getBadgeOsPrincipal(String emailAddress) {
        requireNonNull(emailAddress);
        LOGGER.info("Looking up Principal for " + emailAddress);

        try {
            InternetAddress internetAddress = new InternetAddress(emailAddress);
            return getBadgeOsPrincipal(internetAddress);
        } catch (AddressException e) {
            e.printStackTrace();
        }
        return null;
    }
}
