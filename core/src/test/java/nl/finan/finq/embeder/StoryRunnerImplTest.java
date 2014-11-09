package nl.finan.finq.embeder;

import nl.finan.finq.DefaultConfiguration;
import nl.finan.finq.dao.RunningStoriesDao;
import nl.finan.finq.entities.*;
import nl.finan.finq.websocket.StatusWebSocket;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.List;

import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
public class StoryRunnerImplTest {

    @Mock
    private RunningStoriesDao runningStoriesDao;

    @Mock
    private StatusWebSocket statusWebSocket;

    @Mock
    private ConfigurationFactory configurationFactory;

    @InjectMocks
    private StoryRunnerImpl statefulStoryRunner;

    @Test
    public void testRun() {
        Story story = createStory();

        when(configurationFactory.getConfiguration()).thenReturn(new DefaultConfiguration());
        RunningStories runningStories = mock(RunningStories.class);
        when(runningStories.getStatus()).thenReturn(LogStatus.RUNNING);
        when(runningStoriesDao.find(100l)).thenReturn(runningStories);
        statefulStoryRunner.run(new RunMessage(Arrays.asList(story), 100l));

        Mockito.verify(statusWebSocket).sendStatus(runningStories);
        Mockito.verify(runningStories).setStatus(LogStatus.SUCCESS);
    }

    @Test
    public void testException() {
        Story story = createStory();

        when(configurationFactory.getConfiguration()).thenReturn(new DefaultConfiguration());
        RunningStories runningStories = mock(RunningStories.class);
        when(runningStoriesDao.find(100l)).thenReturn(runningStories);
        doThrow(new RuntimeException()).when(runningStories).getStatus();
        statefulStoryRunner.run(new RunMessage(Arrays.asList(story), 100l));

        Mockito.verify(runningStories).setStatus(LogStatus.FAILED);
    }


    private Story createStory() {
        Story story = new Story();
        story.setId(100l);
        story.setTitle("Story Name");
        story.setScenarios(createScenarios(story));
        return story;
    }

    private List<Scenario> createScenarios(Story story) {
        Scenario scenario = new Scenario();
        scenario.setId(100l);
        scenario.setStory(story);
        scenario.setTitle("Scenario Title");
        scenario.setSteps(Arrays.asList(new Step("Given this scenario has the right syntax"), new Step("Then JMoribus can parse this story"), new Step("And no exceptions are thrown")));
        return Arrays.asList(scenario);
    }
}
