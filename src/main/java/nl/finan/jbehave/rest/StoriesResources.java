package nl.finan.jbehave.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import nl.finan.jbehave.entities.Story;
import nl.finan.jbehave.entities.StoryDao;
import nl.finan.jbehave.rest.embeder.FinanEmbedder;
import nl.finan.jbehave.rest.resources.ScenarioType;
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
    public List<Story> stories() {

    	List<Story> storiesList = storyDao.listAll();
        return storiesList;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{storyName}/scenarios")
    public List<ScenarioType> scenarios(@PathParam("storyName")String storyName ){
    	LOGGER.info("Story name = {} ",storyName);
    	
    	return null;
    }
    
}

