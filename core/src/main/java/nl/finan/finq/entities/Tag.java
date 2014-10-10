package nl.finan.finq.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "FINQ_TAG")
public class Tag extends GenericEntity {

    @Column(name="KEY")
    private String key;

    @Column(name="VALUE")
    private String value;

    @ManyToMany
    @JoinTable(
        name="FINQ_STORY_TAG",
        joinColumns={@JoinColumn(name="TAG_ID", referencedColumnName="ID")},
        inverseJoinColumns={@JoinColumn(name="STORY_ID", referencedColumnName="ID")})
    @JsonBackReference
    private List<Story> stories = new ArrayList<>();

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<Story> getStories() {
        return stories;
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
    }
}
