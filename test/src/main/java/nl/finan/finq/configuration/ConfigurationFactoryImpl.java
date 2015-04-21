package nl.finan.finq.configuration;

import nl.eernie.jmoribus.reporter.Reporter;
import nl.finan.finq.common.configuration.ConfigurationFactory;
import nl.finan.finq.common.configuration.FinqConfiguration;
import nl.finan.finq.steps.Step;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.Arrays;

@Stateless
public class ConfigurationFactoryImpl implements ConfigurationFactory
{

    @EJB
    private Reporter reporter;

    @Override
    public FinqConfiguration getConfiguration()
    {
        FinqConfiguration configuration = new JmoribusConfiguration();
        configuration.addSteps(Arrays.<Object>asList(new Step()));
        configuration.addReporter(reporter);
        return configuration;
    }
}
