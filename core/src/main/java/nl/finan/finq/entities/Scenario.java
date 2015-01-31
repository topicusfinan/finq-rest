package nl.finan.finq.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "FINQ_SCENARIO")
public class Scenario extends StepContainer {

    @ManyToOne(optional = false)
    @JoinColumn(name = "STORY_ID", nullable = false)
    @JsonBackReference
    private Story story;

    @Column(name = "TITLE")
    private String title;


    public Story getStory() {
        return story;
    }

    public void setStory(Story story) {
        this.story = story;
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
        for (Step step : getSteps()) {
            builder.append(System.lineSeparator());
            builder.append(step.toStory());
        }
        return builder.toString();
    }
}
