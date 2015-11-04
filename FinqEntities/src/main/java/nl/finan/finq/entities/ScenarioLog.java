package nl.finan.finq.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "FINQ_LOG_SCENARIO")
public class ScenarioLog extends Log
{

	@ManyToOne
	@JoinColumn
	@JsonBackReference
	private StoryLog storyLog;

	@ManyToOne
	@JoinColumn
	private Scenario scenario;

	@OneToMany(mappedBy = "scenarioLog")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<StepLog> stepLogs;

	public Scenario getScenario()
	{
		return scenario;
	}

	public void setScenario(Scenario scenario)
	{
		this.scenario = scenario;
	}

	public List<StepLog> getStepLogs()
	{
		if (stepLogs == null)
		{
			stepLogs = new ArrayList<>();
		}
		return stepLogs;
	}

	public void setStepLogs(List<StepLog> stepLogs)
	{
		this.stepLogs = stepLogs;
	}

	public StoryLog getStoryLog()
	{
		return storyLog;
	}

	public void setStoryLog(StoryLog storyLog)
	{
		this.storyLog = storyLog;
	}
}
