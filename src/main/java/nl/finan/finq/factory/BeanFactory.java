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

    private static final String BEANS_FILE = "/beans.properties";

    private BeanFactory(){}

    private static String getFromProperties(String key){
        try {
            InputStream stream = BeanFactory.class.getResourceAsStream(BEANS_FILE);
            Properties properties = new Properties();
            properties.load(stream);
            if(!properties.containsKey(key)){
                LOGGER.error("Key {} could not be found in file {}",key,BEANS_FILE);
                return null;
            } else {
                return (String) properties.get(key);
            }

        } catch (FileNotFoundException e) {
            LOGGER.error("File {} couldn't be found.",BEANS_FILE);
            LOGGER.error("Stacktrace: {}",e);
        } catch (IOException e) {
            LOGGER.error("Something went wrong wile reading file {}.",BEANS_FILE);
            LOGGER.error("Stacktrace: {}",e);
        }
        return null;
    }


    public static <T> T getBean(Class<T> clazz) {
        String jndiName = getFromProperties(clazz.getSimpleName());
        try {
            @SuppressWarnings("unchecked")
            T bean = (T) new InitialContext().lookup(jndiName);
            return bean;
        } catch (NamingException e) {
            LOGGER.error("Couldn't cast to {}",clazz.getSimpleName());
        }
        return null;
    }
}
