package nl.finan.finq.websocket.to;

import nl.finan.finq.entities.RunningStories;
import nl.finan.finq.entities.ScenarioLog;
import nl.finan.finq.entities.StepLog;
import nl.finan.finq.entities.StoryLog;

import java.util.ArrayList;
import java.util.List;

public class GistEvent {

    private Long id;
    private int status;
    private List<StoryTO> stories = new ArrayList<StoryTO>();

    public GistEvent(RunningStories runningStories) {
        this.id = runningStories.getId();
        this.status = runningStories.getStatus().ordinal();
        for (StoryLog storyLog : runningStories.getLogs()) {
            stories.add(new StoryTO(storyLog));
        }
    }

    public Long getId() {
        return id;
    }

    public int getStatus() {
        return status;
    }

    public List<StoryTO> getStories() {
        return stories;
    }

    private class StoryTO {

        private Long id;
        private int status;
        private List<ScenarioTO> scenarios = new ArrayList<>();

        public StoryTO(StoryLog storyLog) {
            this.id = storyLog.getStory().getId();
            this.status = storyLog.getStatus().ordinal();
            for (ScenarioLog scenarioLog : storyLog.getScenarioLogs()) {
                scenarios.add(new ScenarioTO(scenarioLog));
            }
        }

        public Long getId() {
            return id;
        }

        public int getStatus() {
            return status;
        }

        public List<ScenarioTO> getScenarios() {
            return scenarios;
        }
    }

    private class ScenarioTO {
        private Long id;
        private int status;
        private List<StepTO> steps = new ArrayList<>();

        public ScenarioTO(ScenarioLog scenarioLog) {
            this.id = scenarioLog.getScenario().getId();
            this.status = scenarioLog.getStatus().ordinal();
            for (StepLog stepLog : scenarioLog.getStepLogs()) {
                steps.add(new StepTO(stepLog));
            }
        }

        public Long getId() {
            return id;
        }

        public int getStatus() {
            return status;
        }

        public List<StepTO> getSteps() {
            return steps;
        }
    }

    private class StepTO {

        private Long id;
        private int status;
        private String message;

        public StepTO(StepLog stepLog) {
            this.id = stepLog.getStep().getId();
            this.status = stepLog.getStatus().ordinal();
            this.message = stepLog.getLog();
        }

        public Long getId() {
            return id;
        }

        public int getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }
}
