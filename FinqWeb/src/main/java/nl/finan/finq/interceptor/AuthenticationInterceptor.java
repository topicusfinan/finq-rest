package nl.finan.finq.interceptor;

import java.io.IOException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import nl.finan.finq.annotation.Authorized;
import nl.finan.finq.dao.UserDao;
import nl.finan.finq.entities.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Authorized
@Provider
public class AuthenticationInterceptor implements ContainerRequestFilter
{

	public static final ThreadLocal<User> USER_THREAD_LOCAL = new ThreadLocal<User>()
	{
		@Override
		protected User initialValue()
		{
			return null;
		}
	};
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationInterceptor.class);
	private UserDao userDao;

	public AuthenticationInterceptor() throws NamingException
	{

		userDao = (UserDao) new InitialContext().lookup("java:module/UserDaoImpl");
	}

	@Override
	public void filter(ContainerRequestContext containerRequestContext) throws IOException
	{
		if (containerRequestContext.getHeaders().containsKey("x-api-key"))
		{
			String token = containerRequestContext.getHeaders().getFirst("x-api-key");
			User user = userDao.findByToken(token);
			if (user != null)
			{
				USER_THREAD_LOCAL.set(user);
				LOGGER.debug("User [{}] successfully authorized by token [{}]", user, token);
			}
			else
			{
				containerRequestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
				LOGGER.warn("User unsuccessfully authorized by token [{}]", token);
			}
		}
		else
		{
			containerRequestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());

			LOGGER.warn("User unsuccessfully authorized without token");
		}
	}
}
