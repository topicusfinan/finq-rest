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

import nl.finan.finq.annotation.Authorized;
import nl.finan.finq.dao.UserDao;
import nl.finan.finq.entities.User;
import nl.finan.finq.entities.UserToken;
import nl.finan.finq.interceptor.AuthenticationInterceptor;
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
    public User getUser(@PathParam("id") Long id){
        User user = userDao.find(id);
        if(user == null){
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return user;
	}

	@POST
	@Path("login")
	public UserToken login(LoginTO loginTO)
	{
		User user = userDao.findByEmail(loginTO.getEmail());
		if (user == null)
		{
			throw new WebApplicationException("User not found",Response.Status.UNAUTHORIZED);
		}
		if (!BCrypt.checkpw(loginTO.getPassword(), user.getPassword()))
		{
            throw new WebApplicationException("Incorrect password",Response.Status.UNAUTHORIZED);
		}
		UserToken token = userService.generateToken(user);
		return token;
    }

    @GET
    @Path("current")
    @Authorized
    public User getCurrentUser(){
        return AuthenticationInterceptor.USER_THREAD_LOCAL.get();
    }
}
