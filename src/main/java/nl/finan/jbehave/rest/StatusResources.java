package nl.finan.jbehave.rest;

import nl.finan.jbehave.dao.RunningStoriesDao;
import nl.finan.jbehave.entities.RunningStories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("status")
@Repository
public class StatusResources {

    @Autowired
    private RunningStoriesDao runningStoriesDao;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public RunningStories getStatus(@PathParam("id") Long statusId){
        RunningStories runningStories = runningStoriesDao.find(statusId);
        return runningStories;
    }
}
