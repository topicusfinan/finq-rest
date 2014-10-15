package nl.finan.finq.rest;

import nl.finan.finq.dao.EnvironmentDao;
import nl.finan.finq.entities.Environment;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path(PathConstants.ENVIRONMENTS)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
public class EnvironmentResources {

    @EJB
    private EnvironmentDao environmentDao;

    @GET
    public List<Environment> getEnvironments(){
        return environmentDao.listAll();
    }

}
