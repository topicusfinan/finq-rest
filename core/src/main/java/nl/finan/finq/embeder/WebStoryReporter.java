package nl.finan.finq.embeder;


import nl.eernie.jmoribus.model.*;
import nl.eernie.jmoribus.model.Scenario;
import nl.eernie.jmoribus.model.Step;
import nl.eernie.jmoribus.model.Story;
import nl.eernie.jmoribus.reporter.Reporter;
import nl.finan.finq.dao.RunningStoriesDao;
import nl.finan.finq.dao.StoryDao;
import nl.finan.finq.entities.*;
import nl.finan.finq.service.ReportService;
import nl.finan.finq.websocket.StatusType;
import nl.finan.finq.websocket.StatusWebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.transaction.Transactional;

@Local(WebStoryReporter.class)
@Stateful
@Transactional
public class WebStoryReporter implements Reporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebStoryReporter.class);

    @EJB
    private RunningStoriesDao runningStoriesDao;

    @EJB
    private StoryDao storyDao;

    @EJB
    private ReportService reportService;

    @EJB
    private StatusWebSocket statusWebSocket;

    private Long reportId;

    private Long currentStoryLog;

    private Long currentScenarioLog;


    public void init(Long reportId) {
        this.reportId = reportId;
    }

    @Override
    @Transactional
    public void beforeStory(Story story) {
        RunningStories runningStories = runningStoriesDao.find(reportId);

        nl.finan.finq.entities.Story storyModel = storyDao.find(Long.valueOf(story.getUniqueIdentifier()));

        StoryLog storyLog = reportService.createStoryLog(storyModel, runningStories);
        currentStoryLog = storyLog.getId();
        statusWebSocket.sendStatus(reportId, storyLog, StatusType.BEFORE_STORY);
    }

    @Override
    public void beforeScenario(Scenario beforeScenario) {
        if (currentStoryLog != null) {
            StoryLog storyLog = reportService.findStoryLog(currentStoryLog);
            for (nl.finan.finq.entities.Scenario scenario : storyLog.getStory().getScenarios()) {
                if (scenario.getTitle().equals(beforeScenario.getTitle())) {
                    ScenarioLog scenarioLog = reportService.createScenarioLog(scenario, storyLog);
                    currentScenarioLog = scenarioLog.getId();
                    statusWebSocket.sendStatus(reportId, scenarioLog, StatusType.BEFORE_SCENARIO);
                }
            }
        }
    }

    @Override
    public void beforeStep(Step runningStep) {

    }

    @Override
    public void successStep(Step runningStep) {
        if (currentScenarioLog != null) {
            ScenarioLog scenarioLog = reportService.findScenarioLog(currentScenarioLog);
            int index = runningStep.getStepContainer().getSteps().indexOf(runningStep);
            nl.finan.finq.entities.Step step = scenarioLog.getScenario().getSteps().get(index);
            StepLog stepLog = reportService.createStepLog(step, scenarioLog, LogStatus.SUCCESS);
            statusWebSocket.sendStatus(reportId, stepLog, StatusType.SUCCESSFUL_STEP);
        }
    }

    @Override
    public void pendingStep(Step runningStep) {
        if (currentScenarioLog != null) {
            ScenarioLog scenarioLog = reportService.findScenarioLog(currentScenarioLog);
            int index = runningStep.getStepContainer().getSteps().indexOf(runningStep);
            nl.finan.finq.entities.Step step = scenarioLog.getScenario().getSteps().get(index);
            StepLog stepLog = reportService.createStepLog(step, scenarioLog, LogStatus.PENDING);
            statusWebSocket.sendStatus(reportId, stepLog, StatusType.PENDING_STEP);
        }
    }

    @Override
    public void afterScenario(Scenario scenario) {
        if (currentScenarioLog != null) {
            ScenarioLog scenarioLog = reportService.findScenarioLog(currentScenarioLog);
            if (scenarioLog.getStatus().equals(LogStatus.RUNNING)) {
                scenarioLog.setStatus(LogStatus.SUCCESS);
                statusWebSocket.sendStatus(reportId, scenarioLog, StatusType.AFTER_SCENARIO);
            }
        }
    }

    @Override
    public void afterStory(Story story) {
        if (currentStoryLog != null) {
            StoryLog storyLog = reportService.findStoryLog(currentStoryLog);
            if (storyLog.getStatus().equals(LogStatus.RUNNING)) {
                storyLog.setStatus(LogStatus.SUCCESS);
                statusWebSocket.sendStatus(reportId, storyLog, StatusType.AFTER_STORY);
            }
        }
    }

    @Override
    public void failedStep(Step runningStep, AssertionError e) {
        if (currentScenarioLog != null) {
            ScenarioLog scenarioLog = reportService.findScenarioLog(currentScenarioLog);

            int index = runningStep.getStepContainer().getSteps().indexOf(runningStep);
            nl.finan.finq.entities.Step step = scenarioLog.getScenario().getSteps().get(index);

            StepLog stepLog = reportService.createStepLog(step, scenarioLog, LogStatus.FAILED);
            stepLog.setLog(e.getMessage());
            scenarioLog.getStoryLog().setStatus(LogStatus.FAILED);
            scenarioLog.setStatus(LogStatus.FAILED);
            scenarioLog.getStoryLog().getRunningStory().setStatus(LogStatus.FAILED);
            statusWebSocket.sendStatus(reportId, stepLog, StatusType.FAILED_STEP);
        }
    }

    @Override
    public void errorStep(Step step, Throwable e) {

    }

    @Override
    public void errorStep(Step step, String cause) {

    }

    @Override
    public void feature(Feature feature) {

    }

    @Override
    public void beforePrologue(Prologue prologue) {

    }

    @Override
    public void afterPrologue(Prologue prologue) {

    }

    @Override
    public void beforeReferringScenario(StepContainer stepContainer, Scenario scenario) {

    }

    @Override
    public void afterReferringScenario(StepContainer stepContainer, Scenario scenario) {

    }

    private String completeStep(Step runningStep) {
        return runningStep.getStepType().name() + " " + runningStep.getCombinedStepLines();
    }
}
