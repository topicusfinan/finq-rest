package nl.finan.jbehave.dao;

import nl.finan.jbehave.entities.RunningStories;
import org.springframework.stereotype.Repository;

@Repository
public class RunningStoriesDaoImpl extends DaoJPAImpl<RunningStories> implements RunningStoriesDao {
}
