/*
 * Copyright 2015 Jett Marks
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
 * Created by jett on 12/1/15.
 */
package com.clueride.io;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.clueride.domain.account.member.Member;
import com.clueride.domain.course.Course;
import com.clueride.domain.course.CourseType;
import com.clueride.domain.invite.Invitation;
import com.clueride.domain.user.Badge;
import com.clueride.domain.user.Clue;
import com.clueride.domain.user.location.Location;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * Exercises the PojoJsonUtilTest class.
 *
 * This is on the way out; won't be using JSON to store Pojo's.
 */
public class PojoJsonUtilTest {

    private PojoJsonService toTest = new PojoJsonUtil();

    @BeforeMethod
    public void setUp() throws Exception {
        toTest = new PojoJsonUtil();
    }

    @Test
    public void testGetFileForLocation() throws Exception {

    }

//    @Test
    public void testLoadLocations() throws Exception {
        List<Location> locations = toTest.loadLocations();
        assertNotNull(locations);
        assertTrue(locations.size() > 0);
    }

//    @Test
    public void testLoadLocationId() throws Exception {
        Location location = PojoJsonUtil.loadLocationId(1);
        assertNotNull(location);
        assertEquals(location.getId(), new Integer(1));
    }

//    @Test
    public void testLoadClueId() throws Exception {
        Clue clue = PojoJsonUtil.loadClue(1);
        assertNotNull(clue);
        assertEquals(clue.getId(), new Integer(1));
    }

    @Test
    public void testLoadJsonObjects_Course() throws Exception {
        List<Course> jsonObjects = PojoJsonUtil.loadCourses();
        assertNotNull(jsonObjects);
        assertTrue(jsonObjects.size() > 0);
    }

//    @Test
    public void testLoadJsonObjects_Invitation() throws Exception {
        List<Invitation> jsonObjects = PojoJsonUtil.loadJsonObjects(JsonStoreType.INVITATION);
        assertNotNull(jsonObjects);
        assertTrue(jsonObjects.size() > 0);
        Invitation firstInvite = jsonObjects.get(0);
        assertNotNull(firstInvite);
        System.out.println("Token: " + firstInvite.getToken());

        Integer memberId = firstInvite.getMemberId();
        assertNotNull(memberId);
    }

//    @Test
    public void testLoadJsonObjects_Member() throws Exception {
        List<Member> jsonObjects = PojoJsonUtil.loadJsonObjects(JsonStoreType.MEMBER);
        assertNotNull(jsonObjects);
        assertTrue(jsonObjects.size() > 0);
        Member firstMember = jsonObjects.get(0);
        assertNotNull(firstMember);
        System.out.println("Email Address: " + firstMember.getEmailAddress());
        for (Badge badge : firstMember.getBadges()) {
            System.out.println(badge);
        }
    }

    @Test
    public void testLoadJsonObjects_CourseType() throws Exception {
        List<CourseType> jsonObjects = PojoJsonUtil.loadJsonObjects(JsonStoreType.COURSE_TYPE);
        assertNotNull(jsonObjects);
        assertTrue(jsonObjects.size() > 0);
        CourseType firstCourseType = jsonObjects.get(0);
        assertNotNull(firstCourseType);
        System.out.print("Name/Type: " + firstCourseType.getType());
        System.out.println(" Description: " + firstCourseType.getDescription());
    }

}