package nl.finan.jbehave.dao;

import nl.finan.jbehave.entities.GenericEntity;

import java.io.Serializable;
import java.util.List;

/**
 * A generic DAO interface.
 * 
 * @param <T>
 *            The type of persistent object that will be inserted / updated / retrieved.
 */
public interface Dao<T extends GenericEntity>
{
	/**
	 * Return a single persistent object that matches with query created by name with named parameters set.
	 * 
	 * 
	 * @param query
	 *            The name of the query to be executed.
	 * @param parameters
	 *            The parameters to be filled on query.
	 * @return An instance of a persistent object that matches on query or <code>null</code> if no object is found.
	 */
	T find(String query, NamedParameter... parameters);

	/**
	 * Return a single persistence object that matches with the ID parameter.
	 * 
	 * @param id
	 *            The entity ID value.
	 * @return An instance of persistence entity that matches id or <code>null</code> if no one is found.
	 */
	T find(Serializable id);

	/**
	 * Return a list of persistent objects that matches in query with received name and parameters.
	 * 
	 * @param query
	 *            The name of the query to be executed.
	 * @param parameters
	 *            The parameters to be filled on query.
	 * @return A list of persistent objects or an empty list.
	 */
	List<T> list(String query, NamedParameter... parameters);

	/**
	 * Return a list of persistent objects that matches in query with received name, parameters and pagination.
	 * 
	 * @param query
	 *            The name of the query to be executed.
	 * @param firstResult
	 *            set the position of first result for retrieving the data
	 * @param maxResult
	 *            set max result size
	 * @param parameters
	 *            The parameters to be filled on query.
	 * @return A list of persistent objects or an empty list.
	 */
	List<T> list(String query, int firstResult, int maxResult, NamedParameter... parameters);

	/**
	 * Return a list of persistent objects that matches in query with received name and parameters.<br />
	 * This is about the same method as {@link #list(String, NamedParameter...)}, but with a different return type (so it allows you to query on other objects than this DAO is implemented for).
	 * 
	 * @param query
	 *            The name of the query to be executed.
	 * @param parameters
	 *            The parameters to be filled on query.
	 * @return A list of persistent objects or an empty list.
	 * @see #list(String, NamedParameter...)
	 */
	<X> List<X> listByQuery(String query, NamedParameter... parameters);

	/**
	 * Return a list of persistent objects that matches in query with received name, parameters and pagination.<br />
	 * This is about the same method as {@link #list(String, int, int, NamedParameter...)}, but with a different return type (so it allows you to query on other objects than this DAO is implemented for).
	 * 
	 * @param query
	 *            The name of the query to be executed.
	 * @param firstResult
	 *            set the position of first result for retrieving the data
	 * @param maxResult
	 *            set max result size
	 * @param parameters
	 *            The parameters to be filled on query.
	 * @return A list of persistent objects or an empty list.
	 * @see #list(String, int, int, NamedParameter...)
	 */
	<X> List<X> listByQuery(String query, int firstResult, int maxResult, NamedParameter... parameters);

	/**
	 * List all persistent objects found under database.<br>
	 * 
	 * @return A list of all found objects.
	 */
	List<T> listAll();

	/**
	 * List all persistent objects found under database.<br>
	 * 
	 * @return A list of all found objects.
	 */
	List<T> listAll(Boolean cacheable, String cacheName);

	/**
	 * Forces the <code>instance</code> to be persisted (synchronized) on database, checking for unique constraints or
	 * other possible database's errors.
	 * 
	 * @param instance
	 *            The instance to be persisted.
	 */
	void persist(T instance);

	void flush();

	/**
	 * Merge the state of given entity into the current persistence context.
	 * 
	 * @param instance
	 *            the instance to be merged.
	 * @return the merged object
	 */
	T merge(T instance);

	/**
	 * Refreshes the current instance with the database values.
	 * 
	 * @param instance
	 *            The object instance to be refreshed.
	 */
	void refresh(T instance);

	/**
	 * Removes from database the given instance object.
	 * 
	 * @param instance
	 *            the instance to be removed.
	 */
	void delete(T instance);

	/**
	 * Executes the updateQuery under database.
	 * 
	 * @param updateQuery
	 *            The name of the update query to be executed.
	 * @param parameters
	 *            The parameters to be filled on query.
	 * @return The total number of registers changed.
	 */
	int executeUpdateNamedQuery(String updateQuery, NamedParameter... parameters);

	T getReference(Long id);
}
