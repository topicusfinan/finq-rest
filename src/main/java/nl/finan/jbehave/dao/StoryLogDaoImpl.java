package nl.finan.jbehave.dao;

import nl.finan.jbehave.entities.StoryLog;
import org.springframework.stereotype.Repository;

@Repository
public class StoryLogDaoImpl extends DaoJPAImpl<StoryLog> implements StoryLogDao{
}
