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
 * Created by jett on 9/22/18.
 */
package com.clueride.domain.invite;

import java.io.IOException;
import java.util.List;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import com.clueride.infrastructure.db.ClueRide;

/**
 * JPA persistence for the Invitation entity.
 */

public class InvitationStoreJpa implements InvitationStore {
    private static final Logger LOGGER = Logger.getLogger(InvitationStoreJpa.class);
    private final EntityManager entityManager;

    @Inject
    public InvitationStoreJpa(
            @Nonnull @ClueRide EntityManager entityManager
    ) {
        this.entityManager = entityManager;
    }

    @Override
    public Integer addNew(Invitation.Builder builder) throws IOException {
        entityManager.getTransaction().begin();
        entityManager.persist(builder);
        entityManager.getTransaction().commit();
        return builder.getId();
    }

    @Override
    public List<Invitation.Builder> getInvitationsByOuting(Integer outingId) {
        return null;
    }

    @Override
    public Invitation.Builder getInvitationByToken(String token) {
        return null;
    }

    @Override
    public List<Invitation.Builder> getUpcomingInvitationsByMemberId(Integer memberId) {
        LOGGER.debug("Retrieving invitations for member ID " + memberId);
        List<Invitation.Builder> builders;
        entityManager.getTransaction().begin();
        // TODO: Ordering and State should be part of the query
        builders = entityManager.createQuery(
                "SELECT i FROM invite i where i.memberId = :memberId"
        ).setParameter("memberId", memberId)
                .getResultList();
        entityManager.getTransaction().commit();
        return builders;
    }

}
