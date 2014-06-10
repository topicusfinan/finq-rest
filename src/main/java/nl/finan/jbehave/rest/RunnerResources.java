package nl.finan.jbehave.rest;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import nl.finan.jbehave.dao.StoryDao;
import nl.finan.jbehave.entities.Story;
import nl.finan.jbehave.rest.embeder.FinanEmbedder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Path("runner")
@Repository
public class RunnerResources {
	
    @Autowired
    private StoryDao storyDao;
    
    @Autowired
    private FinanEmbedder embedder;
	
	@POST
	@Path("story")
	public Response runStory(Long id)
	{
		Story story = storyDao.find(id);
		
		return Response.ok().build();
	}
}
