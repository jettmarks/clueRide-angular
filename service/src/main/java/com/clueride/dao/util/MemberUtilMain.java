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
 * Created by jett on 8/15/17.
 */
package com.clueride.dao.util;

import java.io.IOException;
import java.util.List;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;

import com.clueride.domain.account.member.Member;
import com.clueride.domain.account.member.MemberStore;
import com.clueride.infrastructure.Jpa;
import com.clueride.infrastructure.Json;
import com.clueride.infrastructure.ServiceGuiceModule;

/**
 * Moves records from one Store to another.
 */
public class MemberUtilMain {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new ServiceGuiceModule());

        MemberStore memberStoreJson = injector.getInstance(
                Key.get(
                        MemberStore.class,
                        Json.class
                )
        );
        MemberStore memberStoreJpa = injector.getInstance(
                Key.get(
                        MemberStore.class,
                        Jpa.class
                )
        );

        List<Member> members = memberStoreJson.getAllMembers();
        for (Member member : members) {
            try {
                memberStoreJpa.addNew(member);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
