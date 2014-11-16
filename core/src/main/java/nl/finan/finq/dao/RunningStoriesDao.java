package nl.finan.finq.dao;

import nl.finan.finq.entities.LogStatus;
import nl.finan.finq.entities.RunningStories;

import javax.ejb.Local;
import java.util.Date;
import java.util.List;

@Local
public interface RunningStoriesDao extends Dao<RunningStories> {
    List<RunningStories> findByDateAndStatuses(List<LogStatus> statuses, Date since, Date until, Integer page, Integer size);

    Long countByDateAndStatuses(List<LogStatus> logStatuses, Date since, Date until);
}
