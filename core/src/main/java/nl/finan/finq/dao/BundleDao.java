package nl.finan.finq.dao;

import nl.finan.finq.entities.Book;

import javax.ejb.Local;

@Local
public interface BundleDao extends Dao<Book> {

}
