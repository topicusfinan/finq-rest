package nl.finan.finq.rest;

import nl.finan.finq.dao.SetDao;
import nl.finan.finq.entities.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path(PathConstants.SETS)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
public class SetResources {

    @EJB
    private SetDao setDao;

    @GET
    public List<Set> getSets(){
        return  setDao.listAll();
    }
}
