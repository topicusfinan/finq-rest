package nl.finan.finq.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "FINQ_TAG")
public class Tag extends GenericEntity {

    @Column(name="aKEY")
    private String aKey;

    @Column(name="aVALUE")
    private String aValue;

    @ManyToMany
    @JoinTable(
        name="FINQ_STORY_TAG",
        joinColumns={@JoinColumn(name="TAG_ID", referencedColumnName="ID")},
        inverseJoinColumns={@JoinColumn(name="STORY_ID", referencedColumnName="ID")})
    @JsonBackReference
    private List<Story> stories = new ArrayList<>();

    public String getaKey() {
        return aKey;
    }

    public void setaKey(String aKey) {
        this.aKey = aKey;
    }

    public String getAValue() {
        return aValue;
    }

    public void setAValue(String value) {
        this.aValue = value;
    }

    public List<Story> getStories() {
        return stories;
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
    }
}
