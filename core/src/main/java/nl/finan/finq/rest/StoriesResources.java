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

@Path(PathConstants.BOOKS +"/{bookId}/"+ PathConstants.STORIES)
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
    public Response stories(@PathParam("bookId") Long bookId) throws IOException {
        Book book = bookDao.find(bookId);
        if(book == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(book.getStories()).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createStory(@PathParam("bookId") Long bookId, Story story){
        Book book = bookDao.find(bookId);
        if(book == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        story.setBook(book);
        storyService.addParentsToChilds(story);
        storyDao.persist(story);
        return Response.created(URI.create(PathConstants.BOOKS +"/"+bookId+"/"+ PathConstants.STORIES+"/"+story.getId())).entity(story).build();
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

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/{name}")
    public Response saveStory(@PathParam("name") String name, @PathParam("bookId") Long bookId, String story){

        Book book = bookDao.find(bookId);
        if(book == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        try{
            ParseableStory ps = new ParseableStory(IOUtils.toInputStream(story, "UTF-8"), name);
            nl.eernie.jmoribus.model.Story s = StoryParser.parseStory(ps);
            Story story1 = storyService.convertAndSaveStory(s);
            story1.setBook(book);
            return Response.created(URI.create(PathConstants.BOOKS +"/"+bookId+"/"+ PathConstants.STORIES+"/"+story1.getId())).entity(story).build();
        }
        catch(IOException ioe){

            LOGGER.error("Something went wrong wile parsing the story",ioe);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }


}

