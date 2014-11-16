package nl.finan.finq.rest;


import nl.finan.finq.dao.BookDao;
import nl.finan.finq.dao.RunningStoriesDao;
import nl.finan.finq.dao.ScenarioDao;
import nl.finan.finq.dao.StoryDao;
import nl.finan.finq.entities.LogStatus;
import nl.finan.finq.entities.RunningStories;
import nl.finan.finq.entities.Story;
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
import java.util.Date;
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
    private RunningStoriesDao runningStoriesDao;

    @EJB
    private RunnerService runnerService;

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
    public Page<RunningStories> getRuns(@Context UriInfo uriInfo, @QueryParam("status") List<String> statuses,
                                        @QueryParam("since") Date since, @QueryParam("until") Date until,
                                        @QueryParam("page") @DefaultValue("0") Integer page,
                                        @QueryParam("size") @DefaultValue("20") Integer size) {

        List<LogStatus> logStatuses = null;
        if (statuses != null && !statuses.isEmpty()) {
            logStatuses = new ArrayList<>();
            for (String status : statuses) {
                logStatuses.add(LogStatus.valueOf(status));
            }
        }
        Long count = runningStoriesDao.countByDateAndStatuses(logStatuses, since, until);
        List<RunningStories> resultList = runningStoriesDao.findByDateAndStatuses(logStatuses, since, until, page, size);

        return new Page<>(resultList, count, page, size, uriInfo);
    }

}
