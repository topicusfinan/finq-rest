package nl.finan.finq.runner;

import nl.finan.finq.common.jms.Queues;
import nl.finan.finq.common.to.EventType;
import nl.finan.finq.common.to.ProgressEvent;
import nl.finan.finq.common.to.SendEventTO;
import nl.finan.finq.common.to.StatusMessage;
import nl.finan.finq.common.to.TotalStatus;
import nl.finan.finq.entities.RunningStories;
import nl.finan.finq.entities.ScenarioLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;

@Stateless
public class StatusService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(StatusService.class);

    @Resource(mappedName = Queues.CONNECTION_FACTORY)
    private ConnectionFactory connectionFactory;

    @Resource(mappedName = Queues.STATUS_QUEUE)
    private Queue queue;

    public void sendProgress(Long reportId, ScenarioLog log)
    {
        acknowledgeQueue(reportId, new SendEventTO(EventType.PROGRESS, new ProgressEvent(log)));
    }

    public void sendProgress(Long reportId, RunningStories runningStories)
    {
        acknowledgeQueue(reportId, new SendEventTO(EventType.PROGRESS, new TotalStatus(runningStories)));
    }

    public void acknowledgeQueue(Long reportId, SendEventTO eventTO)
    {
        StatusMessage statusMessage = new StatusMessage(reportId, eventTO);
        try
        {
            try (Connection connection = connectionFactory.createConnection();
                 Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                 MessageProducer producer = session.createProducer(queue))
            {
                ObjectMessage message = session.createObjectMessage();
                message.setObject(statusMessage);
                producer.send(message);
            }
        }
        catch (JMSException e)
        {
            LOGGER.warn(e.getMessage());
        }
    }
}
