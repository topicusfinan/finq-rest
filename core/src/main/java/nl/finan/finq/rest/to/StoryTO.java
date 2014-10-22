package nl.finan.finq.rest.to;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class StoryTO {
    private Long id;
    @JsonProperty("scenarios")
    private List<Long> scenarioIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Long> getScenarioIds() {
        return scenarioIds;
    }

    public void setScenarioIds(List<Long> scenarioIds) {
        this.scenarioIds = scenarioIds;
    }

    @Override
    public String toString() {
        return "StoryTO{" +
            "id=" + id +
            ", scenarioIds=" + scenarioIds +
            '}';
    }
}
