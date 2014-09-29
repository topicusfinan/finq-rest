package nl.finan.finq.websocket;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import javax.websocket.Session;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@RunWith(PowerMockRunner.class)
public class OpenConnectionsTest {

    private OpenConnections openConnections;

    @Before
    public void setUp() throws IllegalArgumentException, IllegalAccessException {
        openConnections = new OpenConnections();
        Field field = Whitebox.getField(OpenConnections.class, "CONNECTION_MAP");
        @SuppressWarnings("unchecked")
        Map<Long, List<Session>> connectionMap = (Map<Long, List<Session>>) field.get(null);
        connectionMap.clear();
    }

    @Test
    public void testAdd() throws IllegalArgumentException, IllegalAccessException {
        Long reportId = 100L;
        Session session = PowerMockito.mock(Session.class);
        openConnections.add(reportId, session);

        Field field = Whitebox.getField(OpenConnections.class, "CONNECTION_MAP");
        @SuppressWarnings("unchecked")
        Map<Long, List<Session>> connectionMap = (Map<Long, List<Session>>) field.get(null);
        Assert.assertEquals(1, connectionMap.size());

        session = PowerMockito.mock(Session.class);
        openConnections.add(reportId, session);

        @SuppressWarnings("unchecked")
        Map<Long, List<Session>> connectionMap2 = (Map<Long, List<Session>>) field.get(null);
        Assert.assertEquals(1, connectionMap2.size());
        Assert.assertEquals(2, connectionMap2.get(reportId).size());
    }

    @Test
    public void testGet() {
        Long reportId = 101L;
        Session session = PowerMockito.mock(Session.class);
        openConnections.add(reportId, session);

        List<Session> sessions = openConnections.get(reportId);
        Assert.assertTrue(sessions.contains(session));
    }

    @Test
    public void testRemove() {
        Session session = PowerMockito.mock(Session.class);
        openConnections.add(100L, session);
        openConnections.add(101L, session);
        openConnections.add(102L, session);

        openConnections.removeFromConnectionMap(100L,session);

        Assert.assertNull(openConnections.get(100L));
        Assert.assertTrue(openConnections.containsKey(101L));
        Assert.assertTrue(openConnections.containsKey(102L));

        openConnections.removeSession(session);

        Assert.assertFalse(openConnections.containsKey(101L));
        Assert.assertFalse(openConnections.containsKey(102L));

    }

}
