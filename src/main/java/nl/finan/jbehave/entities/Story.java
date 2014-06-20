package nl.finan.jbehave.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "JBEHAVE_STORY")
public class Story extends GenericEntity{

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "PROJECT_ID", nullable = false)
	@JsonIgnore
	private Project project;
	
	@Column(name = "NAME")
	private String name;

	@OneToMany(mappedBy = "story",fetch = FetchType.EAGER)
	private List<Scenario> scenarios;

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Scenario> getScenarios() {
		return scenarios;
	}

	public void setScenarios(List<Scenario> scenarios) {
		this.scenarios = scenarios;
	}
	
	public String toStory(){
		StringBuilder builder = new StringBuilder();
		for(Scenario scenario: getScenarios()){
			builder.append(scenario.toStory());
		}
		
		return builder.toString();
	}
}
