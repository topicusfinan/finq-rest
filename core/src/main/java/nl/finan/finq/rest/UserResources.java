package nl.finan.finq.rest;

import nl.finan.finq.dao.UserDao;
import nl.finan.finq.entities.User;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

@Path(PathConstants.USER)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class UserResources {

    @EJB
    private UserDao userDao;

    @GET
    public Page<User> getUsers(@Context UriInfo uriInfo,
                               @QueryParam("firstName") String firstName,@QueryParam("lastName") String lastName,
                               @QueryParam("page") @DefaultValue("0") Integer page,
                               @QueryParam("size") @DefaultValue("20") Integer size){

        Long count = userDao.countByName(firstName, lastName);
        List<User> userList = userDao.findByName(firstName, lastName, page, size);
        return new Page<>(userList,count,page,size,uriInfo);
    }

    @POST
    public Response createUser(User user,@Context UriInfo uriInfo){
        if(user == null || user.getId() != null || user.getFirstName() == null || user.getLastName() == null){
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
        userDao.persist(user);
        URI uri = uriInfo.getAbsolutePathBuilder().path(user.getId().toString()).build();
        return Response.created(uri).build();
    }

    @GET
    @Path("{id}")
    public Response getUser(@PathParam("id") Long id){
        User user = userDao.find(id);
        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(user).build();
    }
}
