package nl.finan.finq.entities;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "FINQ_RUNNING_STORIES")
public class RunningStories extends GenericEntity {

    @Enumerated(EnumType.STRING)
    @Column
    private LogStatus status;

    @OneToMany(mappedBy = "runningStory")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<StoryLog> logs = new ArrayList<StoryLog>();

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User startedBy;

    @ManyToOne
    @JoinColumn(name = "ENVIRONMENT_ID")
    private Environment environment;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "START_DATE")
    @JsonProperty("startedOn")
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "COMPLETE_DATE")
    @JsonProperty("competedOn")
    private Date completeDate;

    public LogStatus getStatus() {
        return status;
    }

    public void setStatus(LogStatus status) {
        this.status = status;
    }

    public List<StoryLog> getLogs() {
        return logs;
    }

    public User getStartedBy() {
        return startedBy;
    }

    public void setStartedBy(User startedBy) {
        this.startedBy = startedBy;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
    }
}
