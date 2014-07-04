package nl.finan.jbehave.dao.impl;

import nl.finan.jbehave.dao.RunningStoriesDao;
import nl.finan.jbehave.entities.RunningStories;

import javax.ejb.Stateless;

@Stateless
public class RunningStoriesDaoImpl extends DaoJPAImpl<RunningStories> implements RunningStoriesDao {
}
