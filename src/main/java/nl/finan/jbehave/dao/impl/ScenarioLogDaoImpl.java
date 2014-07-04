package nl.finan.jbehave.dao.impl;

import nl.finan.jbehave.dao.ScenarioLogDao;
import nl.finan.jbehave.entities.ScenarioLog;

import javax.ejb.Stateless;

@Stateless
public class ScenarioLogDaoImpl extends DaoJPAImpl<ScenarioLog> implements ScenarioLogDao {

}
