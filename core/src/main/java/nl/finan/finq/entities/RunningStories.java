package nl.finan.finq.entities;


import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "JBEHAVE_RUNNING_STORIES")
public class RunningStories extends GenericEntity{

    @Enumerated(EnumType.STRING)
    @Column
    private RunningStoriesStatus status;

    @OneToMany(mappedBy = "runningStory")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<StoryLog> logs = new ArrayList<StoryLog>();

    public RunningStoriesStatus getStatus() {
        return status;
    }

    public void setStatus(RunningStoriesStatus status) {
        this.status = status;
    }

    public List<StoryLog> getLogs() {
        return logs;
    }



}
