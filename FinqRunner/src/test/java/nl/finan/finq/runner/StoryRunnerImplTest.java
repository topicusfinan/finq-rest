package nl.finan.finq.runner;

import nl.finan.finq.common.configuration.ConfigurationFactory;
import nl.finan.finq.common.jms.RunMessage;
import nl.finan.finq.entities.LogStatus;
import nl.finan.finq.entities.RunningStories;
import nl.finan.finq.entities.Scenario;
import nl.finan.finq.entities.Step;
import nl.finan.finq.entities.Story;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(PowerMockRunner.class)
public class StoryRunnerImplTest
{
    @Mock
    private ConfigurationFactory configurationFactory;

    @Mock
    private WebStoryReporter webStoryReporter;

    @InjectMocks
    private StoryRunnerImpl statefulStoryRunner;

    @Test
    public void testRun()
    {
        Story story = createStory();

        PowerMockito.when(configurationFactory.getConfiguration()).thenReturn(new DefaultConfiguration());
        RunningStories runningStories = PowerMockito.mock(RunningStories.class);
        PowerMockito.when(runningStories.getStatus()).thenReturn(LogStatus.RUNNING);
        Map<String, String> stories = new HashMap<>();
        stories.put(story.getId() + "-" + 100l, story.toStory());
        statefulStoryRunner.run(new RunMessage(stories, null, 100l));

        Mockito.verify(webStoryReporter).afterSuccessRun(100l);
    }

    @Test
    public void testException()
    {
        Story story = createStory();

        PowerMockito.when(configurationFactory.getConfiguration()).thenReturn(new DefaultConfiguration());
        PowerMockito.doThrow(new RuntimeException()).when(webStoryReporter).afterSuccessRun(100l);
        Map<String, String> stories = new HashMap<>();
        stories.put(story.getId() + "-" + 100l, story.toStory());
        statefulStoryRunner.run(new RunMessage(stories, null, 100l));

        Mockito.verify(webStoryReporter).afterErrorRun(100l);
    }

    private Story createStory()
    {
        Story story = new Story();
        story.setId(100l);
        story.setTitle("Story Name");
        story.setScenarios(createScenarios(story));
        return story;
    }

    private List<Scenario> createScenarios(Story story)
    {
        Scenario scenario = new Scenario();
        scenario.setId(100l);
        scenario.setStory(story);
        scenario.setTitle("Scenario Title");
        scenario.setSteps(Arrays.asList(new Step("Given this scenario has the right syntax"), new Step("Then JMoribus can parse this story"), new Step("And no exceptions are thrown")));
        return Arrays.asList(scenario);
    }
}
