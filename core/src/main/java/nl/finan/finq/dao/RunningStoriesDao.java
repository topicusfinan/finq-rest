package nl.finan.finq.dao;

import nl.finan.finq.entities.LogStatus;
import nl.finan.finq.entities.RunningStories;

import javax.ejb.Local;
import java.util.List;

@Local
public interface RunningStoriesDao extends Dao<RunningStories> {
    List<RunningStories> findByStatuses(List<LogStatus> statuses, Integer page, Integer size);
}
