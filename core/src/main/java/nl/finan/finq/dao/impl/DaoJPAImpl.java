package nl.finan.finq.dao.impl;

import nl.finan.finq.dao.Dao;
import nl.finan.finq.entities.GenericEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public abstract class DaoJPAImpl<T extends GenericEntity> implements Dao<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DaoJPAImpl.class);
    private static final String QUERY_LISTALL = "SELECT o FROM %s o";
    private static final String QUERY_COUNTALL = "select count(o) from %s o";
    private final Class<T> persistentClass;
    @PersistenceContext
    private EntityManager em;

    @SuppressWarnings("unchecked")
    public DaoJPAImpl() {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public Class<T> getPersistentClass() {
        return persistentClass;
    }

    public void persist(T instance) {
        try {
            em.persist(instance);
        } catch (PersistenceException e) {
            LOGGER.error("It was not possible to persist Entity Class: " + getPersistentClass(), e);
            throw e;
        }
    }

    public void refresh(T instance) {
        em.refresh(instance);
    }

    public void delete(T instance) {
        try {
            em.remove(instance);
        } catch (PersistenceException e) {
            LOGGER.error("It was not possible to remove Entity Class/ID: " + getPersistentClass() + " / " + instance.getId(), e);
            throw e;
        }
    }

    public T find(Serializable id) {
        LOGGER.debug(">>> executing query (find by id) {}", getPersistentClass());
        return em.find(getPersistentClass(), id);
    }

    @SuppressWarnings("unchecked")
    public T find(String namedQuery, NamedParameter... parameters) {
        LOGGER.debug(">>> executing query (find) {}", namedQuery);
        T retrievedEntity = null;

        try {
            retrievedEntity = (T) createNamedQuery(namedQuery, parameters).getSingleResult();

        } catch (NoResultException e) {
            LOGGER.debug("It was not possible to find any {} with SQL parameter(s): {}", getPersistentClass(), parameters);
        } catch (NonUniqueResultException e) {
            LOGGER.error("More than one result was found for " + getPersistentClass() + " with SQL parameter(s): " + parameters, e);
            throw e;
        }

        return retrievedEntity;
    }

    public List<T> list(String query, NamedParameter... parameters) {
        LOGGER.debug(">>> executing query (list) {}", query);

        @SuppressWarnings("unchecked")
        List<T> retrievedList = createNamedQuery(query, parameters).getResultList();

        return retrievedList == null ? new ArrayList<T>() : retrievedList;
    }

    public Long count(String query, NamedParameter... parameters) {
        LOGGER.debug(">>> executing query (count) {}", query);

        Long count = (Long) createNamedQuery(query, parameters).getSingleResult();

        return count == null ? Long.valueOf(0L) : count;
    }


    public List<T> list(String query, int page, int size, NamedParameter... parameters) {
        LOGGER.debug(">>> executing query (list, paginated) {}", query);

        @SuppressWarnings("unchecked")
        List<T> retrievedList = createNamedQuery(query, parameters).setFirstResult(page * size).setMaxResults(size).getResultList();

        return retrievedList == null ? new ArrayList<T>() : retrievedList;
    }


    public List<T> listAll() {
        return listAll(null, null);
    }

    @Override
    public List<T> listAll(Integer page, Integer size) {
        Query query = em.createQuery(String.format(QUERY_LISTALL, getPersistentClass().getSimpleName()), getPersistentClass());

        if (page != null && size != null) {
            query.setFirstResult(page * size);
            query.setMaxResults(size);
        }

        @SuppressWarnings("unchecked")
        List<T> resultList = query.getResultList();

        return resultList;
    }

    @Override
    public Long countAll() {
        Query query = em.createQuery(String.format(QUERY_COUNTALL, getPersistentClass().getSimpleName()), Long.class);

        @SuppressWarnings("unchecked")
        Long result = (Long) query.getSingleResult();

        return result;
    }

    /**
     * Utility method to create an instance of JPA query and set named parameters.
     *
     * @param query      The query name to be created from JPA context
     * @param parameters The parameters to be set on query
     * @return JPA query instance for <code>query</code> named query
     */
    private Query createNamedQuery(String query, NamedParameter... parameters) {
        Query q = em.createNamedQuery(query);

        for (NamedParameter np : parameters) {
            q.setParameter(np.getName(), np.getValue());
        }

        return q;
    }

    public T getReference(Long id) {
        return em.getReference(getPersistentClass(), id);
    }

}
