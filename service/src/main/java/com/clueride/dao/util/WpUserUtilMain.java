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
 * Created by jett on 9/1/18.
 */
package com.clueride.dao.util;

import java.util.Date;

import javax.persistence.EntityManager;

import com.clueride.domain.account.principal.EmailPrincipal;
import com.clueride.domain.account.wpuser.WpUser;
import com.clueride.domain.account.wpuser.WpUserService;
import com.clueride.domain.account.wpuser.WpUserServiceImpl;
import com.clueride.domain.account.wpuser.WpUserStore;
import com.clueride.domain.account.wpuser.WpUserStoreJpa;
import com.clueride.infrastructure.JpaUtil;

/**
 * Utility for matching up between Member and WpUser.
 *
 * And for command-line stuff.
 */
public class WpUserUtilMain {
    private static EntityManager entityManager;
    private static WpUserService wpUserService;
    private static WpUserStore wpUserStore;

    public static void main(String[] args) {
        try {
            instantiateServices();
            EmailPrincipal emailPrincipal = new EmailPrincipal("uname3@clueride.com");
            WpUser user = wpUserService.getUserByEmail(emailPrincipal);
            System.out.println(user);

            System.out.println("\nCreating new record\n");

            String baseName = "tName-" + (new Date().getTime() % 100000);
            WpUser.Builder builder = WpUser.Builder.builder()
                    .withAccountName(baseName)
                    .withDisplayName(baseName)
                    .withEmailString(baseName+"@clueride.com")
                    .withWebsiteString("https://clueride.com/" + baseName);
            WpUser savedUser = wpUserService.createUser(builder);
            System.out.println("New User: " + savedUser);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        System.exit(0);
    }

    private static void instantiateServices() {
        entityManager = JpaUtil.getWordPressEntityManagerFactory().createEntityManager();
        wpUserStore = new WpUserStoreJpa(entityManager);
        wpUserService = new WpUserServiceImpl(wpUserStore);
    }

}
