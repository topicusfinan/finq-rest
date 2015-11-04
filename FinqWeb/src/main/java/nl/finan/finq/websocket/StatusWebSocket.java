package nl.finan.finq.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.finan.finq.common.to.EventType;
import nl.finan.finq.common.to.SendEventTO;
import nl.finan.finq.common.to.TotalStatus;
import nl.finan.finq.dao.RunningStoriesDao;
import nl.finan.finq.entities.RunningStories;
import nl.finan.finq.websocket.to.ReceivingEventTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

@ServerEndpoint(value = "/api/status", decoders = ReceivingEventDecoder.class)
@Stateless
public class StatusWebSocket
{

	private static final Logger LOGGER = LoggerFactory.getLogger(StatusWebSocket.class);

	@EJB
	private RunningStoriesDao runningStoriesDao;

	@EJB
	private OpenConnections openConnections;

	@OnMessage
	public void message(Session session, ReceivingEventTO event) throws IOException
	{
		switch (event.getEvent())
		{
		case SUBSCRIBE:
			subscribe(session, event.getData().getRun());
			break;
		case UNSUBSCRIBE:
			unsubscribe(session, event.getData().getRun());
			break;
		default:
			throw new IllegalArgumentException();
		}
	}

	@OnClose
	public void close(Session session)
	{
		openConnections.removeSession(session);
	}

	public void sendStatus(RunningStories runningStories)
	{
		if (openConnections.containsKey(runningStories.getId()))
		{
			for (Session session : openConnections.get(runningStories.getId()))
			{
				session.getAsyncRemote().sendText(toJson(new SendEventTO(EventType.GIST, new TotalStatus(runningStories))));
			}
		}
	}

	public void sendProgress(Long reportId, SendEventTO sendEventTO)
	{
		if (openConnections.containsKey(reportId))
		{
			for (Session session : openConnections.get(reportId))
			{
				session.getAsyncRemote().sendText(toJson(sendEventTO));
			}
		}
	}

	private void unsubscribe(Session session, Long reportId)
	{
		openConnections.removeFromConnectionMap(reportId, session);
	}

	private void subscribe(Session session, Long reportId)
	{
		RunningStories runningStories = runningStoriesDao.find(reportId);
		if (runningStories != null)
		{
			openConnections.add(reportId, session);
			session.getAsyncRemote().sendText(toJson(new SendEventTO(EventType.GIST, new TotalStatus(runningStories))));
		}
		else
		{
			session.getAsyncRemote().sendText("Could not find the report you're subscribing too.");
		}
	}

	private String toJson(Object object)
	{
		try
		{
			ObjectMapper mapper = new ObjectMapper();

			Writer writer = new StringWriter();
			mapper.writeValue(writer, object);

			return writer.toString();
		}
		catch (IOException e)
		{
			LOGGER.error("Something went wrong serializing the object, returning null", e);
		}
		return null;
	}
}
