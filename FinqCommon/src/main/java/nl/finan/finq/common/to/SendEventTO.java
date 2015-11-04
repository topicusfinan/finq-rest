package nl.finan.finq.common.to;

import java.io.Serializable;

public class SendEventTO implements Serializable
{

	private EventType event;
	private Serializable data;

	public SendEventTO(EventType event, Serializable log)
	{
		this.event = event;
		this.data = log;
	}

	public EventType getEvent()
	{
		return event;
	}

	public void setEvent(EventType eventType)
	{
		this.event = eventType;
	}

	public Object getData()
	{
		return data;
	}

	public void setData(Serializable data)
	{
		this.data = data;
	}
}
