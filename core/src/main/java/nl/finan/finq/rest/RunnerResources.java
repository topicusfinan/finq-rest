package nl.finan.finq.rest;


import nl.finan.finq.dao.BundleDao;
import nl.finan.finq.dao.ScenarioDao;
import nl.finan.finq.dao.StoryDao;
import nl.finan.finq.entities.Bundle;
import nl.finan.finq.entities.RunningStories;
import nl.finan.finq.entities.Scenario;
import nl.finan.finq.entities.Story;
import nl.finan.finq.service.RunnerService;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("runner")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class RunnerResources {

    @EJB
    private StoryDao storyDao;

    @EJB
    private BundleDao bundleDao;

    @EJB
    private ScenarioDao scenarioDao;

    @EJB
    private RunnerService runnerService;

    @POST
    @Path("/story")
    @Transactional
    public Response runStory(Long id) throws NamingException {

        Story story = storyDao.find(id);
        if (story == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        RunningStories runningStories = runnerService.run(story);

        return Response.ok(runningStories).build();
    }

    @POST
    @Path("/bundle")
    @Transactional
    public Response runBundle(Long id) {
        Bundle bundle = bundleDao.find(id);
        if (bundle == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        RunningStories runningStories = runnerService.run(bundle);

        return Response.ok(runningStories).build();
    }

    @POST
    @Path("/scenario")
    @Transactional
    public Response runScenario(Long id) {
        Scenario scenario = scenarioDao.find(id);
        if (scenario == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        RunningStories runningStories = runnerService.run(scenario);

        return Response.ok(runningStories).build();
    }
}
