package nl.finan.finq.common.configuration;

import nl.eernie.jmoribus.configuration.Configuration;
import nl.finan.finq.common.to.EnvironmentTO;

public abstract class FinqConfiguration implements Configuration
{
	private EnvironmentTO environment;

	public EnvironmentTO getEnvironment()
	{
		return environment;
	}

	public void setEnvironment(EnvironmentTO environment)
	{
		this.environment = environment;
	}
}
