package nl.finan.finq.dao;


import nl.finan.finq.entities.User;

import javax.ejb.Local;
import java.util.List;

@Local
public interface UserDao extends Dao<User>{

    List<User> findByName(String firstName, String lastName, Integer page, Integer pageSize);

    Long countByName(String firstName, String lastName);

}
