package nl.finan.finq.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;

@Entity
@Table(name = "FINQ_STEP")
public class Step extends GenericEntity{

    @ManyToOne(optional = false)
    @JoinColumn(name = "SCENARIO_ID")
    @JsonBackReference
    private Scenario scenario;

    @Column(name = "TITLE", nullable = false)
    @LazyCollection(LazyCollectionOption.FALSE)
    private String title;

    @Transient
    private String template;

    public Step() {
    }

    public Step(String title) {
        this.title = title;
    }

    public Step(String title, String template) {
        this.title = title;
        this.template = template;
    }

    public Scenario getScenario() {
        return scenario;
    }

    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String toStory(){
        return title;
    }
}
