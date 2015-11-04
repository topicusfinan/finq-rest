package nl.finan.finq.configuration;

import nl.finan.finq.common.configuration.ConfigurationFactory;
import nl.finan.finq.common.configuration.FinqConfiguration;
import nl.finan.finq.runner.WebStoryReporter;
import nl.finan.finq.steps.Step;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.Collections;

@Stateless
public class ConfigurationFactoryImpl implements ConfigurationFactory
{
	@EJB
	private WebStoryReporter reporter;

	@Override
	public FinqConfiguration getConfiguration()
	{
		FinqConfiguration configuration = new JmoribusConfiguration();
		configuration.addSteps(Collections.<Object>singletonList(new Step()));
		configuration.addReporter(reporter);
		return configuration;
	}
}
