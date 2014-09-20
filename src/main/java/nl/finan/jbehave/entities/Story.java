package nl.finan.jbehave.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "JBEHAVE_STORY")
public class Story extends GenericEntity{

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "BUNDLE_ID", nullable = true)
    @JsonBackReference
	private Bundle bundle;
	
	@Column(name = "NAME")
	private String name;

	@OneToMany(mappedBy = "story")
    @LazyCollection(LazyCollectionOption.FALSE)
	private List<Scenario> scenarios;
	
	@Column(name="DUMMY")
	@JsonIgnore
	private Boolean dummy = false;

	public Bundle getBundle() {
		return bundle;
	}

	public void setBundle(Bundle bundle) {
		this.bundle = bundle;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Scenario> getScenarios() {
		if(scenarios ==null){
			scenarios = new ArrayList<Scenario>();
		}
		return scenarios;
	}

	public void setScenarios(List<Scenario> scenarios) {
		this.scenarios = scenarios;
	}
	
	public Boolean isDummy() {
		return dummy;
	}

	public void setDummy(Boolean dummy) {
		this.dummy = dummy;
	}

	public String toStory(){
		StringBuilder builder = new StringBuilder();
		for(Scenario scenario: getScenarios()){
            builder.append(System.lineSeparator());
			builder.append(scenario.toStory());
		}
		
		return builder.toString();
	}
}
