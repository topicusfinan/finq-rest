package nl.finan.finq.rest;

import nl.finan.finq.dao.BookDao;
import nl.finan.finq.entities.Book;
import nl.finan.finq.service.BookService;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path(PathConstants.BOOKS)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
public class BookResources
{

	@EJB
	private BookDao bookDao;

	@EJB
	private BookService bookService;

	@GET
	public List<Book> getBundles()
	{
		List<Book> all = bookDao.listAll();

		return all;
	}

	@POST
	public Response saveBook(Book book)
	{
		book = bookService.updateOrCreateEntity(book);
		if (book == null)
		{
			return Response.status(Response.Status.NOT_ACCEPTABLE).build();
		}
		return Response.created(URI.create(PathConstants.BOOKS + "/" + book.getId())).build();
	}

	@GET
	@Path("{id}")
	public Book getBook(@PathParam("id") Long id)
	{
		return bookDao.find(id);
	}
}
