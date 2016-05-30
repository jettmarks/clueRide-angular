package com.clueride.rest;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.clueride.domain.account.Member;
import com.clueride.service.TeamService;

/**
 * Team resource (exposed at "team" path).
 *
 * PUT - Creates a New Team with the payload representing the full object.
 * GET - Retrieves the entire Team object as represented on server or a list of teams when no team ID is provided.
 * POST - Adds member to the team (request to join, or from team leader, confirmation of having joined).
 * DELETE - Removes member from the team when individual is supplied; otherwise, the entire team is closed.
 */
@Path("team")
public class Team {
    private final TeamService teamService;

    @Inject
    public Team(TeamService teamService) {
        this.teamService = teamService;
    }

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{teamId}")
    public com.clueride.domain.Team getTeamById(@PathParam("teamId") Integer teamId) {
        return teamService.getTeam(teamId);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{teamId}")
    public com.clueride.domain.Team addMember(@PathParam("teamId") Integer teamId, Member newMember) {
        return teamService.addMember(teamId, newMember);
    }
}
