package nl.finan.jbehave.embeder;

import nl.finan.jbehave.dao.RunningStoriesDao;
import nl.finan.jbehave.entities.RunningStories;
import org.jbehave.core.model.*;
import org.jbehave.core.reporters.StoryReporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Map;

public class WebStoryReporter implements StoryReporter {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebStoryReporter.class);

    private RunningStoriesDao runningStoriesDao;
    private Long reportId;

    public WebStoryReporter(ApplicationContext ctx, Long reportId) {
        this.reportId = reportId;
        runningStoriesDao = ctx.getBean(RunningStoriesDao.class);
    }

    @Override
    public void storyNotAllowed(Story story, String filter) {

    }

    @Override
    public void storyCancelled(Story story, StoryDuration storyDuration) {

    }

    @Override
    public void beforeStory(Story story, boolean givenStory) {
        if(!givenStory){

            RunningStories runningStories = getRunningStories();
            runningStories.addToLog("Starting story :" +story.getPath()+story.getName());
            LOGGER.info("Log : {}" ,runningStories.getLogs());
        }
    }

    @Override
    public void afterStory(boolean givenStory) {

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
        getRunningStories().addToLog("Starting scenario : "+scenarioTitle);
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
    public void beforeStep(String step) {

    }

    @Override
    public void successful(String step) {
        getRunningStories().addToLog("Successful ran "+step);
    }

    @Override
    public void ignorable(String step) {

    }

    @Override
    public void pending(String step) {
        getRunningStories().addToLog("Pending "+step);
    }

    @Override
    public void notPerformed(String step) {

    }

    @Override
    public void failed(String step, Throwable cause) {

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
    public void pendingMethods(List<String> methods) {

    }

    private RunningStories getRunningStories(){
        return runningStoriesDao.find(reportId);
    }
}
