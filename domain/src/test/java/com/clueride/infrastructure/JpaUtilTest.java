package com.clueride.infrastructure;/*
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
 * Created by jett on 8/12/17.
 */

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.clueride.domain.account.Member;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * Exercises the JpaUtilTest class.
 * This is an Integration test in that it actually connects to a running instance of the database.
 */
public class JpaUtilTest {
    private EntityManagerFactory entityManagerFactory;



    @BeforeClass
    public void setUp() {
        entityManagerFactory = JpaUtil.getEntityManagerFactory();
        System.out.println("Entity Manager opened");
    }

    @AfterClass
    public void tearDown() {
        entityManagerFactory.close();
        System.out.println("Entity Manager closed");
    }

    @Test
    public void testJpaUtil() {
        assertNotNull(entityManagerFactory);
    }

    @Test
    public void testJapUtil_readTable() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        String sql = "from member";
        List<Member.Builder> memberBuilders = entityManager
                .createQuery(sql, Member.Builder.class)
                .getResultList();
        entityManager.getTransaction().commit();
        assertNotNull(memberBuilders);
        assertTrue(memberBuilders.size() > 0);
        for (Member.Builder memberBuilder : memberBuilders) {
            System.out.println(memberBuilder.build());
        }
    }


}