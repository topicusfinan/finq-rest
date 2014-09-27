package nl.finan.finq.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "JBEHAVE_LOG_STORY")
public class StoryLog extends Log {

    @ManyToOne
    @JoinColumn
    private Story story;

    @ManyToOne
    @JoinColumn
    @JsonBackReference
    private RunningStories runningStory;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "storyLog")
    private List<ScenarioLog> scenarioLogs;

    public Story getStory() {
        return story;
    }

    public void setStory(Story story) {
        this.story = story;
    }

    public List<ScenarioLog> getScenarioLogs() {
        return scenarioLogs;
    }

    public void setScenarioLogs(List<ScenarioLog> scenarioLogs) {
        this.scenarioLogs = scenarioLogs;
    }

    public RunningStories getRunningStory() {
        return runningStory;
    }

    public void setRunningStory(RunningStories runningStory) {
        this.runningStory = runningStory;
    }
}
