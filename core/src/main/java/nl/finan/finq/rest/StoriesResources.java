package nl.finan.finq.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.eernie.jmoribus.GherkinsListener;
import nl.eernie.jmoribus.parser.ParseableStory;
import nl.eernie.jmoribus.parser.StoryParser;
import nl.finan.finq.dao.StoryDao;
import nl.finan.finq.entities.Scenario;
import nl.finan.finq.entities.Story;
import nl.finan.finq.service.StoryService;
import org.apache.commons.io.IOUtils;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

@Path("stories")
@Transactional
@Stateless
public class StoriesResources {

    @EJB
    private StoryDao storyDao;

    @EJB
    private StoryService storyService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Story> stories() throws IOException {

        List<Story> storiesList = storyDao.listAll();

        return storiesList;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Story story(@PathParam("id") Long id) {
        return storyDao.find(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/scenarios")
    public List<Scenario> scenarios(@PathParam("id") Long id) {
        Story story = storyDao.find(id);
        return story.getScenarios();
    }

    @PUT
    @Transactional
    @Consumes("text/plain")
    @Path("/{name}/save")
    public void saveStory(@PathParam("name") String name, String story)
    {
        try
        {
            ParseableStory ps = new ParseableStory(IOUtils.toInputStream(story, "UTF-8"), name);
            nl.eernie.jmoribus.model.Story s = StoryParser.parseStory(ps);
            Story story1 = storyService.convertAndSaveStory(s);
            System.out.println("name: " + name + ", story: " + story + ", entity: "+ story1);
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }

    }


}

