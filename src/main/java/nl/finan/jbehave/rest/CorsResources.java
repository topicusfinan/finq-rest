package nl.finan.jbehave.rest;

import org.apache.cxf.rs.security.cors.CorsHeaderConstants;
import org.apache.cxf.rs.security.cors.CrossOriginResourceSharing;
import org.apache.cxf.rs.security.cors.LocalPreflight;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

@CrossOriginResourceSharing(
        allowAllOrigins = true
)


@Repository
public class CorsResources {

    @Context
    private HttpHeaders headers;

    // This method will do a preflight check itself
    @OPTIONS
    @Path("/")
    @LocalPreflight
    public Response options() {

        return Response.ok()
                .header(CorsHeaderConstants.HEADER_AC_ALLOW_METHODS, "DELETE PUT")
                .header(CorsHeaderConstants.HEADER_AC_ALLOW_CREDENTIALS, "false")
                .header(CorsHeaderConstants.HEADER_AC_ALLOW_ORIGIN, "*")
                .build();

    }

}
