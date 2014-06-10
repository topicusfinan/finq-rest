package nl.finan.jbehave.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name="JBEHAVE_SCENARIO")
public class Scenario {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "STORY_ID", nullable = false)
	@JsonIgnore
	private Story story;
	
	@Column(name = "TITLE")
	private String title;
	
	@Lob
	@Column(name = "STEPS")
	private String steps;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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
		builder.append("\\n");
		builder.append(getSteps());
		builder.append("\\n");
		return builder.toString();
	}	
}
