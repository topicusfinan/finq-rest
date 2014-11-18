package nl.finan.finq.dao.impl;

import nl.finan.finq.dao.UserTokenDao;
import nl.finan.finq.entities.UserToken;

import javax.ejb.Stateless;

@Stateless
public class UserTokenDaoImpl extends DaoJPAImpl<UserToken> implements UserTokenDao {
}
