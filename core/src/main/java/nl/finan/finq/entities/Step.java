package nl.finan.finq.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "FINQ_STEP")
public class Step extends GenericEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "SCENARIO_ID")
    @JsonBackReference
    private StepContainer stepContainer;

    @Column(name = "TITLE", nullable = false)
    @LazyCollection(LazyCollectionOption.FALSE)
    private String title;


    public Step() {
    }

    public Step(String title) {
        this.title = title;
    }

    public StepContainer getStepContainer() {
        return stepContainer;
    }

    public void setStepContainer(StepContainer stepContainer) {
        this.stepContainer = stepContainer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String toStory() {
        return title;
    }
}
