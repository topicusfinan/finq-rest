package nl.finan.finq.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "FINQ_STORY")
public class Story extends GenericEntity {

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "BOOK_ID", nullable = true)
    @JsonBackReference
    private Book book;

    @Column(name = "TITLE")
    private String title;

    @OneToMany(mappedBy = "story")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Scenario> scenarios;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public List<Scenario> getScenarios() {
        if (scenarios == null) {
            scenarios = new ArrayList<Scenario>();
        }
        return scenarios;
    }

    public void setScenarios(List<Scenario> scenarios) {
        this.scenarios = scenarios;
    }

    public String toStory() {
        StringBuilder builder = new StringBuilder();
        for (Scenario scenario : getScenarios()) {
            builder.append(System.lineSeparator());
            builder.append(scenario.toStory());
        }

        return builder.toString();
    }
}
