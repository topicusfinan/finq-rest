package nl.finan.jbehave.embeder;

import java.util.Arrays;
import java.util.List;

import nl.finan.jbehave.dao.RunningStoriesDao;
import nl.finan.jbehave.dao.StoryDao;
import nl.finan.jbehave.entities.RunningStories;
import nl.finan.jbehave.entities.RunningStoriesStatus;
import nl.finan.jbehave.entities.Scenario;
import nl.finan.jbehave.entities.ScenarioLog;
import nl.finan.jbehave.entities.StepLog;
import nl.finan.jbehave.entities.StoryLog;
import nl.finan.jbehave.service.ReportService;

import org.jbehave.core.model.Story;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;


import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class WebStoryReporterTest {

    @Mock
    private RunningStoriesDao runningStoriesDao;

    @Mock
    private StoryDao storyDao;

    @Mock
    private ReportService reportService;

    @InjectMocks
    private WebStoryReporter webStoryReporter;

    @Test
    public void testBeforeStory() throws Exception {
        webStoryReporter.init(1l);
        Story story = PowerMockito.mock(Story.class);
        RunningStories runningStories = PowerMockito.mock(RunningStories.class);
        nl.finan.jbehave.entities.Story storyModel = PowerMockito.mock(nl.finan.jbehave.entities.Story.class);
        StoryLog storyLog = PowerMockito.mock(StoryLog.class);

        when(story.getPath()).thenReturn("100");
        when(runningStoriesDao.find(1l)).thenReturn(runningStories);
        when(storyDao.find(100l)).thenReturn(storyModel);
        when(reportService.createStoryLog(storyModel,runningStories)).thenReturn(storyLog);
        when(storyLog.getId()).thenReturn(20l);

        webStoryReporter.beforeStory(story, false);

        Long currentStoryLog = (Long) Whitebox.getInternalState(webStoryReporter,"currentStoryLog");
        Assert.assertEquals(new Long(20), currentStoryLog);
    }

    @Test
    public void testAfterStory() throws Exception {
    	Whitebox.setInternalState(webStoryReporter, "currentStoryLog", 20l);
        StoryLog storyLog = new StoryLog();
        
        storyLog.setStatus(RunningStoriesStatus.RUNNING);
    	when(reportService.findStoryLog(20l)).thenReturn(storyLog);
    	
    	webStoryReporter.afterStory(false);
    	Assert.assertEquals(RunningStoriesStatus.SUCCESS, storyLog.getStatus());
    	
    	storyLog.setStatus(RunningStoriesStatus.FAILED);
    	webStoryReporter.afterStory(false);
    	Assert.assertEquals(RunningStoriesStatus.FAILED, storyLog.getStatus());
    	
    }

    @Test
    public void testBeforeScenario() throws Exception {
    	Whitebox.setInternalState(webStoryReporter, "currentStoryLog", 20l);
    	Scenario scenario = PowerMockito.mock(Scenario.class);
    	List<Scenario> scenarios = Arrays.asList(scenario);
        StoryLog storyLog = PowerMockito.mock(StoryLog.class);
        ScenarioLog scenarioLog = PowerMockito.mock(ScenarioLog.class);
        nl.finan.jbehave.entities.Story story = PowerMockito.mock(nl.finan.jbehave.entities.Story.class);
        
        when(scenario.getTitle()).thenReturn("Story Title");
    	when(reportService.findStoryLog(20l)).thenReturn(storyLog);
    	when(storyLog.getStory()).thenReturn(story);
    	when(story.getScenarios()).thenReturn(scenarios);
    	when(scenarioLog.getId()).thenReturn(100l);
    	when(reportService.createScenarioLog(scenario, storyLog)).thenReturn(scenarioLog);
    	
    	webStoryReporter.beforeScenario("Story Title");
    	
    	Object internalState = Whitebox.getInternalState(webStoryReporter, "currentScenarioLog");
    	Assert.assertEquals(new Long(100),internalState);
    }

    @Test
    public void testAfterScenario() throws Exception {
    	Whitebox.setInternalState(webStoryReporter, "currentScenarioLog", 100l);
    	ScenarioLog scenarioLog = new ScenarioLog();
        
        scenarioLog.setStatus(RunningStoriesStatus.RUNNING);
    	when(reportService.findScenarioLog(100l)).thenReturn(scenarioLog);
    	
    	webStoryReporter.afterScenario();
    	Assert.assertEquals(RunningStoriesStatus.SUCCESS, scenarioLog.getStatus());
    	
    	scenarioLog.setStatus(RunningStoriesStatus.FAILED);
    	webStoryReporter.afterScenario();
    	Assert.assertEquals(RunningStoriesStatus.FAILED, scenarioLog.getStatus());
    	
    }

    @Test
    public void testSuccessful() throws Exception {
    	ScenarioLog scenarioLog = this.mockStep();
    	
    	webStoryReporter.successful("Then this is a step");
    	
    	Mockito.verify(reportService).createStepLog("Then this is a step", scenarioLog, RunningStoriesStatus.SUCCESS);
    }

    @Test
    public void testPending() throws Exception {
    	ScenarioLog scenarioLog = this.mockStep();
    	
    	webStoryReporter.pending("Then this is a step");
    	
    	Mockito.verify(reportService).createStepLog("Then this is a step", scenarioLog, RunningStoriesStatus.PENDING);
    }

    @Test
    public void testFailed() throws Exception {
    	String runningStep = "Then this is a step";
    	ScenarioLog scenarioLog = this.mockStep();
    	Throwable throwable = PowerMockito.mock(Throwable.class);
    	RunningStories runningStories = PowerMockito.mock(RunningStories.class);
        StoryLog storyLog = PowerMockito.mock(StoryLog.class);
        StepLog steplog = PowerMockito.mock(StepLog.class);
    	
    	when(throwable.getMessage()).thenReturn("Assertion Failed!");
    	when(scenarioLog.getStoryLog()).thenReturn(storyLog);
    	when(storyLog.getRunningStory()).thenReturn(runningStories);
		when(reportService.createStepLog(runningStep,scenarioLog,RunningStoriesStatus.FAILED)).thenReturn(steplog);
    	
    	webStoryReporter.failed(runningStep,throwable);

    	Mockito.verify(steplog).setLog("Assertion Failed!");
    	Mockito.verify(storyLog).setStatus(RunningStoriesStatus.FAILED);
    	Mockito.verify(runningStories).setStatus(RunningStoriesStatus.FAILED);
    }
    
    private ScenarioLog mockStep(){
    	Whitebox.setInternalState(webStoryReporter, "currentScenarioLog", 100l);
    	ScenarioLog scenarioLog = PowerMockito.mock(ScenarioLog.class);
    	Scenario scenario = PowerMockito.mock(Scenario.class);
        List<String> steps = Arrays.asList("Then this is a step");
    	
        when(reportService.findScenarioLog(100l)).thenReturn(scenarioLog);
    	when(scenarioLog.getScenario()).thenReturn(scenario);
    	when(scenario.getSteps()).thenReturn(steps);
    	return scenarioLog;
    }
}