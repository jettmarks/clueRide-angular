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

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import com.clueride.domain.account.principal.EmailPrincipal;
import com.clueride.infrastructure.db.WordPress;
import static java.util.Objects.requireNonNull;

/**
 * JPA Implementation of WpUser persistence.
 */
public class WpUserStoreJpa implements WpUserStore {
    private static final Logger LOGGER = Logger.getLogger(WpUserStoreJpa.class);
    private final EntityManager entityManager;

    public WpUserStoreJpa(
            @WordPress EntityManager entityManager
    ) {
        this.entityManager = requireNonNull(entityManager);
    }

    @Override
    public WpUser.Builder getWpUser(EmailPrincipal emailPrincipal) {
        requireNonNull(emailPrincipal);

        String emailAddress = emailPrincipal.getName();
        LOGGER.debug("Looking up for email address " + emailAddress);
        entityManager.getTransaction().begin();
        WpUser.Builder builder = entityManager.createQuery(
                "FROM wp_users WHERE user_email = :email",
                WpUser.Builder.class
        )
                .setParameter("email", emailAddress)
                .getSingleResult();
        entityManager.getTransaction().commit();
        return builder;
    }

    @Override
    public WpUser.Builder createWpUser(WpUser.Builder builder) {
        requireNonNull(builder);
        entityManager.getTransaction().begin();
        entityManager.persist(builder);
        entityManager.getTransaction().commit();
        return builder;
    }

}
