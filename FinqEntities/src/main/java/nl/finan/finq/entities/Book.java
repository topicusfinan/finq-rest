package nl.finan.finq.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "FINQ_BOOK")
public class Book extends GenericEntity
{

	@Column(name = "TITLE")
	private String title;

	@OneToMany(mappedBy = "book", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	private List<Story> stories;

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String name)
	{
		this.title = name;
	}

	public List<Story> getStories()
	{
		if (stories == null)
		{
			stories = new ArrayList<>();
		}
		return stories;
	}

	public void setStories(List<Story> stories)
	{
		this.stories = stories;
	}

}
