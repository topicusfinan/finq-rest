package nl.finan.jbehave.dao.impl;

import nl.finan.jbehave.dao.ScenarioLogDao;
import nl.finan.jbehave.entities.ScenarioLog;
import org.springframework.stereotype.Repository;

@Repository
public class ScenarioLogDaoImpl extends DaoJPAImpl<ScenarioLog> implements ScenarioLogDao {

}
