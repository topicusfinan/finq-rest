package nl.finan.jbehave.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.finan.jbehave.dao.StoryDao;
import nl.finan.jbehave.entities.Scenario;
import nl.finan.jbehave.entities.Story;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

@Path("stories")
@Singleton
@Transactional
public class StoriesResources {

    @EJB
    private StoryDao storyDao;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    //TODO: please refactor me, I want to return a List of Stories but this isn't possible because of the List<String> in the scenario... Dangit
    public String stories() throws IOException {

        List<Story> storiesList = storyDao.listAll();
        ObjectMapper mapper = new ObjectMapper();

        Writer writer = new StringWriter();
        mapper.writeValue(writer,storiesList);

        return writer.toString();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Story story(@PathParam("id") Long id){
    	return storyDao.find(id);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/scenarios")
    public List<Scenario> scenarios(@PathParam("id") Long id){
    	Story story = storyDao.find(id);
    	return story.getScenarios();    	
    }


    
}

