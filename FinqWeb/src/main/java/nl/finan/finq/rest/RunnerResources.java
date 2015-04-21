package nl.finan.finq.rest;


import nl.finan.finq.annotation.Authorized;
import nl.finan.finq.common.to.TotalStatus;
import nl.finan.finq.dao.EnvironmentDao;
import nl.finan.finq.dao.RunningStoriesDao;
import nl.finan.finq.dao.StoryDao;
import nl.finan.finq.entities.Environment;
import nl.finan.finq.entities.LogStatus;
import nl.finan.finq.entities.RunningStories;
import nl.finan.finq.entities.Story;
import nl.finan.finq.entities.User;
import nl.finan.finq.interceptor.AuthenticationInterceptor;
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
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
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

	@EJB
	private EnvironmentDao environmentDao;

    @POST
    @Path("/stories")
    @Transactional
	@Authorized
	public RunningStories runStory(RunTO run) throws NamingException
	{
        LOGGER.debug("Receiving post request with run {}", run);
		User user = AuthenticationInterceptor.USER_THREAD_LOCAL.get();

        List<Story> stories = new ArrayList<>();
        for (StoryTO storyTO : run.getStories()) {
            Story story = storyDao.find(storyTO.getId());
            if (story == null) {
				throw new WebApplicationException("Story was not found", Status.NOT_FOUND);
            }
            stories.add(story);
        }

		Environment environment = environmentDao.find(run.getEnvironmentId());
		if (environment == null)
		{
			throw new WebApplicationException("Environment was not found", Status.NOT_FOUND);
		}

        RunningStories runningStories = runnerService.run(stories,user,environment);

		return runningStories;
    }

    @GET
    public Page<TotalStatus> getRuns(@Context UriInfo uriInfo, @QueryParam("status") List<String> statuses,
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
        List<TotalStatus> statusList = new ArrayList<>();
        for (RunningStories runningStories : resultList) {
            statusList.add(new TotalStatus(runningStories));
        }

        return new Page<>(statusList, count, page, size, uriInfo);
    }

}
