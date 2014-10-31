package nl.finan.finq.rest.to;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class RunTO {
    @JsonProperty("environment")
    private Long environmentId;
    private List<StoryTO> stories;

    public Long getEnvironmentId() {
        return environmentId;
    }

    public void setEnvironmentId(Long environmentId) {
        this.environmentId = environmentId;
    }

    public List<StoryTO> getStories() {
        return stories;
    }

    public void setStories(List<StoryTO> stories) {
        this.stories = stories;
    }

    @Override
    public String toString() {
        return "RunTO{" +
            "environmentId=" + environmentId +
            ", stories=" + stories +
            '}';
    }
}
