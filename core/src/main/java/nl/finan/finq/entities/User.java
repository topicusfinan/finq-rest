package nl.finan.finq.entities;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "FINQ_USER", indexes = { @Index(columnList = "username", unique = true) })
@NamedQueries({ @NamedQuery(name = User.QUERY_BY_USER_NAME, query = "select u from User u where u.username = :username"), @NamedQuery(name = User.QUERY_BY_TOKEN, query = "select t.user from UserToken t where t.token = :token") })
public class User extends GenericEntity
{

	public static final String QUERY_BY_USER_NAME = "User.selectByUserName";
	public static final String QUERY_BY_TOKEN = "User.selectByToken";

	@Column(name = "FIRST_NAME")
	private String firstName;

	@Column(name = "LAST_NAME")
	private String lastName;

	@Column(name = "username", nullable = false, unique = true)
	private String username;

	@Column(name = "password", nullable = false)
	@JsonIgnore
	private String password;

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	@JsonProperty
	@Transient
	public String getName()
	{
		return firstName + " " + lastName;
	}
}
