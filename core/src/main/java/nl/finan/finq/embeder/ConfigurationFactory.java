package nl.finan.finq.embeder;

import nl.eernie.jmoribus.configuration.Configuration;

import javax.ejb.Local;
import javax.ejb.Stateless;

@Local
public interface ConfigurationFactory {

    Configuration getConfiguration(Long reportId);
}
