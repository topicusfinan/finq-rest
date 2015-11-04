package nl.finan.finq.service;

import nl.finan.finq.common.converter.EnvironmentConverter;
import nl.finan.finq.common.jms.Queues;
import nl.finan.finq.common.jms.RunMessage;
import nl.finan.finq.dao.RunningStoriesDao;
import nl.finan.finq.dao.StoryDao;
import nl.finan.finq.entities.Book;
import nl.finan.finq.entities.Environment;
import nl.finan.finq.entities.LogStatus;
import nl.finan.finq.entities.RunningStories;
import nl.finan.finq.entities.Story;
import nl.finan.finq.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class RunnerService
{
	private static final Logger LOGGER = LoggerFactory.getLogger(RunnerService.class);

	@EJB
	private StoryDao storyDao;

	@EJB
	private RunningStoriesDao runningStoriesDao;

	@Resource(mappedName = Queues.CONNECTION_FACTORY)
	private ConnectionFactory connectionFactory;

	@Resource(mappedName = Queues.RUN_STORY_QUEUE)
	private Queue queue;

	public RunningStories run(Story story, User user, Environment environment)
	{
		return run(Collections.singletonList(story), user, environment);
	}

	public RunningStories run(Book book, User user, Environment environment)
	{
		return run(book.getStories(), user, environment);
	}

	public RunningStories run(List<Story> stories, User user, Environment environment)
	{
		RunningStories runningStories = new RunningStories();
		runningStories.setStatus(LogStatus.RUNNING);
		runningStories.setStartDate(new Date());
		runningStories.setStartedBy(user);
		runningStories.setEnvironment(environment);
		runningStoriesDao.persist(runningStories);

		acknowledgeQueue(stories, runningStories);

		return runningStories;
	}

	public void acknowledgeQueue(List<Story> stories, RunningStories runningStories)
	{
		RunMessage runMessage = createRunMessage(stories, runningStories);

		try
		{
			try (Connection connection = connectionFactory.createConnection();
				 Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
				 MessageProducer producer = session.createProducer(queue))
			{
				ObjectMessage message = session.createObjectMessage();
				message.setObject(runMessage);
				producer.send(message);
			}
		}
		catch (JMSException e)
		{
			LOGGER.warn(e.getMessage());
		}
	}

	private RunMessage createRunMessage(List<Story> stories, RunningStories runningStories)
	{
		Map<String, String> storyMap = new HashMap<>(stories.size());
		for (Story story : stories)
		{
			String key = story.getId() + "-" + runningStories.getId();
			storyMap.put(key, story.toStory());
		}
		return new RunMessage(storyMap, EnvironmentConverter.convert(runningStories.getEnvironment()), runningStories.getId());
	}
}
