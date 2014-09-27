package nl.finan.finq.service;

import nl.eernie.jmoribus.model.Line;
import nl.eernie.jmoribus.model.Step;
import nl.eernie.jmoribus.model.StepType;
import nl.finan.finq.dao.ScenarioLogDao;
import nl.finan.finq.dao.StepLogDao;
import nl.finan.finq.dao.StoryLogDao;
import nl.finan.finq.entities.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class ReportServiceTest {


    @Mock
    private StoryLogDao storyLogDao;

    @Mock
    private ScenarioLogDao scenarioLogDao;

    @Mock
    private StepLogDao stepLogDao;

    @InjectMocks
    private ReportService reportService;

    @Test
    public void testCreateStoryLog() {
        Story story = PowerMockito.mock(Story.class);
        RunningStories runningStories = PowerMockito.mock(RunningStories.class);

        StoryLog storyLog = reportService.createStoryLog(story, runningStories);

        Mockito.verify(storyLogDao).persist(storyLog);
        Assert.assertSame(story, storyLog.getStory());
        Assert.assertSame(runningStories, storyLog.getRunningStory());
        Assert.assertEquals(RunningStoriesStatus.RUNNING, storyLog.getStatus());
    }

    @Test
    public void testCreateScenarioLog() {
        Scenario scenario = PowerMockito.mock(Scenario.class);
        StoryLog storyLog = PowerMockito.mock(StoryLog.class);

        ScenarioLog scenarioLog = reportService.createScenarioLog(scenario, storyLog);

        Mockito.verify(scenarioLogDao).persist(scenarioLog);
        Assert.assertSame(scenario, scenarioLog.getScenario());
        Assert.assertSame(storyLog, scenarioLog.getStoryLog());
        Assert.assertEquals(RunningStoriesStatus.RUNNING, scenarioLog.getStatus());
    }

    @Test
    public void testCreateStepLog() {
        Step step = new Step(StepType.WHEN);
        step.getStepLines().add(new Line("Test step"));
        ScenarioLog scenarioLog = PowerMockito.mock(ScenarioLog.class);

        StepLog stepLog = reportService.createStepLog(step, scenarioLog, RunningStoriesStatus.PENDING);

        Mockito.verify(stepLogDao).persist(stepLog);
        Assert.assertEquals(step.getCombinedStepLines(), stepLog.getStep());
        Assert.assertSame(scenarioLog, stepLog.getScenarioLog());
        Assert.assertEquals(RunningStoriesStatus.PENDING, stepLog.getStatus());
    }
}
