package nl.finan.finq.dao.impl;

import nl.finan.finq.dao.UserDao;
import nl.finan.finq.entities.User;
import nl.finan.finq.entities.User_;
import org.apache.commons.lang3.StringUtils;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

import static nl.finan.finq.dao.impl.NamedParameter.np;

@Stateless
public class UserDaoImpl extends DaoJPAImpl<User> implements UserDao
{
	@Override
	public List<User> findByName(String firstName, String lastName, Integer page, Integer pageSize)
	{
		return byNameQuery(firstName, lastName).pageSelect(page, pageSize);
	}

	@Override
	public Long countByName(String firstName, String lastName)
	{
		return byNameQuery(firstName, lastName).count();
	}

	private GenericQuery byNameQuery(final String firstName, final String lastName)
	{
		return new GenericQuery()
		{
			@Override
			protected Predicate buildWhere(CriteriaBuilder builder, Root<User> root)
			{
				Predicate where = builder.conjunction();
				if (StringUtils.isNotBlank(firstName))
				{
					where = builder.and(where, builder.equal(root.get(User_.firstName), firstName));
				}
				if (StringUtils.isNotBlank(lastName))
				{
					where = builder.and(where, builder.equal(root.get(User_.lastName), lastName));
				}
				return where;
			}
		};
	}

	@Override
	public User findByEmail(String email)
	{
		return find(User.USER_SELECT_BY_EMAIL, np("email", email));
	}

	@Override
	public User findByToken(String token)
	{
		return find(User.QUERY_BY_TOKEN, np("token", token));
	}
}
