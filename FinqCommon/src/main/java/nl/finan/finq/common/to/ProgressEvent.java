package nl.finan.finq.common.to;

import nl.finan.finq.entities.ScenarioLog;
import nl.finan.finq.entities.StepLog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProgressEvent implements Serializable
{
    private Long id;
    private int status;
    private StoryTO story;

    public ProgressEvent(ScenarioLog scenarioLog)
    {
        this.story = new StoryTO(scenarioLog);
        this.status = scenarioLog.getStoryLog().getRunningStory().getStatus().ordinal();
        this.id = scenarioLog.getStoryLog().getRunningStory().getId();
    }

    public Long getId()
    {
        return id;
    }

    public int getStatus()
    {
        return status;
    }

    public StoryTO getStory()
    {
        return story;
    }

    private class StoryTO
    {
        private Long id;
        private int status;
        private ScenarioTO scenario;

        public StoryTO(ScenarioLog scenarioLog)
        {
            this.scenario = new ScenarioTO(scenarioLog);
            this.status = scenarioLog.getStoryLog().getStatus().ordinal();
            this.id = scenarioLog.getStoryLog().getStory().getId();
        }

        public Long getId()
        {
            return id;
        }

        public int getStatus()
        {
            return status;
        }

        public ScenarioTO getScenario()
        {
            return scenario;
        }
    }

    private class ScenarioTO
    {
        private Long id;
        private int status;
        private List<StepTO> steps = new ArrayList<>();

        public ScenarioTO(ScenarioLog scenarioLog)
        {
            this.status = scenarioLog.getStatus().ordinal();
            this.id = scenarioLog.getScenario().getId();
            for (StepLog stepLog : scenarioLog.getStepLogs())
            {
                steps.add(new StepTO(stepLog));
            }
        }

        public Long getId()
        {
            return id;
        }

        public int getStatus()
        {
            return status;
        }

        public List<StepTO> getSteps()
        {
            return steps;
        }
    }

    private class StepTO
    {
        private Long id;
        private int status;
        private String message;

        public StepTO(StepLog stepLog)
        {
            this.message = stepLog.getLog();
            this.status = stepLog.getStatus().ordinal();
            this.id = stepLog.getStep().getId();
        }

        public Long getId()
        {
            return id;
        }

        public int getStatus()
        {
            return status;
        }

        public String getMessage()
        {
            return message;
        }
    }
}
