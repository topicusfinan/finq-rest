package nl.finan.finq.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;


@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@Table(name = "FINQ_STEP_CONTAINER")
public class StepContainer extends GenericEntity{

    @OneToMany(mappedBy = "stepContainer", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<Step> steps;

    public List<Step> getSteps() {
        if (steps == null) {
            steps = new ArrayList<>();
        }
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }
}
