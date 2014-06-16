package nl.finan.jbehave.rest;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import nl.finan.jbehave.dao.RunningStoriesDao;
import nl.finan.jbehave.dao.StoryDao;
import nl.finan.jbehave.entities.RunningStories;
import nl.finan.jbehave.entities.RunningStorysStatus;
import nl.finan.jbehave.entities.Story;
import nl.finan.jbehave.rest.embeder.FinanEmbedder;

import org.apache.cxf.jaxrs.impl.AsyncResponseImpl;
import org.jbehave.core.embedder.MetaFilter;
import org.jbehave.core.embedder.PerformableTree;
import org.jbehave.core.embedder.StoryManager.RunningStory;
import org.jbehave.core.failures.BatchFailures;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.annotation.ObjectIdGenerators.UUIDGenerator;
import org.springframework.transaction.annotation.Transactional;

@Path("runner")
@Repository
public class RunnerResources {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RunnerResources.class);
	
    @Autowired
    private StoryDao storyDao;
    
    @Autowired
    private FinanEmbedder embedder;

    @Autowired
    private RunningStoriesDao runningStoriesDao;
    
	
	@POST
	@Path("/story")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    @Transactional
	public Long runStory( Long id)
	{

        Story story = storyDao.find(id);
		
		org.jbehave.core.model.Story jbehaveStory = embedder.storyManager().storyOfText(story.toStory(), "db/"+story.getId());

        RunningStories runningStories = new RunningStories();
        runningStories.setStatus(RunningStorysStatus.RUNNING);
        runningStoriesDao.persist(runningStories);
        embedder.getRunningContext().get().setRunningStoriesId(runningStories.getId());

        LOGGER.info("Story is = {}", jbehaveStory.toString());
		try {
            embedder.runStories(Arrays.asList(jbehaveStory, jbehaveStory));
		} catch (Throwable e) {
			LOGGER.error("error in het runnen! {}", e.getMessage());
			e.printStackTrace();
		}
		return runningStories.getId();
	}

    @GET
    @Path("status/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public RunningStories getStatus(@PathParam("id") Long id){
        return runningStoriesDao.find(id);
    }

}
