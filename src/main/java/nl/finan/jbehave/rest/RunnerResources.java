package nl.finan.jbehave.rest;


import nl.finan.jbehave.dao.BundleDao;
import nl.finan.jbehave.dao.RunningStoriesDao;
import nl.finan.jbehave.dao.StoryDao;
import nl.finan.jbehave.embeder.RunStories;
import nl.finan.jbehave.entities.Bundle;
import nl.finan.jbehave.entities.RunningStories;
import nl.finan.jbehave.entities.RunningStoriesStatus;
import nl.finan.jbehave.entities.Story;
import nl.finan.jbehave.factory.BeanFactory;
import nl.finan.jbehave.service.RunnerService;

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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import java.util.Arrays;

@Path("runner")
@Stateless
public class RunnerResources  {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RunnerResources.class);
	
    @EJB
    private StoryDao storyDao;
    
    @EJB
    private BundleDao bundleDao;

    @EJB
    private RunnerService runnerService;
	
	@POST
	@Path("/story")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    @Transactional
	public Response runStory(Long id) throws NamingException {
        Story story = storyDao.find(id);
		if(story == null){
			return Response.status(Status.NOT_FOUND).build();
		}

        RunningStories runningStories=  runnerService.run(story);
       
        return Response.ok(runningStories).build();
	}
	
	@POST
	@Path("/bundle")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Response runBundle(Long id){
		Bundle bundle = bundleDao.find(id);
		if(bundle == null){
			return Response.status(Status.NOT_FOUND).build();
		}
		
		RunningStories runningStories = runnerService.run(bundle);
		
		return Response.ok(runningStories).build();
	}
}
