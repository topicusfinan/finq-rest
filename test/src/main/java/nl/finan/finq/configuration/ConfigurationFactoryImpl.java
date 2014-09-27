package nl.finan.finq.configuration;

import nl.eernie.jmoribus.configuration.Configuration;
import nl.eernie.jmoribus.configuration.DefaultConfiguration;
import nl.finan.finq.embeder.ConfigurationFactory;
import nl.finan.finq.embeder.WebStoryReporter;
import nl.finan.finq.factory.BeanFactory;
import nl.finan.finq.steps.Step;

import javax.ejb.Stateless;
import java.util.Arrays;

@Stateless
public class ConfigurationFactoryImpl implements ConfigurationFactory {
    @Override
    public Configuration getConfiguration(Long reportId) {
        Configuration configuration = new DefaultConfiguration();
        configuration.addSteps(Arrays.<Object>asList(new Step()));
        WebStoryReporter reporter = BeanFactory.getBean(WebStoryReporter.class);
        reporter.init(reportId);
        configuration.addReporter(reporter);
        return configuration;
    }
}
