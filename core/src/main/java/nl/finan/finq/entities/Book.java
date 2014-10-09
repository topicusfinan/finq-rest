package nl.finan.finq.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "FINQ_BUNDLE")
public class Book extends GenericEntity {

    @Column(name = "TITLE")
    private String title;

    @OneToMany(mappedBy = "bundle", fetch = FetchType.EAGER)
    private List<Story> stories;

    @ManyToMany(mappedBy = "bundles")
    @JsonBackReference
    private List<Collection> collections;

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public List<Story> getStories() {
        return stories;
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
    }

    public List<Collection> getCollections() {
        return collections;
    }

    public void setCollections(List<Collection> collections) {
        this.collections = collections;
    }
}
