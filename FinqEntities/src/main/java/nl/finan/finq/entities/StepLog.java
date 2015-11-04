package nl.finan.finq.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "FINQ_LOG_STEP")
public class StepLog extends Log {

    @ManyToOne
    @JoinColumn
    @JsonBackReference
    private ScenarioLog scenarioLog;

    @JoinColumn
    @ManyToOne
    private Step step;

    public ScenarioLog getScenarioLog() {
        return scenarioLog;
    }

    public void setScenarioLog(ScenarioLog scenarioLog) {
        this.scenarioLog = scenarioLog;
    }

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
    }
}
