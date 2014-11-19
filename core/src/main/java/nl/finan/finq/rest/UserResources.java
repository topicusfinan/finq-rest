package nl.finan.finq.rest;

import java.net.URI;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import nl.finan.finq.dao.UserDao;
import nl.finan.finq.entities.User;
import nl.finan.finq.entities.UserToken;
import nl.finan.finq.rest.to.LoginTO;
import nl.finan.finq.service.UserService;
import nl.finan.finq.to.UserTO;

import org.mindrot.jbcrypt.BCrypt;

@Path(PathConstants.USER)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class UserResources {

    @EJB
    private UserDao userDao;

	@EJB
	private UserService userService;

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
	public Response createUser(UserTO userTO, @Context UriInfo uriInfo)
	{
		if (userTO == null || userTO.getFirstname() == null || userTO.getLastname() == null)
		{
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
		User user = userService.createUser(userTO);
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

	@POST
	@Path("login")
	public Response login(LoginTO loginTO)
	{
		User user = userDao.findByUserName(loginTO.getUsername());
		if (user == null)
		{
			return Response.status(Response.Status.UNAUTHORIZED).entity("User not found").build();
		}
		if (!BCrypt.checkpw(loginTO.getPassword(), user.getPassword()))
		{
			return Response.status(Response.Status.UNAUTHORIZED).entity("Incorrect password").build();
		}
		UserToken token = userService.generateToken(user);
		return Response.accepted(token).build();
    }
}
