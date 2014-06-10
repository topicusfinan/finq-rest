package nl.finan.jbehave.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import nl.finan.jbehave.entities.StoryDao;
import nl.finan.jbehave.rest.embeder.FinanEmbedder;
import nl.finan.jbehave.rest.resources.ScenarioType;
import nl.finan.jbehave.rest.utils.StoryUtils;

import org.jbehave.core.embedder.StoryManager;
import org.jbehave.core.model.Scenario;
import org.jbehave.core.model.Story;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Path("/stories")
@Repository
public class StoriesResources {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StoriesResources.class);

    @Autowired
    private FinanEmbedder embedder;

    @Autowired
    private StoryDao storyDao;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> stories() {
    	List<String> storyTitles = new ArrayList<String>();

    	List<nl.finan.jbehave.entities.Story> storiesList = storyDao.listAll();
    	
    	for(nl.finan.jbehave.entities.Story s : storiesList)
    	{
    		storyTitles.add(s.getName());
    	}
    	
        StoryManager manager = embedder.storyManager();

        List<Story> stories = StoryUtils.getStoriesFromPath(embedder.storyPaths(), manager);
        for (Story s : stories) {
            storyTitles.add(s.getName());
        }
        
        return storyTitles;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{storyName}/scenarios")
    public List<ScenarioType> scenarios(@PathParam("storyName")String storyName ){
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

