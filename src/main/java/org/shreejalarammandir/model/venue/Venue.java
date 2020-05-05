package org.shreejalarammandir.model.venue;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;

import org.shreejalarammandir.model.activity.Activity;
import org.shreejalarammandir.model.event.Event;

@Entity
public class Venue {
	
	@Id														// PK, but not enough
	@GeneratedValue(strategy = GenerationType.IDENTITY)		// use this
	private int id;
	private String name;
	private String street;
	private String city;
	private String state;
	private String zipCode;
	
	@OneToMany(mappedBy="venue")
	private Set<Event> event = new HashSet<Event>();
	
	@OneToMany(mappedBy="venue")
	private Set<Activity> activity = new HashSet<Activity>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public Set<Event> getEvent() {
		return event;
	}

	public void setEvent(Set<Event> event) {
		this.event = event;
	}

	public Set<Activity> getActivity() {
		return activity;
	}

	public void setActivity(Set<Activity> activity) {
		this.activity = activity;
	}
	
	@PreRemove
	public void preRemove(){
	    for(Activity temp : this.activity){
	        temp.setVenue(null);
	    }
	    for(Event temp : this.event){
	        temp.setVenue(null);
	    }
	}

	@Override
	public String toString() {
		return "[name=" + name + ", street=" + street + ", city=" + city + ", state=" + state + ", zipCode="
				+ zipCode + "]";
	}
	
	
	
}
