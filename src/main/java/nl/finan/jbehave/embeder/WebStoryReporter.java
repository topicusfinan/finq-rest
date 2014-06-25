package nl.finan.jbehave.embeder;

import nl.finan.jbehave.dao.*;
import nl.finan.jbehave.entities.*;
import org.jbehave.core.model.*;
import org.jbehave.core.model.Scenario;
import org.jbehave.core.model.Story;
import org.jbehave.core.reporters.StoryReporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component
@Scope("prototype")
@Transactional
public class WebStoryReporter implements StoryReporter {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebStoryReporter.class);

    @Autowired
    private RunningStoriesDao runningStoriesDao;

    @Autowired
    private StoryDao storyDao;

    @Autowired
    private StoryLogDao storyLogDao;

    @Autowired
    private ScenarioLogDao scenarioLogDao;

    @Autowired
    private StepLogDao stepLogDao;

    private Long reportId;

    private Long currentStoryLog;
    private Long currentScenarioLog;
    private Long currentStepLog;

    public void init(Long reportId) {
        this.reportId = reportId;
    }

    @Override
    public void storyNotAllowed(Story story, String filter) {
    }

    @Override
    public void storyCancelled(Story story, StoryDuration storyDuration) {
    }

    @Override
    @Transactional
    public void beforeStory(Story story, boolean givenStory) {
        if(!givenStory && !(story.getPath().equalsIgnoreCase("BeforeStories") || story.getPath().equalsIgnoreCase("AfterStories") ) ){
            RunningStories runningStories = runningStoriesDao.find(reportId);

            nl.finan.jbehave.entities.Story storyModel = storyDao.find(Long.valueOf(story.getPath()));

            StoryLog storyLog = new StoryLog();
            storyLog.setStory(storyModel);
            storyLog.setStatus(RunningStoriesStatus.RUNNING);
            storyLog.setRunningStory(runningStories);
            storyLogDao.persist(storyLog);
            currentStoryLog = storyLog.getId();
        }
    }

    @Override
    @Transactional
    public void afterStory(boolean givenStory) {
        if(!givenStory && currentStoryLog != null) {
            StoryLog storyLog = storyLogDao.find(currentStoryLog);
            if (storyLog.getStatus().equals(RunningStoriesStatus.RUNNING)) {
                storyLog.setStatus(RunningStoriesStatus.SUCCESS);
            }
        }
    }

    @Override
    public void narrative(Narrative narrative) {

    }

    @Override
    public void lifecyle(Lifecycle lifecycle) {

    }

    @Override
    public void scenarioNotAllowed(Scenario scenario, String filter) {

    }

    @Override
    @Transactional
    public void beforeScenario(String scenarioTitle) {
        if(currentStoryLog != null) {
            StoryLog storyLog = storyLogDao.find(currentStoryLog);
            for(nl.finan.jbehave.entities.Scenario scenario: storyLog.getStory().getScenarios()){
                if(scenario.getTitle().equals(scenarioTitle)){
                    ScenarioLog scenarioLog = new ScenarioLog();
                    scenarioLog.setStatus(RunningStoriesStatus.RUNNING);
                    scenarioLog.setScenario(scenario);
                    scenarioLog.setStoryLog(storyLog);
                    scenarioLogDao.persist(scenarioLog);
                    currentScenarioLog = scenarioLog.getId();
                }
            }
        }
    }

    @Override
    public void scenarioMeta(Meta meta) {

    }

    @Override
    public void afterScenario() {
        if(currentScenarioLog != null) {
            ScenarioLog scenarioLog = scenarioLogDao.find(currentScenarioLog);
            if (scenarioLog.getStatus().equals(RunningStoriesStatus.RUNNING)) {
                scenarioLog.setStatus(RunningStoriesStatus.SUCCESS);
            }
        }
    }

    @Override
    public void givenStories(GivenStories givenStories) {

    }

    @Override
    public void givenStories(List<String> storyPaths) {
        RunningStories runningStories = runningStoriesDao.find(reportId);
        //runningStories.addToLog("Successful ran " + storyPaths);

    }

    @Override
    public void beforeExamples(List<String> steps, ExamplesTable table) {

    }

    @Override
    public void example(Map<String, String> tableRow) {
        RunningStories runningStories = runningStoriesDao.find(reportId);
        //runningStories.addToLog("Successful ran " + tableRow);
    }

    @Override
    public void afterExamples() {

    }

    @Override
    @Transactional
    public void beforeStep(String runningStep) {
    }

    @Override
    @Transactional
    public void successful(String runningStep) {
        if(currentScenarioLog != null) {
            ScenarioLog scenarioLog = scenarioLogDao.find(currentScenarioLog);
            for(String step: scenarioLog.getScenario().getSteps()){
                if(step.equals(runningStep)){
                    StepLog stepLog = new StepLog();
                    stepLog.setStatus(RunningStoriesStatus.SUCCESS);
                    stepLog.setStep(runningStep);
                    stepLog.setScenarioLog(scenarioLog);
                    stepLogDao.persist(stepLog);
                }
            }
        }
    }

    @Override
    @Transactional
    public void ignorable(String step) {
        LOGGER.info("Succesful ran {}", step);

    }

    @Override
    public void pending(String runningStep) {
        if(currentScenarioLog != null) {
            ScenarioLog scenarioLog = scenarioLogDao.find(currentScenarioLog);
            for(String step: scenarioLog.getScenario().getSteps()){
                if(step.equals(runningStep)){
                    StepLog stepLog = new StepLog();
                    stepLog.setStatus(RunningStoriesStatus.PENDING);
                    stepLog.setStep(runningStep);
                    stepLog.setScenarioLog(scenarioLog);
                    stepLogDao.persist(stepLog);
                }
            }
        }
    }

    @Override
    public void notPerformed(String step) {
        LOGGER.info("Succesful ran {}", step);

    }

    @Override
    public void failed(String runningStep, Throwable cause) {
        if(currentScenarioLog != null) {
            ScenarioLog scenarioLog = scenarioLogDao.find(currentScenarioLog);
            for(String step: scenarioLog.getScenario().getSteps()){
                if(step.equals(runningStep)){
                    StepLog stepLog = new StepLog();
                    stepLog.setStatus(RunningStoriesStatus.FAILED);
                    stepLog.setStep(runningStep);
                    stepLog.setScenarioLog(scenarioLog);
                    stepLog.setLog(cause.getMessage());
                    scenarioLog.setStatus(RunningStoriesStatus.FAILED);
                    scenarioLog.getStoryLog().setStatus(RunningStoriesStatus.FAILED);
                    scenarioLog.getStoryLog().getRunningStory().setStatus(RunningStoriesStatus.FAILED);
                    stepLogDao.persist(stepLog);
                    
                }
            }
        }
    }

    @Override
    public void failedOutcomes(String step, OutcomesTable table) {
        LOGGER.info("Succesful ran {}", step);

    }

    @Override
    public void restarted(String step, Throwable cause) {
        LOGGER.info("Succesful ran {}", step);

    }

    @Override
    public void dryRun() {

    }

    @Override
    public void pendingMethods(List<String> methods) {
        LOGGER.info("Succesful ran {}", methods);

    }

}
