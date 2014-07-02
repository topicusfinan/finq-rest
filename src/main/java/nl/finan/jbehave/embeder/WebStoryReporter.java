package nl.finan.jbehave.embeder;

import nl.finan.jbehave.dao.*;
import nl.finan.jbehave.entities.*;
import nl.finan.jbehave.service.ReportService;

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

    @Autowired
    private RunningStoriesDao runningStoriesDao;

    @Autowired
    private StoryDao storyDao;

    @Autowired
    private ReportService reportService;

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
        }
    }

    @Override
    @Transactional
    public void afterStory(boolean givenStory) {
        if(!givenStory && currentStoryLog != null) {
            StoryLog storyLog = reportService.findStoryLog(currentStoryLog);
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
            StoryLog storyLog = reportService.findStoryLog(currentStoryLog);
            for(nl.finan.jbehave.entities.Scenario scenario: storyLog.getStory().getScenarios()){
                if(scenario.getTitle().equals(scenarioTitle)){
                    ScenarioLog scenarioLog = reportService.createScenarioLog(scenario,storyLog);
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
            ScenarioLog scenarioLog = reportService.findScenarioLog(currentScenarioLog);
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
                    reportService.createStepLog(runningStep,scenarioLog,RunningStoriesStatus.SUCCESS);
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
                    reportService.createStepLog(runningStep,scenarioLog,RunningStoriesStatus.PENDING);
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
                    stepLog.setLog(cause.getMessage());
                    scenarioLog.getStoryLog().setStatus(RunningStoriesStatus.FAILED);
                    scenarioLog.getStoryLog().getRunningStory().setStatus(RunningStoriesStatus.FAILED);
                    
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
