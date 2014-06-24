package nl.finan.jbehave.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="JBEHAVE_LOG_SCENARIO")
public class ScenarioLog  extends Log{

    @ManyToOne
    private Scenario scenario;

    private List<StepLog> stepLogs;

    public Scenario getScenario() {
        return scenario;
    }

    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    public List<StepLog> getStepLogs() {
        return stepLogs;
    }

    public void setStepLogs(List<StepLog> stepLogs) {
        this.stepLogs = stepLogs;
    }
}
