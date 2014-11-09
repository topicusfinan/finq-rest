package nl.finan.finq.service;

import nl.finan.finq.dao.RunningStoriesDao;
import nl.finan.finq.dao.StoryDao;
import nl.finan.finq.embeder.Queues;
import nl.finan.finq.embeder.RunMessage;
import nl.finan.finq.entities.Book;
import nl.finan.finq.entities.LogStatus;
import nl.finan.finq.entities.RunningStories;
import nl.finan.finq.entities.Story;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jms.*;
import java.util.Arrays;
import java.util.List;

@Stateless
public class RunnerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RunnerService.class);

    @EJB
    private StoryDao storyDao;

    @EJB
    private RunningStoriesDao runningStoriesDao;

    @Resource(mappedName = Queues.CONNECTION_FACTORY)
    private ConnectionFactory connectionFactory;

    @Resource(mappedName = Queues.RUN_STORY_QUEUE)
    private Queue queue;

    public RunningStories run(Story story) {
        return run(Arrays.asList(story));
    }

    public RunningStories run(Book book) {
        return run(book.getStories());
    }

    public RunningStories run(List<Story> stories) {
        RunningStories runningStories = new RunningStories();
        runningStories.setStatus(LogStatus.RUNNING);
        runningStoriesDao.persist(runningStories);

        acknowledgeQueue(stories, runningStories);

        return runningStories;
    }

    public void acknowledgeQueue(List<Story> stories, RunningStories runningStories) {
        RunMessage runMessage = new RunMessage(stories, runningStories.getId());
        try {
            Connection connection = connectionFactory.createConnection();
            try {
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                MessageProducer producer = session.createProducer(queue);
                ObjectMessage message = session.createObjectMessage();
                message.setObject(runMessage);

                producer.send(message);

            } finally {
                connection.close();
            }
        } catch (JMSException e) {
            LOGGER.warn(e.getMessage());
        }
    }
}
