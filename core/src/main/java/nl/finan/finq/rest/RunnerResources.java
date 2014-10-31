package nl.finan.finq.rest;


import nl.finan.finq.dao.BookDao;
import nl.finan.finq.dao.ScenarioDao;
import nl.finan.finq.dao.StoryDao;
import nl.finan.finq.entities.Book;
import nl.finan.finq.entities.RunningStories;
import nl.finan.finq.entities.Scenario;
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
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
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

}
