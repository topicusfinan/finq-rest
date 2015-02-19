package nl.finan.finq.dao.impl;

import nl.finan.finq.dao.UserDao;
import nl.finan.finq.entities.User;
import nl.finan.finq.entities.User_;
import org.apache.commons.lang3.StringUtils;

import javax.ejb.Stateless;
import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static nl.finan.finq.dao.impl.NamedParameter.np;

@Stateless
public class UserDaoImpl extends DaoJPAImpl<User> implements UserDao {
    @Override
    public List<User> findByName(String firstName, String lastName, Integer page, Integer pageSize) {
        List<User> list =  new GenericQuery().pageSelect(byNameQuery(firstName, lastName), page, pageSize);
        list.parallelStream()
    }

    @Override
    public Long countByName(String firstName, String lastName) {
        WhereBuilder<User> where = (builder,root) -> builder.conjunction();

        return new GenericQuery().count(byNameQuery(firstName, lastName));
    }

    private WhereBuilder<User> byNameQuery(final String firstName, final String lastName) {
        return (builder, root) -> {
            Predicate where = builder.conjunction();
            if (StringUtils.isNotBlank(firstName)) {
                where = builder.and(where, builder.equal(root.get(User_.firstName), firstName));
            }
            if (StringUtils.isNotBlank(lastName)) {
                where = builder.and(where, builder.equal(root.get(User_.lastName), lastName));
            }
            return where;
        };
    }

    @Override
    public User findByEmail(String email) {
        return find(User.USER_SELECT_BY_EMAIL, np("email", email));
    }

    @Override
    public User findByToken(String token) {
        return find(User.QUERY_BY_TOKEN, np("token", token));
    }
}
