package nl.finan.jbehave.embeder;

import nl.finan.jbehave.dao.RunningStoriesDao;
import nl.finan.jbehave.dao.StoryDao;
import nl.finan.jbehave.entities.*;
import nl.finan.jbehave.service.ReportService;
import nl.finan.jbehave.websocket.StatusType;
import nl.finan.jbehave.websocket.StatusWebSocket;

import org.jbehave.core.model.*;
import org.jbehave.core.model.Scenario;
import org.jbehave.core.model.Story;
import org.jbehave.core.reporters.StoryReporter;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.transaction.Transactional;

import java.util.List;
import java.util.Map;

@Local(WebStoryReporter.class)
@Stateful
@Transactional
public class WebStoryReporter implements StoryReporter {

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

            StoryLog storyLog = reportService.createStoryLog(storyModel,runningStories);
            currentStoryLog = storyLog.getId();
            statusWebSocket.sendStatus(reportId,storyLog,StatusType.BEFORE_STORY);
        }
    }

    @Override
    @Transactional
    public void afterStory(boolean givenStory) {
        if(!givenStory && currentStoryLog != null) {
            StoryLog storyLog = reportService.findStoryLog(currentStoryLog);
            if (storyLog.getStatus().equals(RunningStoriesStatus.RUNNING)) {
                storyLog.setStatus(RunningStoriesStatus.SUCCESS);
                statusWebSocket.sendStatus(reportId, storyLog,StatusType.AFTER_STORY);
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
            StoryLog storyLog = reportService.findStoryLog(currentStoryLog);
            for(nl.finan.jbehave.entities.Scenario scenario: storyLog.getStory().getScenarios()){
                if(scenario.getTitle().equals(scenarioTitle)){
                    ScenarioLog scenarioLog = reportService.createScenarioLog(scenario,storyLog);
                    currentScenarioLog = scenarioLog.getId();

                    statusWebSocket.sendStatus(reportId,scenarioLog,StatusType.BEFORE_SCENARIO);
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
            ScenarioLog scenarioLog = reportService.findScenarioLog(currentScenarioLog);
            if (scenarioLog.getStatus().equals(RunningStoriesStatus.RUNNING)) {
                scenarioLog.setStatus(RunningStoriesStatus.SUCCESS);
                statusWebSocket.sendStatus(reportId, scenarioLog, StatusType.AFTER_SCENARIO);
            }
        }
    }

    @Override
    public void givenStories(GivenStories givenStories) {

    }

    @Override
    public void givenStories(List<String> storyPaths) {

    }

    @Override
    public void beforeExamples(List<String> steps, ExamplesTable table) {

    }

    @Override
    public void example(Map<String, String> tableRow) {
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
            ScenarioLog scenarioLog = reportService.findScenarioLog(currentScenarioLog);
            for(String step: scenarioLog.getScenario().getSteps()){
                if(step.equals(runningStep)){
                    StepLog stepLog = reportService.createStepLog(runningStep, scenarioLog, RunningStoriesStatus.SUCCESS);

                    statusWebSocket.sendStatus(reportId,stepLog,StatusType.SUCCESSFUL_STEP);
                }
            }
        }
    }

    @Override
    @Transactional
    public void ignorable(String step) {
    }

    @Override
    public void pending(String runningStep) {
        if(currentScenarioLog != null) {
            ScenarioLog scenarioLog = reportService.findScenarioLog(currentScenarioLog);
            for(String step: scenarioLog.getScenario().getSteps()){
                if(step.equals(runningStep)){
                    StepLog stepLog = reportService.createStepLog(runningStep, scenarioLog, RunningStoriesStatus.PENDING);

                    statusWebSocket.sendStatus(reportId,stepLog,StatusType.PENDING_STEP);
                }
            }
        }
    }

    @Override
    public void notPerformed(String step) {
    }

    @Override
    public void failed(String runningStep, Throwable cause) {
        if(currentScenarioLog != null) {
            ScenarioLog scenarioLog = reportService.findScenarioLog(currentScenarioLog);
            for(String step: scenarioLog.getScenario().getSteps()){
                if(step.equals(runningStep)){
                    StepLog stepLog = reportService.createStepLog(runningStep,scenarioLog,RunningStoriesStatus.FAILED);
                    stepLog.setLog(cause.getCause().getMessage());
                    scenarioLog.getStoryLog().setStatus(RunningStoriesStatus.FAILED);
                    scenarioLog.getStoryLog().getRunningStory().setStatus(RunningStoriesStatus.FAILED);

                    statusWebSocket.sendStatus(reportId,stepLog, StatusType.FAILED_STEP);
                }
            }
        }
    }

    @Override
    public void failedOutcomes(String step, OutcomesTable table) {
    }

    @Override
    public void restarted(String step, Throwable cause) {
    }

    @Override
    public void dryRun() {

    }

    @Override
    public void pendingMethods(List<String> methods){
    }
}
