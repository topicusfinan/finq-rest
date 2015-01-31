package nl.finan.finq.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
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

    @OneToOne(mappedBy = "story")
    private Prologue prologue;

    @OneToMany(mappedBy = "story", cascade = javax.persistence.CascadeType.PERSIST)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Scenario> scenarios;

    @ManyToMany(mappedBy = "stories")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Set> sets = new ArrayList<>();

    @ManyToMany(mappedBy = "stories")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Tag> tags = new ArrayList<>();

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

    public Prologue getPrologue() {
        return prologue;
    }

    public void setPrologue(Prologue prologue) {
        this.prologue = prologue;
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

    public List<Set> getSets() {
        return sets;
    }

    public void setSets(List<Set> sets) {
        this.sets = sets;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public String toStory() {
        StringBuilder builder = new StringBuilder();
        builder.append("Feature: ").append(title);
        if (prologue != null) {
            builder.append(prologue.toStory());
        }
        for (Scenario scenario : getScenarios()) {
            builder.append(System.lineSeparator());
            builder.append(scenario.toStory());
        }

        return builder.toString();
    }
}
