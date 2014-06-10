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

@Entity(name = "Scenario")
@Table(name="JBEHAVE_SCENARIO")
public class Scenario {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "STORY_ID", nullable = false)
	private Story story;
	
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
	
	
}
