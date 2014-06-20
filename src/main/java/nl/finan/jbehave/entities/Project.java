package nl.finan.jbehave.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "JBEHAVE_PROJECT")
public class Project extends GenericEntity{

	@Column(name = "NAME")
	private String name;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@OneToMany(mappedBy = "project",fetch=FetchType.LAZY)
	private List<Story> stories;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Story> getStories() {
		return stories;
	}

	public void setStories(List<Story> stories) {
		this.stories = stories;
	}
}
