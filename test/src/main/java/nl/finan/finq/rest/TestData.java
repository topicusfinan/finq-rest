package nl.finan.finq.rest;

import nl.finan.finq.dao.BundleDao;
import nl.finan.finq.dao.ScenarioDao;
import nl.finan.finq.dao.StoryDao;
import nl.finan.finq.entities.Book;
import nl.finan.finq.entities.Scenario;
import nl.finan.finq.entities.Story;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("testdata")
@Stateless
public class TestData {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestData.class);

    @EJB
    private StoryDao storyDao;

    @EJB
    private BundleDao projectDao;

    @EJB
    private ScenarioDao scenarioDao;

    @GET
    @Transactional
    public Response generateTestDate() {
        Book b = new Book();
        b.setDescription("Test Bundle");
        b.setTitle("Test Bundle");
        projectDao.persist(b);

        for (int i = 0; i < 2; i++) {
            Story s = new Story();
            s.setName("story " + i);
            s.setBook(b);
            storyDao.persist(s);
            LOGGER.info("Story: {}", s.getName());
            for (int x = 0; x < 2; x++) {
                Scenario sc = new Scenario();
                sc.setTitle("Scenario Title " + i + "-" + x);
                sc.getSteps().add("Given this is a given step");
                sc.getSteps().add("When a When step has been run");
                sc.getSteps().add("Then a result must been shown");
                sc.setStory(s);
                scenarioDao.persist(sc);
            }
        }
        return Response.temporaryRedirect(URI.create("bundles")).build();
    }
}
