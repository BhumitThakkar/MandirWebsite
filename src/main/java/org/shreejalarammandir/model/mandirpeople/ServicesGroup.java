package org.shreejalarammandir.model.mandirpeople;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class ServicesGroup {
	
	@Id														// PK, but not enough
	@GeneratedValue(strategy = GenerationType.IDENTITY)		// use this
	private int id;
	private String title;
	
	@OneToMany(mappedBy="servicesGroup", cascade = CascadeType.ALL)
	private Set<VolunteerService> volunteerService = new HashSet<VolunteerService>();

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

	public Set<VolunteerService> getVolunteerService() {
		return volunteerService;
	}

	public void setVolunteerService(Set<VolunteerService> volunteerService) {
		this.volunteerService = volunteerService;
	}

}
