package nl.finan.jbehave.entities;

import nl.finan.jbehave.steps.Step;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "JBEHAVE_LOG_STEP")
public class StepLog extends Log{
    //TODO: model step

}
