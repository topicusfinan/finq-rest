package nl.finan.jbehave.service;

import nl.finan.jbehave.dao.ScenarioLogDao;
import nl.finan.jbehave.dao.StepLogDao;
import nl.finan.jbehave.dao.StoryLogDao;
import nl.finan.jbehave.entities.RunningStories;
import nl.finan.jbehave.entities.RunningStoriesStatus;
import nl.finan.jbehave.entities.Scenario;
import nl.finan.jbehave.entities.ScenarioLog;
import nl.finan.jbehave.entities.StepLog;
import nl.finan.jbehave.entities.Story;
import nl.finan.jbehave.entities.StoryLog;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Any;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;

import static org.powermock.api.mockito.PowerMockito.when; 

@RunWith(PowerMockRunner.class)
public class ReportServiceTest {
	
	private static final String STORY_NAME = "storyName";
	
	@Mock
    private StoryLogDao storyLogDao;

	@Mock
    private ScenarioLogDao scenarioLogDao;

	@Mock
    private StepLogDao stepLogDao;
	
	@InjectMocks
	private ReportService reportService;
	
	@Test
	public void testCreateStoryLog(){
		Story story = PowerMockito.mock(Story.class);
		RunningStories runningStories = PowerMockito.mock(RunningStories.class);
		
		StoryLog storyLog = reportService.createStoryLog(story, runningStories);
		
		Mockito.verify(storyLogDao).persist(storyLog);
		Assert.assertSame(story, storyLog.getStory());
		Assert.assertSame(runningStories, storyLog.getRunningStory());
		Assert.assertEquals(RunningStoriesStatus.RUNNING, storyLog.getStatus());		
	}
	
	@Test
	public void testCreateScenarioLog(){
		Scenario scenario = PowerMockito.mock(Scenario.class);
		StoryLog storyLog = PowerMockito.mock(StoryLog.class);
		
		ScenarioLog scenarioLog = reportService.createScenarioLog(scenario,storyLog);
		
		Mockito.verify(scenarioLogDao).persist(scenarioLog);
		Assert.assertSame(scenario, scenarioLog.getScenario());
		Assert.assertSame(storyLog, scenarioLog.getStoryLog());
		Assert.assertEquals(RunningStoriesStatus.RUNNING, scenarioLog.getStatus());		
	}
	
	@Test
	public void testCreateStepLog(){
		String step = "Then this is happening";
		ScenarioLog scenarioLog = PowerMockito.mock(ScenarioLog.class);
		
		StepLog stepLog = reportService.createStepLog(step,scenarioLog,RunningStoriesStatus.PENDING);
		
		Mockito.verify(stepLogDao).persist(stepLog);
		Assert.assertSame(step, stepLog.getStep());
		Assert.assertSame(scenarioLog, stepLog.getScenarioLog());
		Assert.assertEquals(RunningStoriesStatus.PENDING, stepLog.getStatus());		
	}
}
