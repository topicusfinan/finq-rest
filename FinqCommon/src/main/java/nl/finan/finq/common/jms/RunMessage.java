package nl.finan.finq.common.jms;

import nl.finan.finq.common.to.EnvironmentTO;

import java.io.Serializable;
import java.util.Map;

public class RunMessage implements Serializable
{
	private static final long serialVersionUID = 1L;

	private final Map<String, String> stories;
	private final EnvironmentTO environment;
	private final long runningProcessId;

	public RunMessage(Map<String, String> stories, EnvironmentTO environment, long runningProcessId)
	{
		this.stories = stories;
		this.environment = environment;
		this.runningProcessId = runningProcessId;
	}

	public Map<String, String> getStories()
	{
		return stories;
	}

	public EnvironmentTO getEnvironment()
	{
		return environment;
	}

	public long getRunningProcessId()
	{
		return runningProcessId;
	}
}
