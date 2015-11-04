package nl.finan.finq.common.to;

import java.io.Serializable;

public class StatusMessage implements Serializable
{
	private final Long reportId;
	private final SendEventTO eventTO;

	public StatusMessage(Long reportId, SendEventTO eventTO)
	{
		this.reportId = reportId;
		this.eventTO = eventTO;
	}

	public SendEventTO getEventTO()
	{
		return eventTO;
	}

	public Long getReportId()
	{
		return reportId;
	}
}
