package nl.finan.jbehave.rest;

import nl.finan.jbehave.rest.embeder.FinanEmbedder;
import nl.finan.jbehave.rest.utils.StoryUtils;
import org.jbehave.core.embedder.StoryManager;
import org.jbehave.core.model.Story;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/stories")

public class HelloWorld {

    @Autowired
    private FinanEmbedder embedder;

    @GET
    @Produces("application/json")
    public List<String> ping(@PathParam("input") String input) {

        StoryManager manager = embedder.storyManager();

        List<Story> stories = StoryUtils.getStoriesFromPath(embedder.storyPaths(), manager);
        List<String> storyTitles = new ArrayList<String>();
        for (Story s : stories) {
            storyTitles.add(s.getScenarios().get(0).getTitle());
        }


        return storyTitles;
    }

    @POST
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/jsonBean")
    public Response modifyJson(JsonBean input) {
        input.setVal2(input.getVal1());
        return Response.ok().entity(input).build();
    }
}

