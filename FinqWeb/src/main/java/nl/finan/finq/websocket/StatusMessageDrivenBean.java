package nl.finan.finq.websocket;


import nl.finan.finq.common.jms.Queues;
import nl.finan.finq.common.to.StatusMessage;
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
    @ActivationConfigProperty(propertyName = "destination", propertyValue = Queues.STATUS_QUEUE),
    @ActivationConfigProperty(propertyName = "maxSession", propertyValue = "1")})
public class StatusMessageDrivenBean implements MessageListener
{
    private static final Logger LOGGER = LoggerFactory.getLogger(StatusMessageDrivenBean.class);

    @EJB
    private StatusWebSocket statusWebSocket;

    @Override
    public void onMessage(Message message)
    {
        try
        {
            if (message instanceof ObjectMessage)
            {
                ObjectMessage objectMessage = (ObjectMessage) message;
                Serializable object = objectMessage.getObject();
                if (object instanceof StatusMessage)
                {
                    StatusMessage statusMessage = (StatusMessage) object;
                    statusWebSocket.sendProgress(statusMessage.getReportId(), statusMessage.getEventTO());
                }
                else
                {
                    LOGGER.warn("Object " + object.getClass().getSimpleName() + " is not an instance of " + StatusMessage.class.getSimpleName());
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

