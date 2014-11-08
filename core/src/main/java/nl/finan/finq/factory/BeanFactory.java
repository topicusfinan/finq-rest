package nl.finan.finq.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class BeanFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeanFactory.class);

    private BeanFactory() {
    }

    public static <T> T getBean(String jndiName) {
        try {
            @SuppressWarnings("unchecked")
            T bean = (T) new InitialContext().lookup(jndiName);
            return bean;
        } catch (NamingException e) {
            LOGGER.error("Could not find bean with jndi {}",jndiName);
        }
        return null;
    }
}
