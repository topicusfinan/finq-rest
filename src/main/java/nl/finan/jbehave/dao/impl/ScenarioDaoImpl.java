package nl.finan.jbehave.dao.impl;

import nl.finan.jbehave.dao.ScenarioDao;
import nl.finan.jbehave.entities.Scenario;

import javax.ejb.Stateless;

@Stateless
public class ScenarioDaoImpl extends DaoJPAImpl<Scenario> implements
        ScenarioDao {

}
