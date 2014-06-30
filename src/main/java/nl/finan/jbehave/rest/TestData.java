package nl.finan.jbehave.rest;

import nl.finan.jbehave.dao.ProjectDao;
import nl.finan.jbehave.dao.ScenarioDao;
import nl.finan.jbehave.dao.StoryDao;
import nl.finan.jbehave.entities.Project;
import nl.finan.jbehave.entities.Scenario;
import nl.finan.jbehave.entities.Story;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("testdata")
@Stateless
public class TestData {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TestData.class);
	
	@EJB
	private StoryDao storyDao;
	
	@EJB
	private ProjectDao projectDao;
	
	@EJB
	private ScenarioDao scenarioDao;
	
	@GET
	@Transactional
	public Response generateTestDate(){
		Project p = new Project();
		p.setDescription("Test Project");
		p.setName("Test projectuh");
		projectDao.persist(p);
		
		for(int i = 0; i<2; i++){
			Story s = new Story();
			s.setName("story "+i);
			s.setProject(p);
			storyDao.persist(s);
			LOGGER.info("Story: {}",s.getName());
			for(int x = 0; x<2; x++){
				Scenario sc = new Scenario();
                sc.setTitle("Scenario Title "+i+"-"+x);
                sc.getSteps().add("Given this is a given step");
                sc.getSteps().add("When a When step has been run");
                sc.getSteps().add("Then a result must been shown");
				sc.setStory(s);
				scenarioDao.persist(sc);
			}
		}
		return Response.ok().build();
	}
}
