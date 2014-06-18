package nl.finan.jbehave.rest;


import java.util.Arrays;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import nl.finan.jbehave.dao.RunningStoriesDao;
import nl.finan.jbehave.dao.StoryDao;
import nl.finan.jbehave.entities.RunningStories;
import nl.finan.jbehave.entities.RunningStorysStatus;
import nl.finan.jbehave.entities.Story;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;

@Path("runner")
@Repository
public class RunnerResources implements ApplicationContextAware {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RunnerResources.class);
	
    @Autowired
    private StoryDao storyDao;


    @Autowired
    private RunningStoriesDao runningStoriesDao;

    private ApplicationContext context;

    @Resource
    private ThreadPoolTaskExecutor runExecutor;

	
	@POST
	@Path("/story")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    @Transactional
	public Long runStory(Long id)
	{

        Story story = storyDao.find(id);

        RunningStories runningStories = new RunningStories();
        runningStories.setStatus(RunningStorysStatus.RUNNING);
        runningStoriesDao.persist(runningStories);

        RunStories runStories = context.getBean(RunStories.class);
        runStories.init(Arrays.asList(story),runningStories.getId());

        runExecutor.execute(runStories);

		return runningStories.getId();
	}

    @GET
    @Path("status/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public RunningStories getStatus(@PathParam("id") Long id){
        return runningStoriesDao.find(id);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
