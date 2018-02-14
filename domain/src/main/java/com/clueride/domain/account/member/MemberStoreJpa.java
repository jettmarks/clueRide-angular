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
 * Created by jett on 8/13/17.
 */
package com.clueride.domain.account.member;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.mail.internet.InternetAddress;
import javax.persistence.EntityManager;

import static java.util.Objects.requireNonNull;

/**
 * JPA Implementation of the MemberStore (DAO) interface.
 */
public class MemberStoreJpa implements MemberStore {
    private final EntityManager entityManager;
    @Inject
    public MemberStoreJpa(
            @Nonnull EntityManager entityManager
    ) {
        this.entityManager = requireNonNull(entityManager);
    }

    @Override
    public Integer addNew(Member member) throws IOException {
        entityManager.getTransaction().begin();
        entityManager.persist(Member.Builder.from(member));
        entityManager.getTransaction().commit();
        return member.getId();
    }

    @Override
    public Member getMemberById(Integer id) {
        return null;
    }

    @Override
    public List<Member> getMemberByName(String name) {
        return null;
    }

    @Override
    public Member getMemberByEmail(InternetAddress emailAddress) {
        Member.Builder memberBuilder;
        try {
            entityManager.getTransaction().begin();
            memberBuilder = entityManager
                    .createQuery(
                            "from member m where m.emailAddress = :emailAddress",
                            Member.Builder.class
                    )
                    .setParameter("emailAddress", emailAddress.toString())
                    .getSingleResult();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }

        return memberBuilder.build();
    }

    @Override
    public void update(Member member) {

    }

    @Override
    public List<Member> getAllMembers() {
        return Collections.emptyList();
    }
}
