package nl.finan.jbehave.embeder;

import nl.finan.jbehave.dao.RunningStoriesDao;
import nl.finan.jbehave.dao.StoryDao;
import nl.finan.jbehave.entities.RunningStories;
import nl.finan.jbehave.entities.StoryLog;
import nl.finan.jbehave.service.ReportService;
import org.jbehave.core.model.Story;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
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
    }

    @Test
    public void testBeforeScenario() throws Exception {

    }

    @Test
    public void testAfterScenario() throws Exception {

    }

    @Test
    public void testSuccessful() throws Exception {

    }

    @Test
    public void testPending() throws Exception {

    }

    @Test
    public void testFailed() throws Exception {

    }
}