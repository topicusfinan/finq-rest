package nl.finan.jbehave.embeder;

import nl.finan.jbehave.dao.RunningStoriesDao;
import nl.finan.jbehave.entities.RunningStories;
import org.jbehave.core.model.*;
import org.jbehave.core.reporters.StoryReporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
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

    private Long reportId;


    public void init(Long reportId) {
        this.reportId = reportId;
    }

    @Override
    public void storyNotAllowed(Story story, String filter) {
        RunningStories runningStories = runningStoriesDao.find(reportId);
        runningStories.addToLog("Successful ran " + story);
    }

    @Override
    public void storyCancelled(Story story, StoryDuration storyDuration) {
        RunningStories runningStories = runningStoriesDao.find(reportId);
        runningStories.addToLog("Successful ran " + story);
    }

    @Override
    @Transactional
    public void beforeStory(Story story, boolean givenStory) {
        if(!givenStory){
            RunningStories runningStories = runningStoriesDao.find(reportId);
            runningStories.addToLog("Starting story :" + story.getPath());
            LOGGER.info("Log : {}", runningStories.getLogs());
        }
    }

    @Override
    public void afterStory(boolean givenStory) {
        RunningStories runningStories = runningStoriesDao.find(reportId);
        runningStories.addToLog("Successful ran " + givenStory);

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
    public void beforeScenario(String scenarioTitle) {
        RunningStories runningStories = runningStoriesDao.find(reportId);
        runningStories.addToLog("Starting scenario : " + scenarioTitle);
    }

    @Override
    public void scenarioMeta(Meta meta) {

    }

    @Override
    public void afterScenario() {

    }

    @Override
    public void givenStories(GivenStories givenStories) {

    }

    @Override
    public void givenStories(List<String> storyPaths) {
        RunningStories runningStories = runningStoriesDao.find(reportId);
        runningStories.addToLog("Successful ran " + storyPaths);

    }

    @Override
    public void beforeExamples(List<String> steps, ExamplesTable table) {

    }

    @Override
    public void example(Map<String, String> tableRow) {
        RunningStories runningStories = runningStoriesDao.find(reportId);
        runningStories.addToLog("Successful ran " + tableRow);
    }

    @Override
    public void afterExamples() {

    }

    @Override
    public void beforeStep(String step) {
        LOGGER.info("Succesful ran {}", step);

    }

    @Override
    public void successful(String step) {
        LOGGER.info("Succesful ran {}", step);
        RunningStories runningStories = runningStoriesDao.find(reportId);
        runningStories.addToLog("Successful ran " + step);
    }

    @Override
    public void ignorable(String step) {
        LOGGER.info("Succesful ran {}", step);

    }

    @Override
    public void pending(String step) {
        RunningStories runningStories = runningStoriesDao.find(reportId);
        runningStories.addToLog("Pending " + step);
    }

    @Override
    public void notPerformed(String step) {
        LOGGER.info("Succesful ran {}", step);

    }

    @Override
    public void failed(String step, Throwable cause) {
        RunningStories runningStories = runningStoriesDao.find(reportId);
        runningStories.addToLog("error in step:  " + step + " cause: "+cause.getMessage());

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
