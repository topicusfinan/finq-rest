package nl.finan.finq.rest;

import nl.finan.finq.dao.BookDao;
import nl.finan.finq.entities.Book;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path("books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
public class BookResources {

    @EJB
    private BookDao bookDao;

    @GET
    public List<Book> getBundles() {
        List<Book> all = bookDao.listAll();

        return all;
    }

    @POST
    public Response saveBook(Book book){
        bookDao.persist(book);
        return Response.created(URI.create("books/"+book.getId())).build();
    }

    @GET
    @Path("{id}")
    public Book getBook(@PathParam("id") Long id){
        return bookDao.find(id);
    }
}
