package nl.finan.finq.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "FINQ_PROLOGUE")
public class Prologue extends StepContainer {

    @OneToOne
    @JoinColumn(name = "STORY_ID", nullable = false)
    @JsonBackReference
    private Story story;

    public Story getStory() {
        return story;
    }

    public void setStory(Story story) {
        this.story = story;
    }

    public String toStory() {
        StringBuilder builder = new StringBuilder();
        builder.append("Prologue:");
        for (Step step : getSteps()) {
            builder.append(System.lineSeparator());
            builder.append(step.toStory());
        }
        return builder.toString();
    }
}
