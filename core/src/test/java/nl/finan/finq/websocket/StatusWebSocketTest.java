package nl.finan.finq.websocket;

import nl.finan.finq.dao.RunningStoriesDao;
import nl.finan.finq.entities.LogStatus;
import nl.finan.finq.entities.RunningStories;
import nl.finan.finq.entities.Step;
import nl.finan.finq.entities.StepLog;
import nl.finan.finq.websocket.to.DataTO;
import nl.finan.finq.websocket.to.ReceivingEventTO;
import nl.finan.finq.websocket.to.EventType;
import org.junit.Ignore;
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
@Ignore
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

        Mockito.verify(async).sendText("{\"reportId\":1200,\"log\":{\"id\":1200,\"status\":\"SUCCESS\",\"logs\":[]},\"statusType\":\"INITIAL_STATUS\"}");

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
        StepLog log = new StepLog();
        log.setStatus(LogStatus.SUCCESS);
        log.setId(100L);
        log.setStep(new Step("test Step"));
        statusWebSocket.sendStatus(null);

        Mockito.verify(async).sendText("{\"reportId\":1200,\"log\":{\"id\":100,\"log\":null,\"status\":\"SUCCESS\",\"step\":{\"id\":null,\"title\":\"test Step\",\"template\":null}},\"statusType\":\"FINAL_STATUS\"}");
    }

    @Test
    public void testException() throws IOException{
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

    private ReceivingEventTO createSubscribe(){
        ReceivingEventTO receivingEventTO = new ReceivingEventTO();
        DataTO dataTO = new DataTO();
        dataTO.setRun(1200l);
        receivingEventTO.setData(dataTO);
        receivingEventTO.setEvent(EventType.SUBSCRIBE);
        return receivingEventTO;
    }

    private ReceivingEventTO createUnsubscribe(){
        ReceivingEventTO receivingEventTO = new ReceivingEventTO();
        DataTO dataTO = new DataTO();
        dataTO.setRun(1200l);
        receivingEventTO.setData(dataTO);
        receivingEventTO.setEvent(EventType.UNSUBSCRIBE);
        return receivingEventTO;
    }

}
