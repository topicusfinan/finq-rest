package nl.finan.jbehave.dao.impl;


import nl.finan.jbehave.dao.StoryDao;
import nl.finan.jbehave.entities.Story;
import org.springframework.stereotype.Repository;

@Repository
public class StoryDaoImpl extends DaoJPAImpl<Story> implements StoryDao {


}
