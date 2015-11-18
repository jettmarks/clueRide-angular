package com.clueride.rest;

import com.clueride.service.TeamService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Team resource (exposed at "team" path).
 *
 * PUT - Creates a New Team with the payload representing the full object.
 * GET - Retrieves the entire Team object as represented on server.
 * POST - Adds member to the team (request to join, or from team leader, confirmation of having joined).
 * DELETE - Removes member from the team when individual is supplied; otherwise, the entire team is closed.
 */
@Path("team")
public class Team {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getIt() {
        return TeamService.getInstance().getTeamAsJson();
    }
}
