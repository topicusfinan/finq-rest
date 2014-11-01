package nl.finan.finq.entities;


import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "FINQ_RUNNING_STORIES")
@NamedQueries({
    @NamedQuery(name = RunningStories.QUERY_FIND_BY_STATUS,
        query = "select rs from RunningStories rs where rs.status in (:statuses)")
})
public class RunningStories extends GenericEntity {

    public static final String QUERY_FIND_BY_STATUS = "RunningStories.findByStatus";

    @Enumerated(EnumType.STRING)
    @Column
    private LogStatus status;

    @OneToMany(mappedBy = "runningStory")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<StoryLog> logs = new ArrayList<StoryLog>();

    public LogStatus getStatus() {
        return status;
    }

    public void setStatus(LogStatus status) {
        this.status = status;
    }

    public List<StoryLog> getLogs() {
        return logs;
    }


}
