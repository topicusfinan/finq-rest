package nl.finan.jbehave.service;


import nl.finan.jbehave.dao.ScenarioLogDao;
import nl.finan.jbehave.dao.StepLogDao;
import nl.finan.jbehave.dao.StoryLogDao;
import nl.finan.jbehave.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    @Autowired
    private StoryLogDao storyLogDao;

    @Autowired
    private ScenarioLogDao scenarioLogDao;

    @Autowired
    private StepLogDao stepLogDao;

    public StoryLog createStoryLog(Story story, RunningStories runningStories) {
        StoryLog storyLog = new StoryLog();
        storyLog.setRunningStory(runningStories);
        storyLog.setStory(story);
        storyLog.setStatus(RunningStoriesStatus.RUNNING);
        storyLogDao.persist(storyLog);
        return storyLog;
    }

    public StoryLog findStoryLog(Long id){
        return storyLogDao.find(id);
    }

    public ScenarioLog createScenarioLog(Scenario scenario, StoryLog storyLog) {
        ScenarioLog scenarioLog = new ScenarioLog();
        scenarioLog.setStatus(RunningStoriesStatus.RUNNING);
        scenarioLog.setScenario(scenario);
        scenarioLog.setStoryLog(storyLog);
        scenarioLogDao.persist(scenarioLog);
        return scenarioLog;
    }

    public ScenarioLog findScenarioLog(Long id) {
        return scenarioLogDao.find(id);
    }

    public StepLog createStepLog(String runningStep, ScenarioLog scenarioLog, RunningStoriesStatus status) {
        StepLog stepLog = new StepLog();
        stepLog.setStatus(status);
        stepLog.setStep(runningStep);
        stepLog.setScenarioLog(scenarioLog);
        stepLogDao.persist(stepLog);
        return stepLog;
    }
}
