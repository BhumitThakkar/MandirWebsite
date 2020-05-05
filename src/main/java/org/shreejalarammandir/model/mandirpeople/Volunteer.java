package org.shreejalarammandir.model.mandirpeople;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.PreRemove;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "volunteer_id")
public class Volunteer extends MandirPeople {

	@ManyToMany(mappedBy="volunteer")
	private Set<VolunteerService> volunteerService = new HashSet<VolunteerService>();

	public Set<VolunteerService> getVolunteerService() {
		return volunteerService;
	}

	public void setVolunteerService(Set<VolunteerService> volunteerService) {
		this.volunteerService = volunteerService;
	}
	
	@PreRemove
	public void preRemove(){
	    for(VolunteerService temp : this.volunteerService){
	        temp.getVolunteer().remove(this);
	    }
	}
}
