package nl.finan.finq.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "FINQ_USER_TOKEN")
@NamedQueries({ @NamedQuery(name = UserToken.QUERY_BY_TOKEN, query = "select t from UserToken t where t.token = :token") })
public class UserToken extends GenericEntity
{

	public static final String QUERY_BY_TOKEN = "UserToken.selectByToken";

	@ManyToOne
	@JoinColumn(name = "USER_ID", nullable = false)
	private User user;

	@Column(name = "TOKEN", nullable = false, unique = true)
	private String token;

	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

	public String getToken()
	{
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}

	public Date getCreationDate()
	{
		return creationDate;
	}

	public void setCreationDate(Date creationDate)
	{
		this.creationDate = creationDate;
	}
}
