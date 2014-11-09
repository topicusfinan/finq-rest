package nl.finan.finq.embeder;


import nl.finan.finq.entities.Story;

import java.io.Serializable;
import java.util.List;

public class RunMessage implements Serializable{

    private List<Story> stories;
    private Long runId;

    public RunMessage(List<Story> stories, Long runId) {
        this.stories = stories;
        this.runId = runId;
    }

    public List<Story> getStories() {
        return stories;
    }

    public Long getRunId() {
        return runId;
    }
}
