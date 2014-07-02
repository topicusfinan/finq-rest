package nl.finan.jbehave.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "JBEHAVE_BUNDLE")
public class Bundle extends GenericEntity{

	@Column(name = "NAME")
	private String name;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@OneToMany(mappedBy = "bundle",fetch=FetchType.LAZY)
	private List<Story> stories;
	
	@ManyToMany(mappedBy="bundles")
	private List<Collection> collections;

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

	public List<Collection> getCollections() {
		return collections;
	}

	public void setCollections(List<Collection> collections) {
		this.collections = collections;
	}
}
