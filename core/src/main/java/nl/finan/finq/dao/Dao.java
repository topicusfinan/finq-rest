package nl.finan.finq.dao;

import nl.finan.finq.dao.impl.NamedParameter;
import nl.finan.finq.entities.GenericEntity;
import nl.finan.finq.entities.RunningStories;

import javax.ejb.Local;
import java.io.Serializable;
import java.util.List;

/**
 * A generic DAO interface.
 *
 * @param <T> The type of persistent object that will be inserted / updated / retrieved.
 */
@Local
public interface Dao<T extends GenericEntity> {
    /**
     * Return a single persistent object that matches with query created by name with named parameters set.
     *
     * @param query      The name of the query to be executed.
     * @param parameters The parameters to be filled on query.
     * @return An instance of a persistent object that matches on query or <code>null</code> if no object is found.
     */
    T find(String query, NamedParameter... parameters);

    /**
     * Return a single persistence object that matches with the ID parameter.
     *
     * @param id The entity ID value.
     * @return An instance of persistence entity that matches id or <code>null</code> if no one is found.
     */
    T find(Serializable id);

    /**
     * Return a list of persistent objects that matches in query with received name and parameters.
     *
     * @param query      The name of the query to be executed.
     * @param parameters The parameters to be filled on query.
     * @return A list of persistent objects or an empty list.
     */
    List<T> list(String query, NamedParameter... parameters);

    /**
     * Return a list of persistent objects that matches in query with received name, parameters and pagination.
     *
     * @param query       The name of the query to be executed.
     * @param firstResult set the position of first result for retrieving the data
     * @param maxResult   set max result size
     * @param parameters  The parameters to be filled on query.
     * @return A list of persistent objects or an empty list.
     */
    List<T> list(String query, int firstResult, int maxResult, NamedParameter... parameters);

    /**
     * List all persistent objects found under database.<br>
     *
     * @return A list of all found objects.
     */
    List<T> listAll();

    /**
     * Forces the <code>instance</code> to be persisted (synchronized) on database, checking for unique constraints or
     * other possible database's errors.
     *
     * @param instance The instance to be persisted.
     */
    void persist(T instance);

    /**
     * Refreshes the current instance with the database values.
     *
     * @param instance The object instance to be refreshed.
     */
    void refresh(T instance);

    /**
     * Removes from database the given instance object.
     *
     * @param instance the instance to be removed.
     */
    void delete(T instance);

    T getReference(Long id);

    List<T> listAll(Integer page, Integer size);
}
