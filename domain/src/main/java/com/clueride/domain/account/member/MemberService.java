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
 * Created by jett on 6/26/16.
 */
package com.clueride.domain.account.member;

import java.util.List;

/**
 * Provides business-layer services for Members and their Badges.
 */
public interface MemberService {
    /**
     * Retrieve a Member instance by ID.
     * @param id - Unique identifier for the Member record.
     * @return Matching instance of Member including Badges.
     */
    Member getMember(Integer id);

    /**
     * Retrieve Member instances by displayName.
     * @param displayName - Name by which the Member appears within teams; not necessarily Unique.
     * @return Matching instance of Member.
     */
    List<Member> getMemberByDisplayName(String displayName);

    /**
     * Retrieve Member instances by Email Address (Principal).
     * @param email String representation of what should be a valid email address.
     * @return Matching instance of Member.
     */
    Member getMemberByEmail(String email);

    /**
     * Sets up a brand new user with the given email address.
     * If the user already exists, that Member account is returned.
     * @param email for the new user.
     * @return new instance of Member.
     */
    Member createNewMemberWithEmail(String email);

    /**
     * Returns the entire list of members contained within the store.
     * TODO: This won't be sustainable once we have a significant number of members, but it's sufficient for testing.
     * @return List of the Members currently defined.
     */
    List<Member> getAllMembers();

}
