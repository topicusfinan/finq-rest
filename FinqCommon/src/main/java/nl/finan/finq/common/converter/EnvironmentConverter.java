package nl.finan.finq.common.converter;

import nl.finan.finq.common.to.EnvironmentTO;
import nl.finan.finq.entities.Environment;

public final class EnvironmentConverter {
    private EnvironmentConverter() {
    }

    public static EnvironmentTO convert(Environment source) {
        if (source == null) {
            return null;
        }
        EnvironmentTO target = new EnvironmentTO();
        target.setAddress(source.getAddress());
        target.setName(source.getName());
        return target;
    }
}
