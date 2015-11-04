package nl.finan.finq.common.configuration;

import javax.ejb.Local;

@Local
public interface ConfigurationFactory
{

	FinqConfiguration getConfiguration();
}
