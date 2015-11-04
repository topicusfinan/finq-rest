package nl.finan.finq.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "FINQ_TAG")
public class Tag extends GenericEntity
{

	@Column(name = "aKEY")
	@JsonProperty("key")
	private String aKey;

	@Column(name = "aVALUE")
	@JsonProperty("value")
	private String aValue;

	@ManyToMany
	@JoinTable(
			name = "FINQ_STORY_TAG",
			joinColumns = { @JoinColumn(name = "TAG_ID", referencedColumnName = "ID") },
			inverseJoinColumns = { @JoinColumn(name = "STORY_ID", referencedColumnName = "ID") })
	@JsonBackReference
	private List<Story> stories = new ArrayList<>();

	public String getaKey()
	{
		return aKey;
	}

	public void setaKey(String aKey)
	{
		this.aKey = aKey;
	}

	public String getaValue()
	{
		return aValue;
	}

	public void setaValue(String aValue)
	{
		this.aValue = aValue;
	}

	public List<Story> getStories()
	{
		return stories;
	}

	public void setStories(List<Story> stories)
	{
		this.stories = stories;
	}
}
