package nl.finan.jbehave.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import nl.finan.jbehave.steps.Step;

import javax.persistence.*;

@Entity
@Table(name = "JBEHAVE_LOG_STEP")
public class StepLog extends Log{

    @ManyToOne
    @JoinColumn
    @JsonIgnore
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
