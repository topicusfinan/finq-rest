package nl.finan.finq.runner;

import nl.finan.finq.common.jms.Queues;
import nl.finan.finq.common.jms.RunMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.io.Serializable;

@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "destination", propertyValue = Queues.RUN_STORY_QUEUE),
    @ActivationConfigProperty(propertyName = "maxSession", propertyValue = "20")})
public class StoryRunnerMessageDrivenBean implements MessageListener
{

    private static final Logger LOGGER = LoggerFactory.getLogger(StoryRunnerMessageDrivenBean.class);

    @EJB
    private StoryRunner storyRunner;

    @Override
    public void onMessage(Message message)
    {
        try
        {
            if (message instanceof ObjectMessage)
            {
                ObjectMessage objectMessage = (ObjectMessage) message;
                Serializable object = objectMessage.getObject();
                if (object instanceof RunMessage)
                {
                    storyRunner.run((RunMessage) object);
                }
                else
                {
                    LOGGER.warn("Object " + object.getClass().getSimpleName() + " is not an instance of " + RunMessage.class.getSimpleName());
                }
            }
            else
            {
                LOGGER.warn("Message " + message.getClass().getSimpleName() + " is not an instance of " + ObjectMessage.class.getSimpleName());
            }
        }
        catch (JMSException e)
        {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
