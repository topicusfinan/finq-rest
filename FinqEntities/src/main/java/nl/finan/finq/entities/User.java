package nl.finan.finq.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "FINQ_USER", indexes = { @Index(columnList = "email", unique = true) })
@NamedQueries({ @NamedQuery(name = User.USER_SELECT_BY_EMAIL, query = "select u from User u where u.email = :email"), @NamedQuery(name = User.QUERY_BY_TOKEN, query = "select t.user from UserToken t where t.token = :token") })
public class User extends GenericEntity
{

	public static final String USER_SELECT_BY_EMAIL = "User.selectByEmail";
	public static final String QUERY_BY_TOKEN = "User.selectByToken";

	@Column(name = "FIRST_NAME")
	private String firstName;

	@Column(name = "LAST_NAME")
	private String lastName;

	@Column(name = "email", nullable = false, unique = true)
	private String email;

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

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
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
