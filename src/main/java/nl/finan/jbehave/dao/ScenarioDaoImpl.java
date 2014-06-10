package nl.finan.jbehave.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import nl.finan.jbehave.entities.Scenario;

@Repository
public class ScenarioDaoImpl extends DaoJPAImpl<Scenario> implements
		ScenarioDao {

}
