package nl.finan.jbehave.entities;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "JBEHAVE_RUNNING_STORIES")
public class RunningStories extends GenericEntity{

    @Enumerated(EnumType.STRING)
    @Column
    private RunningStorysStatus status;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="JBEHAVE_LOGS", joinColumns = @JoinColumn(name="RUNNING_STORIES_ID"))
    @Column(name = "LOGS")
    private List<String> logs = new ArrayList<String>();

    public RunningStorysStatus getStatus() {
        return status;
    }

    public void setStatus(RunningStorysStatus status) {
        this.status = status;
    }

    public List<String> getLogs() {
        return logs;
    }

    public void addToLog(String log){
        this.logs.add(log);
    }

}
