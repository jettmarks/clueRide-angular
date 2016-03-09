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
 * Created by jett on 2/6/16.
 */
package com.clueride.rest;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.clueride.domain.user.Badge;
import com.clueride.rest.dto.CRCredentials;

/**
 * Rest API for Authentication and Authorization functionality.
 */
@Path("login")
public class Login {
    @Context
    private HttpServletRequest request;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Badge> getBadgesForSession() {
        return (List<Badge>) request.getSession().getAttribute("badges");
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<Badge> authenticate(CRCredentials crCredentials) {
        List<Badge> result = new ArrayList<>();
        request.getSession(true);
        HttpSession session = request.getSession();
        if ("Jett".equals(crCredentials.name) && "adfhg".equals(crCredentials.password)) {
            result.add(Badge.TEAM_LEAD);
        }
        result.add(Badge.TEAM_MEMBER);
        session.setMaxInactiveInterval(12 * 60 * 60);
        session.setAttribute("badges", result);
        return result;
    }

    @DELETE
    public void logout() {
        request.getSession().invalidate();
    }
}
