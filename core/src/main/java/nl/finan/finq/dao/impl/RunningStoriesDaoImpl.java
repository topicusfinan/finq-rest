package nl.finan.finq.dao.impl;

import nl.finan.finq.dao.RunningStoriesDao;
import nl.finan.finq.entities.LogStatus;
import nl.finan.finq.entities.RunningStories;
import nl.finan.finq.entities.RunningStories_;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;


@Stateless
public class RunningStoriesDaoImpl extends DaoJPAImpl<RunningStories> implements RunningStoriesDao {

    @Override
    public List<RunningStories> findByDateAndStatuses(List<LogStatus> statuses, Date since, Date until, Integer page, Integer size) {
        return queryOrderByStartDate().pageSelect(whereByDateAndStatuses(statuses, since, until), page, size);
    }

    @Override
    public Long countByDateAndStatuses(List<LogStatus> logStatuses, Date since, Date until) {
        return queryOrderByStartDate().count(whereByDateAndStatuses(logStatuses, since, until));
    }

    private WhereBuilder<RunningStories> whereByDateAndStatuses(final List<LogStatus> logStatuses, final Date since, final Date until) {
        return (builder, root) -> {
            Predicate where = builder.conjunction();
            if (logStatuses != null) {
                where = builder.and(where, root.get(RunningStories_.status).in(logStatuses));
            }
            if (since != null) {
                where = builder.and(where, builder.greaterThanOrEqualTo(root.get(RunningStories_.startDate), since));
            }
            if (until != null) {
                where = builder.and(where, builder.lessThanOrEqualTo(root.get(RunningStories_.startDate), until));
            }
            return where;
        };
    }

    private GenericQuery queryOrderByStartDate() {
        return new GenericQuery() {
            @Override
            protected void buildOrderBy(CriteriaBuilder builder, Root<RunningStories> root, List<Order> orderBy) {
                orderBy.add(builder.desc(root.get(RunningStories_.startDate)));
            }
        };
    }
}
