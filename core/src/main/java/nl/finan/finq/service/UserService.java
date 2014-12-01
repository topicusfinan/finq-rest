package nl.finan.finq.service;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import nl.finan.finq.converter.UserConverter;
import nl.finan.finq.dao.UserDao;
import nl.finan.finq.dao.UserTokenDao;
import nl.finan.finq.entities.User;
import nl.finan.finq.entities.UserToken;
import nl.finan.finq.to.UserTO;
import org.mindrot.jbcrypt.BCrypt;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.Date;
import java.util.UUID;

@Stateless
public class UserService {

    @EJB
    private UserDao userDao;

    @EJB
    private UserTokenDao userTokenDao;

    public User createUser(UserTO userTO){
        User user = UserConverter.convert(userTO);
        user.setPassword(hashPassword(user.getPassword()));
        userDao.persist(user);
        return user;
    }

    public UserToken generateToken(User user){
        UserToken token = new UserToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setCreationDate(new Date());
        userTokenDao.persist(token);
        return token;
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
