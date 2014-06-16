package nl.finan.jbehave.entities;


import org.hibernate.annotations.CollectionOfElements;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "RUNNING_STORIES")
public class RunningStories extends GenericEntity{

    @Enumerated(EnumType.STRING)
    @Column
    private RunningStorysStatus status;

    @CollectionOfElements
    @CollectionTable(name="RUNNING_LOGS", joinColumns=@JoinColumn(name="RUNNING_STORY_ID"))
    @Column(name="LOGS")
    private List<String> logs;

    public RunningStorysStatus getStatus() {
        return status;
    }

    public void setStatus(RunningStorysStatus status) {
        this.status = status;
    }

    public List<String> getLogs() {
        if(logs == null){
            logs = new ArrayList<String>();
        }
        return logs;
    }

    public void setLogs(List<String> logs) {
        this.logs = logs;
    }
}
