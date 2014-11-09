package nl.finan.finq.dao.impl;

import nl.finan.finq.dao.RunningStoriesDao;
import nl.finan.finq.entities.LogStatus;
import nl.finan.finq.entities.RunningStories;

import javax.ejb.Stateless;
import java.util.List;

import static nl.finan.finq.dao.impl.NamedParameter.np;


@Stateless
public class RunningStoriesDaoImpl extends DaoJPAImpl<RunningStories> implements RunningStoriesDao {

    @Override
    public List<RunningStories> findByStatuses(List<LogStatus> statuses, Integer page, Integer size) {
        return list(RunningStories.QUERY_FIND_BY_STATUS, page, size, np("statuses", statuses));
    }

    @Override
    public Long countBySatuses(List<LogStatus> logStatuses) {
        return count(RunningStories.QUERY_COUNT_BY_STATUS, np("statuses", logStatuses));
    }
}
