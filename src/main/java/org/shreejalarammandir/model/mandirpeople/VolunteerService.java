package org.shreejalarammandir.model.mandirpeople;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class VolunteerService {

	@Id														// PK, but not enough
	@GeneratedValue(strategy = GenerationType.IDENTITY)		// use this
	private int id;
	private String title;
	
	@ManyToOne
	private ServicesGroup servicesGroup;
	
	@ManyToMany
	private Set<Volunteer> volunteer = new HashSet<Volunteer>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ServicesGroup getServicesGroup() {
		return servicesGroup;
	}

	public void setServicesGroup(ServicesGroup servicesGroup) {
		this.servicesGroup = servicesGroup;
	}

	public Set<Volunteer> getVolunteer() {
		return volunteer;
	}

	public void setVolunteer(Set<Volunteer> volunteer) {
		this.volunteer = volunteer;
	}
	
}
