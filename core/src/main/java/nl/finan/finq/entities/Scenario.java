package nl.finan.finq.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "FINQ_SCENARIO")
public class Scenario extends GenericEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "STORY_ID", nullable = false)
    @JsonBackReference
    private Story story;

    @Column(name = "TITLE")
    private String title;

    @OneToMany(mappedBy = "scenario", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<Step> steps;

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

    public List<Step> getSteps() {
        if(steps == null){
            steps = new ArrayList<>();
        }
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
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
