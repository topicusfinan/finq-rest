package nl.finan.finq.dao;

import java.util.List;

import javax.ejb.Local;

import nl.finan.finq.entities.User;

@Local
public interface UserDao extends Dao<User>
{

	List<User> findByName(String firstName, String lastName, Integer page, Integer pageSize);

	Long countByName(String firstName, String lastName);

	User findByEmail(String email);

	User findByToken(String token);
}
