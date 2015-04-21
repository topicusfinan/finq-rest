package nl.finan.finq.rest;

import nl.finan.finq.dao.EnvironmentDao;
import nl.finan.finq.entities.Book;
import nl.finan.finq.entities.Environment;
import nl.finan.finq.entities.Story;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path(PathConstants.ENVIRONMENTS)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
public class EnvironmentResources {

    @EJB
    private EnvironmentDao environmentDao;

    @GET
    public List<Environment> getEnvironments() {
        return environmentDao.listAll();
    }

    @POST
    public Response createStory(Environment environment) {
        environmentDao.persist(environment);
        return Response.created(URI.create(PathConstants.ENVIRONMENTS + "/" + environment.getId())).entity(environment).build();
    }

}
