package nl.finan.finq.embeder;

import nl.eernie.jmoribus.configuration.Configuration;
import nl.finan.finq.configuration.FinqConfiguration;

import javax.ejb.Local;

@Local
public interface ConfigurationFactory {

    FinqConfiguration getConfiguration();
}
