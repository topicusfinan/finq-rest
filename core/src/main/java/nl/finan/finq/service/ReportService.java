package nl.finan.finq.service;


import nl.eernie.jmoribus.model.Step;
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
        storyLog.setStatus(RunningStoriesStatus.RUNNING);
        storyLogDao.persist(storyLog);
        return storyLog;
    }

    public StoryLog findStoryLog(Long id) {
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

    public StepLog createStepLog(Step runningStep, ScenarioLog scenarioLog, RunningStoriesStatus status) {
        StepLog stepLog = new StepLog();
        stepLog.setStatus(status);
        stepLog.setStep(runningStep.getCombinedStepLines());
        stepLog.setScenarioLog(scenarioLog);
        stepLogDao.persist(stepLog);
        return stepLog;
    }
}
