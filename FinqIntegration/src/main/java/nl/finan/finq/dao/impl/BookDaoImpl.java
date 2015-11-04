package nl.finan.finq.dao.impl;

import nl.finan.finq.dao.BookDao;
import nl.finan.finq.entities.Book;

import javax.ejb.Stateless;

@Stateless
public class BookDaoImpl extends DaoJPAImpl<Book> implements BookDao
{

}
