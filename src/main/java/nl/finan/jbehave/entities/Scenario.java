package nl.finan.jbehave.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;


@Entity
@Table(name="JBEHAVE_SCENARIO")
public class Scenario extends GenericEntity{

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "STORY_ID", nullable = false)
	@JsonIgnore
	private Story story;
	
	@Column(name = "TITLE")
	private String title;
	
	@Lob
	@Column(name = "STEPS")
	private String steps;

	public Story getStory() {
		return story;
	}

	public void setStory(Story story) {
		this.story = story;
	}

	public String getSteps() {
		return steps;
	}

	public void setSteps(String steps) {
		this.steps = steps;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String toStory() {
		StringBuilder builder = new StringBuilder();
		builder.append("Scenario: ");
		builder.append(getTitle());
		builder.append("\n");
		builder.append(getSteps());
		builder.append("\n");
		return builder.toString();
	}	
}
