package nl.finan.finq.service;

import nl.finan.finq.dao.RunningStoriesDao;
import nl.finan.finq.dao.StoryDao;
import nl.finan.finq.entities.Book;
import nl.finan.finq.entities.Environment;
import nl.finan.finq.entities.LogStatus;
import nl.finan.finq.entities.RunningStories;
import nl.finan.finq.entities.Story;
import nl.finan.finq.entities.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.enterprise.concurrent.ManagedExecutorService;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class RunnerServiceTest
{

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
	public void before() throws JMSException
	{
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
	public void runStory() throws JMSException
	{
		Story story = mock(Story.class);
		User user = mock(User.class);
		Environment environment = mock(Environment.class);
		doAnswer(new SetIdAnwser()).when(runningStoriesDao).persist(any(RunningStories.class));

		RunningStories run = runnerService.run(story, user, environment);

		Mockito.verify(runningStoriesDao).persist(run);
		run.setId(1l);
		Assert.assertEquals(LogStatus.RUNNING, run.getStatus());

		Mockito.verify(producer).send(message);
	}

	@Test
	public void runBundle() throws JMSException
	{
		Book book = mock(Book.class);
		User user = mock(User.class);
		Environment environment = mock(Environment.class);
		doAnswer(new SetIdAnwser()).when(runningStoriesDao).persist(any(RunningStories.class));

		RunningStories run = runnerService.run(book, user, environment);

		Mockito.verify(runningStoriesDao).persist(run);
		Assert.assertEquals(LogStatus.RUNNING, run.getStatus());

		Mockito.verify(producer).send(message);
	}

	class SetIdAnwser implements Answer<Void>
	{

		@Override
		public Void answer(InvocationOnMock invocationOnMock) throws Throwable
		{
			RunningStories run = (RunningStories) invocationOnMock.getArguments()[0];
			run.setId(1l);
			return null;
		}
	}
}
