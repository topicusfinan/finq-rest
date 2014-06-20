package nl.finan.jbehave.dao;

import nl.finan.jbehave.entities.Scenario;
import org.springframework.stereotype.Repository;

@Repository
public class ScenarioDaoImpl extends DaoJPAImpl<Scenario> implements
		ScenarioDao {

}
