package nl.finan.jbehave.rest;

import nl.finan.jbehave.rest.embeder.FinanEmbedder;
import nl.finan.jbehave.rest.resources.ScenarioType;
import nl.finan.jbehave.rest.utils.StoryUtils;

import org.jbehave.core.annotations.Named;
import org.jbehave.core.embedder.StoryManager;
import org.jbehave.core.model.Scenario;
import org.jbehave.core.model.Story;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

@Path("/stories")
@Repository
public class StoriesResources {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StoriesResources.class);

    @Autowired
    private FinanEmbedder embedder;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> stories() {

        StoryManager manager = embedder.storyManager();

        List<Story> stories = StoryUtils.getStoriesFromPath(embedder.storyPaths(), manager);
        List<String> storyTitles = new ArrayList<String>();
        for (Story s : stories) {
            storyTitles.add(s.getName());
        }
        
        return storyTitles;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{storyName}/scenarios")
    public List<ScenarioType> scenarios(@QueryParam("storyName")String storyName ){
    	LOGGER.info("Story name = {} ",storyName);
    	
    	StoryManager manager = embedder.storyManager();
    	List<Story> stories = StoryUtils.getStoriesFromPath(embedder.storyPaths(), manager);
        List<ScenarioType> scenarios = new ArrayList<ScenarioType>();
    	for(Story s: stories){
        	if(s.getName().equals(storyName)){
        		for(Scenario scenario: s.getScenarios()){
        			scenarios.add(new ScenarioType(scenario.getTitle(), scenario.getSteps()));
        		}
        	}
        }
    	
    	return scenarios;
    }
    
}

