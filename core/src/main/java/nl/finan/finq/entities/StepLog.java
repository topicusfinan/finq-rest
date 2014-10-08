package nl.finan.finq.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "FINQ_LOG_STEP")
public class StepLog extends Log {

    @ManyToOne
    @JoinColumn
    @JsonBackReference
    private ScenarioLog scenarioLog;

    @Column
    private String step;

    public ScenarioLog getScenarioLog() {
        return scenarioLog;
    }

    public void setScenarioLog(ScenarioLog scenarioLog) {
        this.scenarioLog = scenarioLog;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }
}
