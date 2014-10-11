package nl.finan.finq.rest;

import nl.finan.finq.dao.*;
import nl.finan.finq.entities.*;

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

    @EJB
    private StoryDao storyDao;

    @EJB
    private BookDao projectDao;

    @EJB
    private ScenarioDao scenarioDao;

    @EJB
    private StepDao stepDao;

    @EJB
    private ApplicationDao applicationDao;

    @EJB
    private SetDao setDao;

    @EJB
    private TagDao tagDao;

    @EJB
    private EnvironmentDao environmentDao;

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
            storyDao.persist(s);
            addTags(s);
            addSets(s);
            b.getStories().add(s);

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

        if(applicationDao.listAll().isEmpty()){
            Application application = new Application();
            application.setAuthenticate(false);
            application.setSubject("Book store");
            application.setTitle("Finq app");
            applicationDao.persist(application);
        }

        if(environmentDao.listAll().isEmpty()){
            Environment environment = new Environment();
            environment.setName("Stalone");
            environment.setAddress("http://stalone.local");
            environmentDao.persist(environment);

            Environment mattdamon = new Environment();
            mattdamon.setAddress("http://mattdamon.local");
            mattdamon.setName("MattDamon");
            environmentDao.persist(mattdamon);
        }

        return Response.temporaryRedirect(URI.create("books")).build();
    }

    private void addTags(Story s) {
        Tag tag = new Tag();
        tag.setaKey("test");
        tag.setaValue("Test");
        tag.getStories().add(s);
        s.getTags().add(tag);
        tagDao.persist(tag);
    }

    private void addSets(Story s) {
        Set set = new Set();
        set.setName("Regressie");
        set.getStories().add(s);
        s.getSets().add(set);
        setDao.persist(set);
    }

    private void addStep(Scenario sc, String s) {
        Step step = new Step();
        step.setScenario(sc);
        step.setTitle(s);
        sc.getSteps().add(step);
        stepDao.persist(step);
    }
}
