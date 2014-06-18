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

    @Column
    private String logs;

    public RunningStorysStatus getStatus() {
        return status;
    }

    public void setStatus(RunningStorysStatus status) {
        this.status = status;
    }

    public String getLogs() {
        if(logs == null){
            logs = new String();
        }
        return logs;
    }

    public void addToLog(String log){
        this.logs = getLogs();
        this.logs.concat(log);
    }

    public void setLogs(String logs) {
        this.logs = logs;
    }
}
