package nl.finan.finq.rest;

import nl.finan.finq.common.to.UserTO;
import nl.finan.finq.dao.ApplicationDao;
import nl.finan.finq.dao.BookDao;
import nl.finan.finq.dao.EnvironmentDao;
import nl.finan.finq.dao.ScenarioDao;
import nl.finan.finq.dao.SetDao;
import nl.finan.finq.dao.StepDao;
import nl.finan.finq.dao.StoryDao;
import nl.finan.finq.dao.TagDao;
import nl.finan.finq.dao.UserDao;
import nl.finan.finq.entities.Application;
import nl.finan.finq.entities.Book;
import nl.finan.finq.entities.Environment;
import nl.finan.finq.entities.Scenario;
import nl.finan.finq.entities.Set;
import nl.finan.finq.entities.Step;
import nl.finan.finq.entities.Story;
import nl.finan.finq.entities.Tag;
import nl.finan.finq.service.UserService;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("testdata")
@Stateless
public class TestData
{

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

	@EJB
	private UserDao userDao;

	@EJB
	private UserService userService;

	@GET
	@Transactional
	public Response generateTestDate()
	{
		if (projectDao.listAll().isEmpty())
		{
			Book b = new Book();
			b.setTitle("Test Bundle");
			projectDao.persist(b);

			for (int i = 0; i < 2; i++)
			{
				Story s = new Story();
				s.setTitle("story " + i);
				s.setBook(b);
				storyDao.persist(s);
				addTags(s);
				addSets(s);
				b.getStories().add(s);

				for (int x = 0; x < 2; x++)
				{
					Scenario sc = new Scenario();
					sc.setTitle("Scenario Title " + i + "-" + x);
					sc.setStory(s);
					scenarioDao.persist(sc);
					s.getScenarios().add(sc);
					addStep(sc, "Given this is a given step");
					addStep(sc, "When a When step has been run");
					addStep(sc, "Then a result must been shown");
				}
			}
		}

		if (applicationDao.listAll().isEmpty())
		{
			Application application = new Application();
			application.setAuthenticate(true);
			application.setSubject("Book store");
			application.setTitle("Finq app");
			applicationDao.persist(application);
		}

		if (environmentDao.listAll().isEmpty())
		{
			Environment environment = new Environment();
			environment.setName("Stalone");
			environment.setAddress("http://stalone.local");
			environmentDao.persist(environment);

			Environment mattdamon = new Environment();
			mattdamon.setAddress("http://mattdamon.local");
			mattdamon.setName("MattDamon");
			environmentDao.persist(mattdamon);
		}

		if (userDao.listAll().isEmpty())
		{
			UserTO user = new UserTO();
			user.setFirstname("John");
			user.setLastname("Doe");
			user.setEmail("JohnDoe");
			user.setPassword("test");
			userService.createUser(user);
		}

		return Response.temporaryRedirect(URI.create("books")).build();
	}

	private void addTags(Story s)
	{
		Tag tag = new Tag();
		tag.setaKey("test");
		tag.setaValue("Test");
		tag.getStories().add(s);
		s.getTags().add(tag);
		tagDao.persist(tag);
	}

	private void addSets(Story s)
	{
		Set set = new Set();
		set.setName("Regressie");
		set.getStories().add(s);
		s.getSets().add(set);
		setDao.persist(set);
	}

	private void addStep(Scenario sc, String s)
	{
		Step step = new Step();
		step.setScenario(sc);
		step.setTitle(s);
		sc.getSteps().add(step);
		stepDao.persist(step);
	}
}
