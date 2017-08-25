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
 * Created by jett on 8/6/17.
 */
package com.clueride.dao;

import java.util.Collections;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import com.clueride.domain.account.member.Member;
import com.clueride.domain.user.Badge;

/**
 * Configures bindings for the Service module.
 */
public class ServiceGuiceModuleTest extends AbstractModule {
    @Override
    protected void configure() {

    }

    @Provides
    private Member getMember() {
        return Member.Builder.builder()
                .withFirstName("ClueRide")
                .withLastName("Guest")
                .withEmailAddress("guest.dummy@clueride.com")
                .withBadges(Collections.singletonList(Badge.LOCATION_EDITOR))
                .withDisplayName("ClueRide Guest")
                .withPhone("123-456-7890")
                .build();
    }
}
