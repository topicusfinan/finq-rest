package nl.finan.finq.service;

import nl.finan.finq.dao.BookDao;
import nl.finan.finq.entities.Book;

import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class BookService
{

	@EJB
	private BookDao bookDao;

	public Book updateOrCreateEntity(Book book)
	{
		if (book == null)
		{
			return null;
		}

		if (book.getId() == null)
		{
			bookDao.persist(book);
			return book;
		}

		Book entityBook = bookDao.find(book.getId());
		if (!entityBook.getTitle().equals(book.getTitle()))
		{
			entityBook.setTitle(book.getTitle());
		}
		return entityBook;
	}
}
