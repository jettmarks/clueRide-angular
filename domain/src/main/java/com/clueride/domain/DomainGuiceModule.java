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
package com.clueride.domain;

import javax.persistence.EntityManager;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import com.clueride.domain.account.member.MemberStore;
import com.clueride.domain.account.member.MemberStoreJpa;
import com.clueride.infrastructure.JpaUtil;

/**
 * Guice Bindings for the Domain module.
 */
public class DomainGuiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(MemberStore.class).to(MemberStoreJpa.class);
    }

    @Provides
    private EntityManager getEntityManager() {
        return JpaUtil.getEntityManagerFactory().createEntityManager();
    }
}
