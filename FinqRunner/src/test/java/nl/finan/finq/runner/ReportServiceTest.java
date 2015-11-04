package nl.finan.finq.runner;

import nl.finan.finq.dao.ScenarioLogDao;
import nl.finan.finq.dao.StepLogDao;
import nl.finan.finq.dao.StoryLogDao;
import nl.finan.finq.entities.LogStatus;
import nl.finan.finq.entities.RunningStories;
import nl.finan.finq.entities.Scenario;
import nl.finan.finq.entities.ScenarioLog;
import nl.finan.finq.entities.Step;
import nl.finan.finq.entities.StepLog;
import nl.finan.finq.entities.Story;
import nl.finan.finq.entities.StoryLog;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.Serializable;

import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class ReportServiceTest
{

	@Mock
	private StoryLogDao storyLogDao;

	@Mock
	private ScenarioLogDao scenarioLogDao;

	@Mock
	private StepLogDao stepLogDao;

	@InjectMocks
	private ReportService reportService;

	@Test
	public void testCreateStoryLog()
	{
		Story story = PowerMockito.mock(Story.class);
		RunningStories runningStories = PowerMockito.mock(RunningStories.class);

		StoryLog storyLog = reportService.createStoryLog(story, runningStories);

		Mockito.verify(storyLogDao).persist(storyLog);
		Assert.assertSame(story, storyLog.getStory());
		Assert.assertSame(runningStories, storyLog.getRunningStory());
		Assert.assertEquals(LogStatus.RUNNING, storyLog.getStatus());
	}

	@Test
	public void testCreateScenarioLog()
	{
		Scenario scenario = PowerMockito.mock(Scenario.class);
		StoryLog storyLog = PowerMockito.mock(StoryLog.class);

		ScenarioLog scenarioLog = reportService.createScenarioLog(scenario, storyLog);

		Mockito.verify(scenarioLogDao).persist(scenarioLog);
		Assert.assertSame(scenario, scenarioLog.getScenario());
		Assert.assertSame(storyLog, scenarioLog.getStoryLog());
		Assert.assertEquals(LogStatus.RUNNING, scenarioLog.getStatus());
	}

	@Test
	public void testCreateStepLog()
	{
		Step step = new Step("Test step");
		ScenarioLog scenarioLog = PowerMockito.mock(ScenarioLog.class);

		StepLog stepLog = reportService.createStepLog(step, scenarioLog, LogStatus.SKIPPED);

		Mockito.verify(stepLogDao).persist(stepLog);
		Assert.assertEquals(step.getTitle(), stepLog.getStep().getTitle());
		Assert.assertSame(scenarioLog, stepLog.getScenarioLog());
		Assert.assertEquals(LogStatus.SKIPPED, stepLog.getStatus());
	}

	@Test
	public void testFindStoryLog()
	{
		StoryLog storyLog = new StoryLog();
		when(storyLogDao.find(any(Serializable.class))).thenReturn(storyLog);

		StoryLog log = reportService.findStoryLog(100L);

		Assert.assertSame(storyLog, log);
	}

	@Test
	public void testFindScenarioLog()
	{
		ScenarioLog scenarioLog = new ScenarioLog();
		when(scenarioLogDao.find(any(Serializable.class))).thenReturn(scenarioLog);

		ScenarioLog log = reportService.findScenarioLog(100L);

		Assert.assertSame(scenarioLog, log);
	}
}
