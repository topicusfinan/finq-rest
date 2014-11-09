package nl.finan.finq.rest;

import nl.finan.finq.dao.TagDao;
import nl.finan.finq.entities.Tag;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path(PathConstants.TAGS)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
public class TagResources {

    @EJB
    private TagDao tagDao;

    @GET
    public List<Tag> getTags() {
        return tagDao.listAll();
    }

    @POST
    public Response createTag(Tag tag) {
        if (tag == null || tag.getId() != null) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
        tagDao.persist(tag);
        return Response.created(URI.create(PathConstants.TAGS + "/" + tag.getId())).entity(tag).build();
    }
}
