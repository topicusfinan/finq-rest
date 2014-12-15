package nl.finan.finq.configuration;

import nl.eernie.jmoribus.configuration.Configuration;
import nl.finan.finq.entities.Environment;

public abstract class FinqConfiguration implements Configuration {
    private Environment environment;

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
