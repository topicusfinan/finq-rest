package nl.finan.finq.rest;

import nl.eernie.jmoribus.parser.ParseableStory;
import nl.eernie.jmoribus.parser.StoryParser;
import nl.finan.finq.dao.BookDao;
import nl.finan.finq.dao.StoryDao;
import nl.finan.finq.entities.Book;
import nl.finan.finq.entities.Scenario;
import nl.finan.finq.entities.Story;
import nl.finan.finq.service.BookService;
import nl.finan.finq.service.StoryService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.util.List;

@Path(PathConstants.STORIES)
@Transactional
@Stateless
public class StoriesResources {

    private static final Logger LOGGER = LoggerFactory.getLogger(StoriesResources.class);

    @EJB
    private StoryDao storyDao;

    @EJB
    private BookDao bookDao;

    @EJB
    private BookService bookService;

    @EJB
    private StoryService storyService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Story> stories() throws IOException {

        List<Story> storiesList = storyDao.listAll();

        return storiesList;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createStory(Story story){
        Book book = bookService.updateOrCreateEntity(story.getBook());
        if(book == null){
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
        story.setBook(book);
        storyDao.persist(story);
        return Response.created(URI.create(PathConstants.STORIES+"/"+story.getId())).build();
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
    @Consumes(MediaType.TEXT_PLAIN)
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

