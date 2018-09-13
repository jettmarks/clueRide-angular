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
import javax.mail.internet.InternetAddress;
import javax.persistence.EntityManager;

import com.clueride.infrastructure.db.WordPress;
import static java.util.Objects.requireNonNull;

/**
 * JPA implementation of the Badge OS Principal Store.
 */
public class BadgeOsPrincipalStoreJpa implements BadgeOsPrincipalStore {
    private final EntityManager entityManager;

    @Inject
    public BadgeOsPrincipalStoreJpa(
            @WordPress EntityManager entityManager
    ) {
        this.entityManager = entityManager;
    }

    @Override
    public BadgeOsPrincipal.Builder getBadgeOsPrincipalForEmailAddress(InternetAddress emailAddress) {
        requireNonNull(emailAddress);

        BadgeOsPrincipal.Builder principalBuilder;
        try {
            entityManager.getTransaction().begin();

            principalBuilder = entityManager
                    .createQuery(
                            "from badgeos_principal p where p.emailAddressString = :emailAddress",
                            BadgeOsPrincipal.Builder.class
                    )
                    .setParameter("emailAddress", emailAddress.toString())
                    .getSingleResult();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }

        return principalBuilder;
    }

}
