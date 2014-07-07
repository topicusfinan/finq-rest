package nl.finan.jbehave.rest;


import nl.finan.jbehave.dao.RunningStoriesDao;
import nl.finan.jbehave.dao.StoryDao;
import nl.finan.jbehave.embeder.RunStories;
import nl.finan.jbehave.entities.RunningStories;
import nl.finan.jbehave.entities.RunningStoriesStatus;
import nl.finan.jbehave.entities.Story;
import nl.finan.jbehave.factory.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;

@Path("runner")
@Stateless
public class RunnerResources  {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RunnerResources.class);
	
    @EJB
    private StoryDao storyDao;


    @EJB
    private RunningStoriesDao runningStoriesDao;

    @Resource
    private ManagedExecutorService runExecutor;

	
	@POST
	@Path("/story")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    @Transactional
	public Long runStory(Long id) throws NamingException {

        Story story = storyDao.find(id);

        RunningStories runningStories = new RunningStories();
        runningStories.setStatus(RunningStoriesStatus.RUNNING);
        runningStoriesDao.persist(runningStories);

        Object rs =  new InitialContext().lookup("java:module/RunStories");
        RunStories runStories = BeanFactory.getBean(RunStories.class);
        runStories.init(Arrays.asList(story),runningStories.getId());

        runExecutor.execute(runStories); //TODO: take a look at callable<T>. Maybe we can use this to pause a thread!


		return runningStories.getId();
	}

    @GET
    @Path("status/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public RunningStories getStatus(@PathParam("id") Long id){
        return runningStoriesDao.find(id);
    }

}
