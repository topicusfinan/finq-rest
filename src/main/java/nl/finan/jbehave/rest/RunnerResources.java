package nl.finan.jbehave.rest;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import nl.finan.jbehave.dao.StoryDao;
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

@Path("runner")
@Repository
public class RunnerResources {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RunnerResources.class);
	
    @Autowired
    private StoryDao storyDao;
    
    @Autowired
    private FinanEmbedder embedder;
    
	
	@POST
	@Path("/story")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void runStory( Long id)
	{
		Story story = storyDao.find(id);
		
		org.jbehave.core.model.Story jbehaveStory =embedder.storyManager().storyOfText(story.toStory(), story.getName());
		//jbehaveStory = new org.jbehave.core.model.Story("", jbehaveStory.getDescription(),jbehaveStory.getNarrative(),jbehaveStory.getScenarios());
		
		
		try {
			embedder.storyRunner().run(embedder.configuration(), embedder.stepsFactory().createCandidateSteps(), jbehaveStory);
			
		} catch (Throwable e) {
			LOGGER.error("error in het runnen! {}", e.getMessage());
			e.printStackTrace();
		}
		
	}
}
