package nl.finan.jbehave.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name="JBEHAVE_SCENARIO")
public class Scenario extends GenericEntity{

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "STORY_ID", nullable = false)
	@JsonIgnore
	private Story story;
	
	@Column(name = "TITLE")
	private String title;
	

    @ElementCollection()
    @CollectionTable(name="JBEHAVE_STEPS", joinColumns = @JoinColumn(name="SCENARIO_ID"))
    @Column(name = "STEPS")
	private List<String> steps;

    public Scenario() {
        if(steps==null){
            steps = new ArrayList<String>();
        }
    }

    public Story getStory() {
		return story;
	}

	public void setStory(Story story) {
		this.story = story;
	}

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
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
		for(String step: steps){
            builder.append(step);
            builder.append("\n");
        }
		return builder.toString();
	}	
}
