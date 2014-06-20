package nl.finan.jbehave.rest;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.json.UTF8JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nl.finan.jbehave.dao.StoryDao;
import nl.finan.jbehave.entities.Scenario;
import nl.finan.jbehave.entities.Story;
import org.apache.cxf.rs.security.cors.CorsHeaderConstants;
import org.apache.cxf.rs.security.cors.CrossOriginResourceSharing;
import org.apache.cxf.rs.security.cors.LocalPreflight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

@CrossOriginResourceSharing(
        allowAllOrigins = true
)

@Path("stories")
@Repository
@Transactional
public class StoriesResources {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StoriesResources.class);


    @Autowired
    private StoryDao storyDao;


    @Context
    private HttpHeaders headers;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    //TODO: please refactor me, I want to return a List of Stories but this isn't posible becouse of the List<String> in the scenario... Dangit
    public String stories() throws IOException {

        List<Story> storiesList = storyDao.listAll();
        ObjectMapper mapper = new ObjectMapper();

        Writer writer = new StringWriter();
        mapper.writeValue(writer,storiesList);

        return writer.toString();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Story story(@PathParam("id") Long id){
    	return storyDao.find(id);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/scenarios")
    public List<Scenario> scenarios(@PathParam("id") Long id){
    	Story story = storyDao.find(id);
    	return story.getScenarios();    	
    }

    // This method will do a preflight check itself
    @OPTIONS
    @Path("/")
    @LocalPreflight
    public Response options() {
        String origin = headers.getRequestHeader("Origin").get(0);

            return Response.ok()
                    .header(CorsHeaderConstants.HEADER_AC_ALLOW_METHODS, "DELETE PUT")
                    .header(CorsHeaderConstants.HEADER_AC_ALLOW_CREDENTIALS, "false")
                    .header(CorsHeaderConstants.HEADER_AC_ALLOW_ORIGIN, "http://area51.mil:3333")
                    .build();

    }
    
}

