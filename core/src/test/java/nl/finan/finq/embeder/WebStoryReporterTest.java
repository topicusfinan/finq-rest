package nl.finan.finq.embeder;

import nl.eernie.jmoribus.model.Line;
import nl.eernie.jmoribus.model.Scenario;
import nl.eernie.jmoribus.model.StepType;
import nl.eernie.jmoribus.model.Story;
import nl.finan.finq.dao.RunningStoriesDao;
import nl.finan.finq.dao.StoryDao;
import nl.finan.finq.entities.ScenarioLog;
import nl.finan.finq.entities.Step;
import nl.finan.finq.entities.StepLog;
import nl.finan.finq.entities.StoryLog;
import nl.finan.finq.entities.LogStatus;
import nl.finan.finq.entities.RunningStories;
import nl.finan.finq.service.ReportService;
import nl.finan.finq.websocket.StatusWebSocket;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class WebStoryReporterTest {

    @Mock
    private RunningStoriesDao runningStoriesDao;

    @Mock
    private StoryDao storyDao;

    @Mock
    private ReportService reportService;

    @Mock
    private StatusWebSocket statusWebSocket;

    @InjectMocks
    private WebStoryReporter webStoryReporter;


    @Test
    public void testBeforeStory() throws Exception {
        RunningStories runningStories = PowerMockito.mock(RunningStories.class);
        nl.finan.finq.entities.Story storyModel = PowerMockito.mock(nl.finan.finq.entities.Story.class);
        StoryLog storyLog = PowerMockito.mock(StoryLog.class);

        when(runningStoriesDao.find(101l)).thenReturn(runningStories);
        when(storyDao.find(100l)).thenReturn(storyModel);
        when(reportService.createStoryLog(storyModel, runningStories)).thenReturn(storyLog);
        when(storyLog.getId()).thenReturn(20l);

        webStoryReporter.beforeStory(createCompleteStory());
    }

    @Test
    public void testAfterStory() throws Exception {
        StoryLog storyLog = new StoryLog();
        RunningStories runningStories = PowerMockito.mock(RunningStories.class);
        Story story = createCompleteStory();

        storyLog.setStatus(LogStatus.RUNNING);
        when(runningStoriesDao.find(101l)).thenReturn(runningStories);
        when(runningStories.getLogs()).thenReturn(Arrays.asList(storyLog));

        webStoryReporter.afterStory(story);
        Assert.assertEquals(LogStatus.SUCCESS, storyLog.getStatus());

        storyLog.setStatus(LogStatus.FAILED);
        webStoryReporter.afterStory(story);
        Assert.assertEquals(LogStatus.FAILED, storyLog.getStatus());

    }

    @Test
    public void testBeforeScenario() throws Exception {
        RunningStories runningStories = PowerMockito.mock(RunningStories.class);
        nl.finan.finq.entities.Scenario scenario = PowerMockito.mock(nl.finan.finq.entities.Scenario.class);
        List<nl.finan.finq.entities.Scenario> scenarios = Arrays.asList(scenario);
        StoryLog storyLog = PowerMockito.mock(StoryLog.class);
        ScenarioLog scenarioLog = PowerMockito.mock(ScenarioLog.class);
        nl.finan.finq.entities.Story story = PowerMockito.mock(nl.finan.finq.entities.Story.class);

        when(scenario.getTitle()).thenReturn("Scenario Title");
        when(reportService.findStoryLog(20l)).thenReturn(storyLog);
        when(storyLog.getStory()).thenReturn(story);
        when(story.getScenarios()).thenReturn(scenarios);
        when(scenarioLog.getId()).thenReturn(100l);
        when(reportService.createScenarioLog(scenario, storyLog)).thenReturn(scenarioLog);
        when(runningStoriesDao.find(101l)).thenReturn(runningStories);
        when(runningStories.getLogs()).thenReturn(Arrays.asList(storyLog));

        webStoryReporter.beforeScenario(createCompleteStory().getScenarios().get(0));
    }

    @Test
    public void testAfterScenario() throws Exception {
        ScenarioLog scenarioLog = new ScenarioLog();
        RunningStories runningStories = PowerMockito.mock(RunningStories.class);
        StoryLog storyLog = new StoryLog();

        scenarioLog.setStatus(LogStatus.RUNNING);
        when(reportService.findScenarioLog(100l)).thenReturn(scenarioLog);
        when(runningStoriesDao.find(101l)).thenReturn(runningStories);
        when(runningStories.getLogs()).thenReturn(Arrays.asList(storyLog));
        storyLog.setScenarioLogs(new ArrayList<ScenarioLog>());
        storyLog.getScenarioLogs().add(scenarioLog);

        Scenario scenario = createCompleteStory().getScenarios().get(0);
        webStoryReporter.afterScenario(scenario);
        Assert.assertEquals(LogStatus.SUCCESS, scenarioLog.getStatus());

        scenarioLog.setStatus(LogStatus.FAILED);
        webStoryReporter.afterScenario(scenario);
        Assert.assertEquals(LogStatus.FAILED, scenarioLog.getStatus());

    }

    @Test
    public void testSuccessful() throws Exception {
        nl.eernie.jmoribus.model.Step runningStep = new nl.eernie.jmoribus.model.Step(StepType.THEN);
        runningStep.getStepLines().add(new Line("this is a step"));
        RunningStories runningStories = PowerMockito.mock(RunningStories.class);
        StoryLog storyLog = new StoryLog();
        storyLog.setScenarioLogs(new ArrayList<ScenarioLog>());
        ScenarioLog scenarioLog = new ScenarioLog();
        scenarioLog.setScenario(new nl.finan.finq.entities.Scenario());
        Step step = new Step();
        step.setTitle("a test");
        scenarioLog.getScenario().getSteps().add(step);
        storyLog.getScenarioLogs().add(scenarioLog);

        when(runningStoriesDao.find(101l)).thenReturn(runningStories);
        when(runningStories.getLogs()).thenReturn(Arrays.asList(storyLog));

        webStoryReporter.successStep(createCompleteStory().getScenarios().get(0).getSteps().get(0));

        Mockito.verify(reportService).createStepLog(scenarioLog.getScenario().getSteps().get(0), scenarioLog, LogStatus.SUCCESS);
    }

    @Test
    public void testPending() throws Exception {
        nl.eernie.jmoribus.model.Step runningStep = new nl.eernie.jmoribus.model.Step(StepType.THEN);
        runningStep.getStepLines().add(new Line("this is a step"));
        RunningStories runningStories = PowerMockito.mock(RunningStories.class);
        StoryLog storyLog = new StoryLog();
        storyLog.setScenarioLogs(new ArrayList<ScenarioLog>());
        ScenarioLog scenarioLog = new ScenarioLog();
        scenarioLog.setScenario(new nl.finan.finq.entities.Scenario());
        Step step = new Step();
        step.setTitle("a test");
        scenarioLog.getScenario().getSteps().add(step);
        storyLog.getScenarioLogs().add(scenarioLog);

        when(runningStoriesDao.find(101l)).thenReturn(runningStories);
        when(runningStories.getLogs()).thenReturn(Arrays.asList(storyLog));

        webStoryReporter.pendingStep(createCompleteStory().getScenarios().get(0).getSteps().get(0));

        Mockito.verify(reportService).createStepLog(scenarioLog.getScenario().getSteps().get(0), scenarioLog, LogStatus.SKIPPED);
    }

    @Test
    public void testFailed() throws Exception {
        nl.eernie.jmoribus.model.Step runningStep = new nl.eernie.jmoribus.model.Step(StepType.THEN);
        runningStep.getStepLines().add(new Line("this is a step"));
        AssertionError throwable = PowerMockito.mock(AssertionError.class);
        RunningStories runningStories = PowerMockito.mock(RunningStories.class);
        StoryLog storyLog = new StoryLog();
        storyLog.setScenarioLogs(new ArrayList<ScenarioLog>());
        ScenarioLog scenarioLog = new ScenarioLog();
        scenarioLog.setScenario(new nl.finan.finq.entities.Scenario());
        Step step = new Step();
        step.setTitle("a test");
        scenarioLog.getScenario().getSteps().add(step);
        storyLog.getScenarioLogs().add(scenarioLog);
        StepLog stepLog = new StepLog();
        stepLog.setScenarioLog(scenarioLog);
        scenarioLog.setStoryLog(storyLog);
        storyLog.setRunningStory(runningStories);

        when(throwable.getMessage()).thenReturn("Assertion Failed!");
        when(runningStoriesDao.find(101l)).thenReturn(runningStories);
        when(runningStories.getLogs()).thenReturn(Arrays.asList(storyLog));
        when(reportService.createStepLog(step,scenarioLog,LogStatus.FAILED)).thenReturn(stepLog);

        webStoryReporter.failedStep(createCompleteStory().getScenarios().get(0).getSteps().get(0), throwable);

        Assert.assertEquals(stepLog.getLog(),"Assertion Failed!");
        Assert.assertEquals(LogStatus.FAILED,storyLog.getStatus());
        Mockito.verify(runningStories).setStatus(LogStatus.FAILED);
    }


    private Story createCompleteStory(){
        Story story = new Story();
        story.setUniqueIdentifier("100-101");
        story.setTitle("Story title");
        story.getScenarios().add(createScenario(story));
        return story;
    }

    private Scenario createScenario(Story story) {
        Scenario scenario = new Scenario();
        scenario.setStory(story);
        scenario.setTitle("Scenario title");
        scenario.getSteps().addAll(createSteps(scenario));
        return scenario;
    }

    private List<nl.eernie.jmoribus.model.Step> createSteps(Scenario scenario) {
        List<nl.eernie.jmoribus.model.Step> steps = new ArrayList<>();

        nl.eernie.jmoribus.model.Step step1 = new nl.eernie.jmoribus.model.Step(StepType.GIVEN);
        step1.getStepLines().add(new Line("a variable has been set"));
        step1.setStepContainer(scenario);

        nl.eernie.jmoribus.model.Step step2 = new nl.eernie.jmoribus.model.Step(StepType.WHEN);
        step2.getStepLines().add(new Line("the variable has been read"));
        step2.setStepContainer(scenario);

        nl.eernie.jmoribus.model.Step step3 = new nl.eernie.jmoribus.model.Step(StepType.THEN);
        step3.getStepLines().add(new Line("the value will be returned"));
        step3.setStepContainer(scenario);

        return Arrays.asList(step1,step2,step3);
    }
}
