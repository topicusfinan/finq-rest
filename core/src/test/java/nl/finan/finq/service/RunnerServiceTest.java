package nl.finan.finq.service;

import nl.finan.finq.dao.RunningStoriesDao;
import nl.finan.finq.dao.StoryDao;
import nl.finan.finq.embeder.StoryRunner;
import nl.finan.finq.entities.*;
import nl.finan.finq.factory.BeanFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.enterprise.concurrent.ManagedExecutorService;

import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(BeanFactory.class)
public class RunnerServiceTest {

    @Mock
    private StoryDao storyDao;

    @Mock
    private RunningStoriesDao runningStoriesDao;

    @Mock
    private ManagedExecutorService managedExecutorService;

    @InjectMocks
    private RunnerService runnerService;

    private StoryRunner storyRunner = mock(StoryRunner.class);

    @Before
    public void before(){
        PowerMockito.mockStatic(BeanFactory.class);
        when(BeanFactory.getBean("java:module/StatefulStoryRunner")).thenReturn(storyRunner);

    }

    @Test
    public void runStory(){
        Story story = mock(Story.class);
        RunningStories run = runnerService.run(story);

        Mockito.verify(runningStoriesDao).persist(run);
        Assert.assertEquals(LogStatus.RUNNING, run.getStatus());
        Mockito.verify(managedExecutorService).execute(storyRunner);
    }

    @Test
    public void runBundle(){
        Book book = mock(Book.class);
        RunningStories run = runnerService.run(book);

        Mockito.verify(runningStoriesDao).persist(run);
        Assert.assertEquals(LogStatus.RUNNING, run.getStatus());
        Mockito.verify(managedExecutorService).execute(storyRunner);
    }

}
