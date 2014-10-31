package nl.finan.finq.embeder;

import nl.eernie.jmoribus.configuration.DefaultConfiguration;
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

import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class StatefulStoryRunnerTest {

    @Mock
    private RunningStoriesDao runningStoriesDao;

    @Mock
    private StatusWebSocket statusWebSocket;

    @Mock
    private ConfigurationFactory configurationFactory;

    @InjectMocks
    private StatefulStoryRunner statefulStoryRunner;

    @Test
    public void testRun(){
        Story story = createStory();
        statefulStoryRunner.init(Arrays.asList(story), 100l);

        when(configurationFactory.getConfigurationWithReporter(100l)).thenReturn(new DefaultConfiguration());
        RunningStories runningStories = mock(RunningStories.class);
        when(runningStoriesDao.find(100l)).thenReturn(runningStories);

        statefulStoryRunner.run();
    }

    @Test
    public void testException(){
        Story story = createStory();
        statefulStoryRunner.init(Arrays.asList(story), 100l);

        when(configurationFactory.getConfigurationWithReporter(100l)).thenReturn(new DefaultConfiguration());
        RunningStories runningStories = mock(RunningStories.class);
        when(runningStoriesDao.find(100l)).thenReturn(runningStories);
        doThrow(new RuntimeException()).when(runningStories).setStatus(LogStatus.SUCCESS);

        statefulStoryRunner.run();
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
