package nl.finan.finq.dao.impl;

import nl.finan.finq.dao.UserTokenDao;
import nl.finan.finq.entities.UserToken;

import javax.ejb.Stateless;

import static nl.finan.finq.dao.impl.NamedParameter.np;

@Stateless
public class UserTokenDaoImpl extends DaoJPAImpl<UserToken> implements UserTokenDao
{
	@Override
	public UserToken findByToken(String token)
	{
		return find(UserToken.QUERY_BY_TOKEN, np("token", token));
	}
}
