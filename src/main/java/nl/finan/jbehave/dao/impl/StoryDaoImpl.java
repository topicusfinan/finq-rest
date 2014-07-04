package nl.finan.jbehave.dao.impl;


import nl.finan.jbehave.dao.StoryDao;
import nl.finan.jbehave.entities.Story;

import javax.ejb.Stateless;

@Stateless
public class StoryDaoImpl extends DaoJPAImpl<Story> implements StoryDao {


}
