package nl.finan.jbehave.dao.impl;

import nl.finan.jbehave.dao.StoryLogDao;
import nl.finan.jbehave.entities.StoryLog;
import org.springframework.stereotype.Repository;

@Repository
public class StoryLogDaoImpl extends DaoJPAImpl<StoryLog> implements StoryLogDao {
}
