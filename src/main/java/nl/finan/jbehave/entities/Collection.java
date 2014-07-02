package nl.finan.jbehave.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="JBEHAVE_COLLECTION")
public class Collection extends GenericEntity {

	@Column(name="NAME")
	private String name;
	
	@ManyToMany
	@JoinTable(
		      name="JBEHAVE_COLLECTION_SET",
		      joinColumns={@JoinColumn(name="BUNDLE_ID", referencedColumnName="ID")},
		      inverseJoinColumns={@JoinColumn(name="COLLECTION_ID", referencedColumnName="ID")})
	private List<Bundle> bundles;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Bundle> getBundles() {
		return bundles;
	}

	public void setBundles(List<Bundle> bundles) {
		this.bundles = bundles;
	}	
}
