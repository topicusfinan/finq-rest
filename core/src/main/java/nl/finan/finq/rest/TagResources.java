package nl.finan.finq.rest;

import nl.finan.finq.dao.TagDao;
import nl.finan.finq.entities.Tag;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path(PathConstants.TAGS)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
public class TagResources {

    @EJB
    private TagDao tagDao;

    @GET
    public List<Tag> getTags(){
        return tagDao.listAll();
    }
}
