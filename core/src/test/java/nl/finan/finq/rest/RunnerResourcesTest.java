package nl.finan.finq.rest;

import nl.finan.finq.dao.BookDao;
import nl.finan.finq.dao.RunningStoriesDao;
import nl.finan.finq.dao.ScenarioDao;
import nl.finan.finq.dao.StoryDao;
import nl.finan.finq.entities.LogStatus;
import nl.finan.finq.entities.RunningStories;
import nl.finan.finq.service.RunnerService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class RunnerResourcesTest {

    @Mock
    private StoryDao storyDao;

    @Mock
    private BookDao bookDao;

    @Mock
    private ScenarioDao scenarioDao;

    @Mock
    private RunningStoriesDao runningStoriesDao;

    @Mock
    private RunnerService runnerService;

    @InjectMocks
    private RunnerResources runnerResources;

    @Test
    public void testGetRuns() throws URISyntaxException {

        when(runningStoriesDao.countBySatuses(Mockito.anyListOf(LogStatus.class))).thenReturn(1l);
        when(runningStoriesDao.countAll()).thenReturn(3l);

        when(runningStoriesDao.listAll()).thenReturn(Arrays.asList(new RunningStories(), new RunningStories(), new RunningStories()));
        when(runningStoriesDao.findByStatuses(Arrays.asList(LogStatus.RUNNING),0,1)).thenReturn(Arrays.asList(new RunningStories()));

        Page<RunningStories> running = runnerResources.getRuns(null, Arrays.asList("RUNNING"), 0, 1);
        Assert.assertEquals(Long.valueOf(1),running.getTotalCount());
        Assert.assertEquals(Integer.valueOf(1), running.getPageSize());
        Assert.assertEquals(Integer.valueOf(0), running.getPage());

        UriInfo uriInfo = mock(UriInfo.class);
        UriBuilder uriBuilder = mock(UriBuilder.class);
        URI uri = new URI("http://test");
        when(uriInfo.getRequestUriBuilder()).thenReturn(uriBuilder);
        when(uriBuilder.replaceQueryParam(Mockito.anyString(),Mockito.anyString())).thenReturn(uriBuilder);
        when(uriBuilder.build()).thenReturn(uri);

        running = runnerResources.getRuns(uriInfo,null,1,1);
        Assert.assertEquals(Long.valueOf(3),running.getTotalCount());
        Assert.assertEquals(Integer.valueOf(1), running.getPageSize());
        Assert.assertEquals(Integer.valueOf(1), running.getPage());
        Assert.assertEquals(2, running.get_link().size());

    }



}
