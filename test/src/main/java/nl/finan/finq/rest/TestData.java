package nl.finan.finq.rest;

import nl.finan.finq.dao.BookDao;
import nl.finan.finq.dao.ScenarioDao;
import nl.finan.finq.dao.StepDao;
import nl.finan.finq.dao.StoryDao;
import nl.finan.finq.entities.Book;
import nl.finan.finq.entities.Scenario;
import nl.finan.finq.entities.Step;
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
    private BookDao projectDao;

    @EJB
    private ScenarioDao scenarioDao;

    @EJB
    private StepDao stepDao;

    @GET
    @Transactional
    public Response generateTestDate() {
        Book b = new Book();
        b.setTitle("Test Bundle");
        projectDao.persist(b);

        for (int i = 0; i < 2; i++) {
            Story s = new Story();
            s.setTitle("story " + i);
            s.setBook(b);
            b.getStories().add(s);
            storyDao.persist(s);
            LOGGER.info("Story: {}", s.getTitle());
            for (int x = 0; x < 2; x++) {
                Scenario sc = new Scenario();
                sc.setTitle("Scenario Title " + i + "-" + x);
                sc.setStory(s);
                scenarioDao.persist(sc);
                s.getScenarios().add(sc);
                addStep(sc, "Given this is a given step");
                addStep(sc, "When a When step has been run");
                addStep(sc,"Then a result must been shown");
            }
        }
        return Response.temporaryRedirect(URI.create("books")).build();
    }

    private void addStep(Scenario sc, String s) {
        Step step = new Step();
        step.setScenario(sc);
        step.setTitle(s);
        sc.getSteps().add(step);
        stepDao.persist(step);
    }
}
