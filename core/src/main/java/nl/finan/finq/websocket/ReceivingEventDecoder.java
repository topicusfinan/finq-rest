package nl.finan.finq.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.finan.finq.websocket.to.ReceivingEventTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.IOException;


public class ReceivingEventDecoder implements Decoder.Text<ReceivingEventTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReceivingEventDecoder.class);

    @Override
    public ReceivingEventTO decode(String s) throws DecodeException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(s, ReceivingEventTO.class);
        } catch (IOException e) {
            LOGGER.error("IOException caught while deserialization ["+s+"]", e);
            return null;
        }
    }

    @Override
    public boolean willDecode(String s) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(s);
            return jsonNode.has("event");
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public void destroy() {

    }
}
