package nl.finan.finq.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "FINQ_SET")
public class Set extends GenericEntity {

    @Column(name = "NAME")
    private String name;

    @ManyToMany
    @JoinTable(
        name = "FINQ_STORY_SET",
        joinColumns = {@JoinColumn(name = "SET_ID", referencedColumnName = "ID")},
        inverseJoinColumns = {@JoinColumn(name = "STORY_ID", referencedColumnName = "ID")})
    @JsonBackReference
    private List<Story> stories = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Story> getStories() {
        return stories;
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
    }
}
