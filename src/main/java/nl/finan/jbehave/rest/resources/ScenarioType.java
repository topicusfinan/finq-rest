package nl.finan.jbehave.rest.resources;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.cxf.jaxrs.ext.xml.XMLName;

@XmlRootElement
public class ScenarioType {
	
	@XmlElement
	private String name;
	
	@XmlElement
	private List<String> steps;

	public ScenarioType() {
	}

	public ScenarioType(String name, List<String> steps) {
		this.name = name;
		this.steps = steps;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getSteps() {
		return steps;
	}

	public void setSteps(List<String> steps) {
		this.steps = steps;
	}
	
}
