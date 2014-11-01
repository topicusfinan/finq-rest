package nl.finan.finq.rest;


import nl.finan.finq.dao.BookDao;
import nl.finan.finq.dao.RunningStoriesDao;
import nl.finan.finq.dao.ScenarioDao;
import nl.finan.finq.dao.StoryDao;
import nl.finan.finq.entities.*;
import nl.finan.finq.rest.to.RunTO;
import nl.finan.finq.rest.to.StoryTO;
import nl.finan.finq.service.RunnerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;

@Path("run")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class RunnerResources {

    private static final Logger LOGGER = LoggerFactory.getLogger(RunnerResources.class);

    @EJB
    private StoryDao storyDao;

    @EJB
    private BookDao bookDao;

    @EJB
    private ScenarioDao scenarioDao;

    @EJB
    private RunningStoriesDao runningStoriesDao;

    @EJB
    private RunnerService runnerService;

    @Context
    private UriInfo uriInfo;

    @POST
    @Path("/stories")
    @Transactional
    public Response runStory(RunTO run) throws NamingException {
        LOGGER.debug("Receiving post request with run {}", run);

        List<Story> stories = new ArrayList<>();
        for (StoryTO storyTO : run.getStories()) {
            Story story = storyDao.find(storyTO.getId());
            if (story == null) {
                return Response.status(Status.NOT_FOUND).build();
            }
            stories.add(story);
        }

        RunningStories runningStories = runnerService.run(stories);

        return Response.ok(runningStories).build();
    }

    @GET
    public List<RunningStories> getRuns(@QueryParam("status") List<String> statuses){
        if(statuses !=null && !statuses.isEmpty()){
            List<LogStatus> logStatuses = new ArrayList<>();
            for (String status : statuses) {
                logStatuses.add(LogStatus.valueOf(status));
            }
            
            return runningStoriesDao.findByStatuses(logStatuses);
        }
        return runningStoriesDao.listAll();
    }

}
