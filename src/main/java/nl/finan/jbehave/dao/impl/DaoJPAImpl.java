package nl.finan.jbehave.dao.impl;

import nl.finan.jbehave.dao.Dao;
import nl.finan.jbehave.entities.GenericEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public abstract class DaoJPAImpl<T extends GenericEntity> implements Dao<T>
{
	private static final Logger LOGGER = LoggerFactory.getLogger(DaoJPAImpl.class);

	@PersistenceContext
	private EntityManager em;

	private final Class<T> persistentClass;

	private static final String QUERY_LISTALL = "SELECT o FROM %s o";

	@SuppressWarnings("unchecked")
	public DaoJPAImpl()
	{
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public Class<T> getPersistentClass()
	{
		return persistentClass;
	}

	public void persist(T instance)
	{
		try
		{
			em.persist(instance);
			// 2012-12-04: RK: Don't do this, see http://www.developerfusion.com/article/84945/flush-and-clear-or-mapping-antipatterns/ why this is generally a bad idea.
			// em.flush(); // forces check for unique constraints
		}
		catch (PersistenceException e)
		{
			// transactions is marked for rollback
			LOGGER.error("It was not possible to persist Entity Class: " + getPersistentClass(), e);
			throw e;
		}
	}

	public void flush()
	{
		em.flush();
	}

	public T merge(T instance)
	{
		T merged = null;

		try
		{
			merged = em.merge(instance);
		}
		catch (PersistenceException e)
		{
			LOGGER.error("It was not possible to merge Entity Class: " + getPersistentClass(), e);
			throw e;
		}

		return merged;
	}

	public void refresh(T instance)
	{
		em.refresh(instance);
	}

	public void delete(T instance)
	{
		Long retrievedId = retrieveEntityId(instance);

		try
		{
			em.remove(instance);
			em.flush(); // force database update
		}
		catch (PersistenceException e)
		{
			LOGGER.error("It was not possible to remove Entity Class/ID: " + getPersistentClass() + " / " + retrievedId, e);
			throw e;
		}
	}

	public int executeUpdateNamedQuery(String updateNamedQuery, NamedParameter... parameters)
	{
		int updated = createNamedQuery(updateNamedQuery, parameters).executeUpdate();
		return updated;
	}

	public Integer executeUpdateQuery(String updateQuery, NamedParameter... parameters)
	{
		int updated = createQuery(updateQuery, parameters).executeUpdate();
		return updated;
	}

	/**
	 * To avoid language translation overhead, use executeNativeUpdate.
	 */
	protected Integer executeNativeUpdate(String updateQuery, NamedParameter... parameters)
	{
		Query q = em.createNativeQuery(updateQuery);

		for (NamedParameter np : parameters)
		{
			q.setParameter(np.getName(), np.getValue());
		}

		int updated = q.executeUpdate();

		return updated;
	}

	public T find(Serializable id)
	{
		LOGGER.debug(">>> executing query (find by id) {}", getPersistentClass());
		return em.find(getPersistentClass(), id);
	}

	@SuppressWarnings("unchecked")
	public T find(String namedQuery, NamedParameter... parameters)
	{
		LOGGER.debug(">>> executing query (find) {}", namedQuery);
		T retrieviedEntity = null;

		try
		{
			retrieviedEntity = (T) createNamedQuery(namedQuery, parameters).getSingleResult();

		}
		catch (NoResultException e)
		{
			LOGGER.debug("It was not possible to find any {} with SQL parameter(s): {}", getPersistentClass(), parameters);
		}
		catch (NonUniqueResultException e)
		{
			LOGGER.error("More than one result was found " + e.getStackTrace() + " for {} with SQL parameter(s): {}", getPersistentClass(), parameters);
			throw e;
		}

		return retrieviedEntity;
	}

	protected <A extends Serializable> A findAttribute(String namedQuery, Class<A> attributeType, NamedParameter... parameters)
	{
		Serializable retrievedEntity = null;

		try
		{
			retrievedEntity = (Serializable) createNamedQuery(namedQuery, parameters).getSingleResult();
		}
		catch (NoResultException e)
		{
			LOGGER.debug("It was not possible to find any {} with SQL parameter(s): {}", getPersistentClass(), parameters);
		}
		catch (NonUniqueResultException e)
		{
			LOGGER.warn("More than one result was found for {} with SQL parameter(s): {}", getPersistentClass(), parameters);
		}

		@SuppressWarnings("unchecked")
		A castedEntity = (A) retrievedEntity;
		return castedEntity;
	}

	public List<T> list(String query, NamedParameter... parameters)
	{
		LOGGER.debug(">>> executing query (list) {}", query);

		@SuppressWarnings("unchecked")
		List<T> retrievedList = createNamedQuery(query, parameters).getResultList();

		return retrievedList == null ? new ArrayList<T>() : retrievedList;
	}

	public Long count(String query, NamedParameter... parameters)
	{
		LOGGER.debug(">>> executing query (count) {}", query);

		Long count = (Long) createNamedQuery(query, parameters).getSingleResult();

		return count == null ? Long.valueOf(0L) : count;
	}

	public <FT extends Serializable> List<FT> listField(String query, NamedParameter... parameters)
	{
		List<FT> result = createNamedQuery(query, parameters).getResultList();

		return result;
	}

	public <FT extends Serializable> FT findField(String query, NamedParameter... parameters)
	{
		FT result = (FT) createNamedQuery(query, parameters).getSingleResult();

		return result;
	}

	public List<T> list(String query, int firstResult, int maxResult, NamedParameter... parameters)
	{
		LOGGER.debug(">>> executing query (list, paginated) {}", query);

		@SuppressWarnings("unchecked")
		List<T> retrievedList = createNamedQuery(query, parameters).setFirstResult(firstResult).setMaxResults(maxResult).getResultList();

		return retrievedList == null ? new ArrayList<T>() : retrievedList;
	}

	/**
	 * Return a list of persistent objects that matches in query with received name and parameters.
	 * 
	 * @return A list of persistent objects or an empty list.
	 */
	protected List<T> listByHQLQuery(String hqlQuery)
	{
		LOGGER.debug(">>> executing query (list by HQL) {}", hqlQuery);
		@SuppressWarnings("unchecked")
		List<T> retrievedList = em.createQuery(hqlQuery).getResultList();

		return retrievedList == null ? new ArrayList<T>() : retrievedList;
	}

	@SuppressWarnings("unchecked")
	public <X> List<X> listByQuery(String query, NamedParameter... parameters)
	{
		List<X> retrievedList = createNamedQuery(query, parameters).getResultList();

		return retrievedList == null ? new ArrayList<X>() : retrievedList;
	}

	@SuppressWarnings("unchecked")
	public <X> List<X> listByQuery(String query, int firstResult, int maxResult, NamedParameter... parameters)
	{
		LOGGER.debug(">>> executing query (list, paginated 2) {}", query);
		List<X> retrievedList = createNamedQuery(query, parameters).setFirstResult(firstResult).setMaxResults(maxResult).getResultList();

		return retrievedList == null ? new ArrayList<X>() : retrievedList;
	}

	public List<T> listAll()
	{
		return listAll(false, null);
	}

	public List<T> listAll(Boolean cacheable, String cacheName)
	{
		Query query = em.createQuery(String.format(QUERY_LISTALL, getPersistentClass().getSimpleName()));

		if (cacheable)
		{
			query.setHint("org.hibernate.cacheable", true);
			query.setHint("org.hibernate.cacheRegion", cacheName);
		}

		@SuppressWarnings("unchecked")
		List<T> retrievedList = query.getResultList();

		return retrievedList == null ? new ArrayList<T>() : retrievedList;
	}

	/**
	 * Utility method to create an instance of JPA query and set named parameters.
	 * 
	 * @param query
	 *            The query name to be created from JPA context
	 * @param parameters
	 *            The parameters to be set on query
	 * @return JPA query instance for <code>query</code> named query
	 */
	private Query createNamedQuery(String query, NamedParameter... parameters)
	{
		Query q = em.createNamedQuery(query);

		for (NamedParameter np : parameters)
		{
			q.setParameter(np.getName(), np.getValue());
		}

		return q;
	}

	/**
	 * Utility method to create an instance of JPA query and set named parameters.
	 * 
	 * @param query
	 *            The query name to be created from JPA context
	 * @param parameters
	 *            The parameters to be set on query
	 * @return JPA query instance for <code>query</code> named query
	 */
	protected Query createQuery(String query, NamedParameter... parameters)
	{
		Query q = em.createQuery(query);

		for (NamedParameter np : parameters)
		{
			q.setParameter(np.getName(), np.getValue());
		}

		return q;
	}

	/**
	 * Utility method to create an instance of native query and set named parameters.<br />
	 * <strong>Please don't use this method when you don't have to! HQL queries are always preferred over native queries!</strong>
	 * 
	 * @param query
	 *            The SQL string to be created
	 * @param parameters
	 *            The parameters to be set on query
	 * @return JPA query instance for <code>sql</code> named query
	 */
	protected Query createNativeQuery(String sql, NamedParameter... parameters)
	{
		Query q = em.createNativeQuery(sql);

		for (NamedParameter np : parameters)
		{
			q.setParameter(np.getName(), np.getValue());
		}

		return q;
	}

	/**
	 * Retrieves by reflection the Entity ID.
	 * 
	 * @return The retrieved entity Id from current object instance or <code>null</code> if it was not possible.
	 */
	private Long retrieveEntityId(T instance)
	{
		Long retrievedId = null;

		try
		{
			@SuppressWarnings("rawtypes")
			Class clazz = Class.forName(getPersistentClass().getName());

			Method m = clazz.getMethod("getId");
			retrievedId = (Long) m.invoke(instance);
		}
		catch (Exception e)
		{
			// null, no method getId, or other thing else will be caught...
			LOGGER.debug("It was not possible to retrieve the ID from entity {}", getPersistentClass());
		}

		return retrievedId;
	}

	public T getReference(Long id)
	{
		return em.getReference(getPersistentClass(), id);
	}

	public BigDecimal returnValueOrZeroOnIfNull(BigDecimal retVal)
	{
		if (null == retVal)
		{
			return BigDecimal.ZERO;
		}
		else
		{
			return retVal;
		}
	}
}