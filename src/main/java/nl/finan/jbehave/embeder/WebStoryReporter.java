package nl.finan.jbehave.embeder;


import nl.eernie.jmoribus.model.*;
import nl.eernie.jmoribus.reporter.Reporter;
import nl.finan.jbehave.dao.RunningStoriesDao;
import nl.finan.jbehave.dao.StoryDao;
import nl.finan.jbehave.entities.RunningStories;
import nl.finan.jbehave.entities.RunningStoriesStatus;
import nl.finan.jbehave.entities.ScenarioLog;
import nl.finan.jbehave.entities.StoryLog;
import nl.finan.jbehave.service.ReportService;
import nl.finan.jbehave.websocket.StatusWebSocket;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Local(WebStoryReporter.class)
@Stateful
@Transactional
public class WebStoryReporter implements Reporter {

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

        nl.finan.jbehave.entities.Story storyModel = storyDao.find(Long.valueOf(story.getUniqueIdentifier()));

        StoryLog storyLog = reportService.createStoryLog(storyModel,runningStories);
        currentStoryLog = storyLog.getId();
        statusWebSocket.sendStatus(reportId,storyLog);
    }

    @Override
    public void beforeScenario(Scenario beforeScenario) {
        if(currentStoryLog != null) {
            StoryLog storyLog = reportService.findStoryLog(currentStoryLog);
            for(nl.finan.jbehave.entities.Scenario scenario: storyLog.getStory().getScenarios()){
                if(scenario.getTitle().equals(beforeScenario.getTitle())){
                    ScenarioLog scenarioLog = reportService.createScenarioLog(scenario,storyLog);
                    currentScenarioLog = scenarioLog.getId();
                }
            }
        }
    }

    @Override
    public void beforeStep(Step runningStep) {
        if(currentScenarioLog != null) {
            ScenarioLog scenarioLog = reportService.findScenarioLog(currentScenarioLog);
            for(String step: scenarioLog.getScenario().getSteps()){
                if(step.equals(runningStep.getCombinedStepLines())){
                    reportService.createStepLog(runningStep.toString(),scenarioLog,RunningStoriesStatus.PENDING);
                }
            }
        }

    }

    @Override
    public void successStep(Step runningStep) {
        if(currentScenarioLog != null) {
            ScenarioLog scenarioLog = reportService.findScenarioLog(currentScenarioLog);
            for(String step: scenarioLog.getScenario().getSteps()){
                if(step.equals(runningStep.getCombinedStepLines())){
                    reportService.createStepLog(runningStep.toString(),scenarioLog,RunningStoriesStatus.SUCCESS);
                }
            }
        }
    }

    @Override
    public void pendingStep(Step runningStep) {
        if(currentScenarioLog != null) {
            ScenarioLog scenarioLog = reportService.findScenarioLog(currentScenarioLog);
            for(String step: scenarioLog.getScenario().getSteps()){
                if(step.equals(runningStep.getCombinedStepLines())){
                    reportService.createStepLog(runningStep.toString(),scenarioLog,RunningStoriesStatus.PENDING);
                }
            }
        }
    }

    @Override
    public void afterScenario(Scenario scenario) {
        if(currentScenarioLog != null) {
            ScenarioLog scenarioLog = reportService.findScenarioLog(currentScenarioLog);
            if (scenarioLog.getStatus().equals(RunningStoriesStatus.RUNNING)) {
                scenarioLog.setStatus(RunningStoriesStatus.SUCCESS);
            }
        }
    }

    @Override
    public void afterStory(Story story) {
        if(currentStoryLog != null) {
            StoryLog storyLog = reportService.findStoryLog(currentStoryLog);
            if (storyLog.getStatus().equals(RunningStoriesStatus.RUNNING)) {
                storyLog.setStatus(RunningStoriesStatus.SUCCESS);
            }
        }
    }

    @Override
    public void failedStep(Step step, AssertionError e) {

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
}
