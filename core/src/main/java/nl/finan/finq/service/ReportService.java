package nl.finan.finq.service;


import nl.finan.finq.dao.ScenarioLogDao;
import nl.finan.finq.dao.StepLogDao;
import nl.finan.finq.dao.StoryLogDao;
import nl.finan.finq.entities.*;

import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class ReportService {

    @EJB
    private StoryLogDao storyLogDao;

    @EJB
    private ScenarioLogDao scenarioLogDao;

    @EJB
    private StepLogDao stepLogDao;

    public StoryLog createStoryLog(Story story, RunningStories runningStories) {
        StoryLog storyLog = new StoryLog();
        storyLog.setRunningStory(runningStories);
        storyLog.setStory(story);
        storyLog.setStatus(LogStatus.RUNNING);
        storyLogDao.persist(storyLog);
        runningStories.getLogs().add(storyLog);
        return storyLog;
    }

    public StoryLog findStoryLog(Long id) {
        return storyLogDao.find(id);
    }

    public ScenarioLog createScenarioLog(Scenario scenario, StoryLog storyLog) {
        ScenarioLog scenarioLog = new ScenarioLog();
        scenarioLog.setStatus(LogStatus.RUNNING);
        scenarioLog.setScenario(scenario);
        scenarioLog.setStoryLog(storyLog);
        scenarioLogDao.persist(scenarioLog);
        storyLog.getScenarioLogs().add(scenarioLog);
        return scenarioLog;
    }

    public ScenarioLog findScenarioLog(Long id) {
        return scenarioLogDao.find(id);
    }

    public StepLog createStepLog(Step runningStep, ScenarioLog scenarioLog, LogStatus status) {
        StepLog stepLog = new StepLog();
        stepLog.setStatus(status);
        stepLog.setStep(runningStep);
        stepLog.setScenarioLog(scenarioLog);
        stepLogDao.persist(stepLog);
        scenarioLog.getStepLogs().add(stepLog);
        return stepLog;
    }
}
