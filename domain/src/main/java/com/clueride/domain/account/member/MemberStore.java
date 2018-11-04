/*
 * Copyright 2016 Jett Marks
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
 * Created by jett on 6/25/16.
 */
package com.clueride.domain.account.member;

import java.io.IOException;
import java.util.List;

import javax.mail.internet.InternetAddress;

/**
 * Persistence interface for {@link Member} instances.
 */
public interface MemberStore {
    /**
     * Accepts fully-constructed {@link Member} to the store and returns the ID.
     * @param member - instance to be persisted.
     * @return Unique Integer ID for the new Member.
     * @throws IOException if trouble persisting.
     */
    Integer addNew(Member member) throws IOException;

    /**
     * Retrieves the {@link Member} instance matching the ID.
     * @param id - Unique ID for the Member.
     * @return Unique matching instance.
     */
    Member getMemberById(Integer id);

    /**
     * Retrieves the list of {@link Member} whose name matches.
     * Note that names are not checked for uniqueness; this won't be the PK field
     * for the record.
     * @param name - String account name for the Member.
     * @return All matching instances.
     */
    List<Member> getMemberByName(String name);

    /**
     * Retrieves the {@link Member} instance matching the emailAddress.
     * @param emailAddress - InternetAddress for the member.
     * @return Unique matching instance.
     */
    Member getMemberByEmail(InternetAddress emailAddress);

    void update(Member member);

    /**
     * Returns the entire list of members contained within the store.
     * TODO: This won't be sustainable once we have a significant number of members, but it's sufficient for testing.
     * @return List of the Members currently defined.
     */
    List<Member.Builder> getAllMembers();

}
