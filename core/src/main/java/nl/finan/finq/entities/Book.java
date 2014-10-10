package nl.finan.finq.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "FINQ_BOOK")
public class Book extends GenericEntity {

    @Column(name = "TITLE")
    private String title;

    @OneToMany(mappedBy = "book", fetch = FetchType.EAGER)
    private List<Story> stories;

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public List<Story> getStories() {
        if(stories == null){
            stories = new ArrayList<>();
        }
        return stories;
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
    }

}
