package nl.finan.finq.entities;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Log extends GenericEntity {

    @Column
    private String log;

    @Enumerated(EnumType.STRING)
    @Column
    private RunningStoriesStatus status;

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public RunningStoriesStatus getStatus() {
        return status;
    }

    public void setStatus(RunningStoriesStatus status) {
        this.status = status;
    }
}
