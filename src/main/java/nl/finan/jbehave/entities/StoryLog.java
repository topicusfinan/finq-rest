package nl.finan.jbehave.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name="JBEHAVE_LOG_STORY")
public class StoryLog extends Log{

    @ManyToOne
    private Story story;

    @OneToMany
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
}
