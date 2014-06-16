package nl.finan.jbehave.rest.embeder;

import nl.finan.jbehave.dao.RunningStoriesDao;
import nl.finan.jbehave.entities.RunningStories;
import nl.finan.jbehave.rest.RunningContextLocal;
import org.jbehave.core.model.*;
import org.jbehave.core.reporters.StoryReporter;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class WebStoryReporter implements StoryReporter {

    @Autowired
    private RunningContextLocal runningContext;

    @Autowired
    private RunningStoriesDao runningStoriesDao;

    @Override
    public void storyNotAllowed(Story story, String filter) {

    }

    @Override
    public void storyCancelled(Story story, StoryDuration storyDuration) {

    }

    @Override
    public void beforeStory(Story story, boolean givenStory) {
        if(!givenStory){
            getRunningStories().getLogs().add("Starting story :" +story.getPath()+story.getName());
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
        getRunningStories().getLogs().add("Starting scenario : "+scenarioTitle);
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
        getRunningStories().getLogs().add("Successful ran "+step);
    }

    @Override
    public void ignorable(String step) {

    }

    @Override
    public void pending(String step) {
        getRunningStories().getLogs().add("Pending "+step);
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
        Long id = runningContext.get().getRunningStoriesId();
        return runningStoriesDao.find(id);
    }
}
