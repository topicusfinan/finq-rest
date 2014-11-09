package nl.finan.finq.embeder;

import nl.eernie.jmoribus.configuration.Configuration;

import javax.ejb.Local;

@Local
public interface ConfigurationFactory {

    Configuration getConfiguration();
}
