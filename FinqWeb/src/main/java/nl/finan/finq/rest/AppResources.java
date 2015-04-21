package nl.finan.finq.rest;

import nl.finan.finq.dao.ApplicationDao;
import nl.finan.finq.entities.Application;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path(PathConstants.APP)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
public class AppResources {

    @EJB
    private ApplicationDao applicationDao;

    @GET
    public Application getApp() {
        List<Application> applications = applicationDao.listAll();
        if (applications.isEmpty()) {
            return new Application();
        }
        return applications.get(0);
    }

}
