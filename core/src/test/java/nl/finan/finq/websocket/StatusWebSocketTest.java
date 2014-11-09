package nl.finan.finq.websocket;

import nl.finan.finq.dao.RunningStoriesDao;
import nl.finan.finq.entities.*;
import nl.finan.finq.websocket.to.DataTO;
import nl.finan.finq.websocket.to.EventType;
import nl.finan.finq.websocket.to.ReceivingEventTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class StatusWebSocketTest {
    @Mock
    private RunningStoriesDao runningStoriesDao;

    @Mock
    private OpenConnections openConnections;

    @InjectMocks
    private StatusWebSocket statusWebSocket;

    @Test
    public void testSubscribe() throws IOException {
        Session session = mock(Session.class);
        RemoteEndpoint.Async async = mock(RemoteEndpoint.Async.class);
        when(session.getAsyncRemote()).thenReturn(async);
        statusWebSocket.message(session, createSubscribe());

        Mockito.verify(async).sendText("Could not find the report you're subscribing too.");

        RunningStories runningStories = new RunningStories();
        runningStories.setStatus(LogStatus.SUCCESS);
        runningStories.setId(1200L);
        when(runningStoriesDao.find(any(Serializable.class))).thenReturn(runningStories);

        statusWebSocket.message(session, createSubscribe());

        Mockito.verify(async).sendText("{\"event\":\"run:gist\",\"data\":{\"id\":1200,\"status\":1,\"stories\":[]}}");

        Mockito.verify(openConnections).add(Long.valueOf("1200"), session);

    }

    @Test
    public void testUnsubscribe() throws IOException {
        Session session = mock(Session.class);
        statusWebSocket.message(session, createUnsubscribe());

        Mockito.verify(openConnections).removeFromConnectionMap(Long.valueOf(1200), session);

    }

    @Test
    public void testOnClose() {
        Session session = mock(Session.class);
        statusWebSocket.close(session);
        Mockito.verify(openConnections).removeSession(session);
    }

    @Test
    public void testSendStatus() {
        when(openConnections.containsKey(1200L)).thenReturn(true);
        Session session = mock(Session.class);
        RemoteEndpoint.Async async = mock(RemoteEndpoint.Async.class);
        when(session.getAsyncRemote()).thenReturn(async);
        List<Session> sessions = Arrays.asList(session);
        when(openConnections.get(1200l)).thenReturn(sessions);

        RunningStories runningStories = new RunningStories();
        runningStories.setStatus(LogStatus.SUCCESS);
        runningStories.setId(1200l);
        statusWebSocket.sendStatus(runningStories);

        Mockito.verify(async).sendText("{\"event\":\"run:gist\",\"data\":{\"id\":1200,\"status\":1,\"stories\":[]}}");
    }

    @Test
    public void testSendProgress() {
        when(openConnections.containsKey(1200L)).thenReturn(true);
        Session session = mock(Session.class);
        RemoteEndpoint.Async async = mock(RemoteEndpoint.Async.class);
        when(session.getAsyncRemote()).thenReturn(async);
        List<Session> sessions = Arrays.asList(session);
        when(openConnections.get(1200l)).thenReturn(sessions);
        StepLog log = new StepLog();
        log.setStatus(LogStatus.SUCCESS);
        log.setId(100L);
        log.setStep(new Step("test Step"));
        ScenarioLog scenarioLog = new ScenarioLog();
        scenarioLog.getStepLogs().add(log);
        scenarioLog.setStatus(LogStatus.RUNNING);
        scenarioLog.setScenario(new Scenario());
        StoryLog storyLog = new StoryLog();
        scenarioLog.setStoryLog(storyLog);
        storyLog.setStatus(LogStatus.RUNNING);
        storyLog.setStory(new Story());
        storyLog.getStory().setId(10l);
        storyLog.setRunningStory(new RunningStories());
        storyLog.getRunningStory().setStatus(LogStatus.RUNNING);

        RunningStories runningStories = new RunningStories();
        runningStories.setStatus(LogStatus.SUCCESS);
        runningStories.setId(1200l);
        statusWebSocket.sendProgress(1200l, scenarioLog);

        Mockito.verify(async).sendText("{\"event\":\"run:progress\",\"data\":{\"id\":null,\"status\":0,\"story\":{\"id\":10,\"status\":0,\"scenario\":{\"id\":null,\"status\":0,\"steps\":[{\"id\":null,\"status\":1,\"message\":null}]}}}}");
    }

    @Test(expected = IOException.class)
    public void testException() throws IOException {
        Session session = mock(Session.class);
        RemoteEndpoint.Async async = mock(RemoteEndpoint.Async.class);
        when(session.getAsyncRemote()).thenReturn(async);
        RunningStories runningStories = mock(RunningStories.class);
        when(runningStories.getStatus()).thenReturn(LogStatus.SUCCESS);
        when(runningStories.getId()).thenThrow(IOException.class);
        when(runningStoriesDao.find(any(Serializable.class))).thenReturn(runningStories);


        statusWebSocket.message(session, createSubscribe());

        Mockito.verify(async).sendText(null);
    }

    private ReceivingEventTO createSubscribe() {
        ReceivingEventTO receivingEventTO = new ReceivingEventTO();
        DataTO dataTO = new DataTO();
        dataTO.setRun(1200l);
        receivingEventTO.setData(dataTO);
        receivingEventTO.setEvent(EventType.SUBSCRIBE);
        return receivingEventTO;
    }

    private ReceivingEventTO createUnsubscribe() {
        ReceivingEventTO receivingEventTO = new ReceivingEventTO();
        DataTO dataTO = new DataTO();
        dataTO.setRun(1200l);
        receivingEventTO.setData(dataTO);
        receivingEventTO.setEvent(EventType.UNSUBSCRIBE);
        return receivingEventTO;
    }

}
