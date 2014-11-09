package nl.finan.finq.service;

import nl.finan.finq.dao.RunningStoriesDao;
import nl.finan.finq.dao.StoryDao;
import nl.finan.finq.embeder.StoryRunner;
import nl.finan.finq.entities.*;
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
import javax.jms.*;

import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class RunnerServiceTest {

    @Mock
    private StoryDao storyDao;

    @Mock
    private RunningStoriesDao runningStoriesDao;

    @Mock
    private ManagedExecutorService managedExecutorService;

    @Mock
    private Queue queue;

    @Mock
    private ConnectionFactory connectionFactory;

    @InjectMocks
    private RunnerService runnerService;

    private ObjectMessage message;
    private MessageProducer producer;

    @Before
    public void before() throws JMSException {
        message = mock(ObjectMessage.class);
        Connection connection = mock(Connection.class);
        Session session = mock(Session.class);
        producer = mock(MessageProducer.class);
        when(connectionFactory.createConnection()).thenReturn(connection);
        when(connection.createSession(false, Session.AUTO_ACKNOWLEDGE)).thenReturn(session);
        when(session.createProducer(queue)).thenReturn(producer);
        when(session.createObjectMessage()).thenReturn(message);
    }

    @Test
    public void runStory() throws JMSException {
        Story story = mock(Story.class);
        RunningStories run = runnerService.run(story);

        Mockito.verify(runningStoriesDao).persist(run);
        Assert.assertEquals(LogStatus.RUNNING, run.getStatus());

        Mockito.verify(producer).send(message);
    }

    @Test
    public void runBundle() throws JMSException {
        Book book = mock(Book.class);
        RunningStories run = runnerService.run(book);

        Mockito.verify(runningStoriesDao).persist(run);
        Assert.assertEquals(LogStatus.RUNNING, run.getStatus());

        Mockito.verify(producer).send(message);
    }

}
