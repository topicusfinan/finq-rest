package nl.finan.finq.dao.impl;

import nl.finan.finq.dao.RunningStoriesDao;
import nl.finan.finq.entities.RunningStories;

import javax.ejb.Stateless;

@Stateless
public class RunningStoriesDaoImpl extends DaoJPAImpl<RunningStories> implements RunningStoriesDao {
}
