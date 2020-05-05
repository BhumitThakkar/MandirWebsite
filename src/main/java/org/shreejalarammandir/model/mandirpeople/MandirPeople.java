package org.shreejalarammandir.model.mandirpeople;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;

import org.shreejalarammandir.model.activity.Activity;
import org.shreejalarammandir.model.activity.Session;
import org.shreejalarammandir.model.event.Event;
import org.shreejalarammandir.model.lesson.Lesson;
import org.shreejalarammandir.model.pooja.Pooja;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class MandirPeople {
	
	@Id														// PK, but not enough
	@GeneratedValue(strategy = GenerationType.IDENTITY)		// use this
	private int id;
	private FullName fullName;
	private String number;
	private String email;
	private String password;
	private String picUrl;
	private String category;
	
	@ManyToMany(mappedBy = "contactUs")
	private Set<Activity> activityContactUs = new HashSet<Activity>();
	
	@ManyToMany(mappedBy = "contactUs")
	private Set<Event> eventContactUs = new HashSet<Event>();
	
	@ManyToMany(mappedBy = "contactUs")
	private Set<Pooja> poojaContactUs = new HashSet<Pooja>();

	@ManyToMany(mappedBy="mandirPeople")
	private Set<Authority> authority = new HashSet<Authority>();
	
	@ManyToMany(mappedBy="instructor")
	private Set<Session> activitySession = new HashSet<Session>();

	@OneToMany(mappedBy="mandirPeople", cascade = CascadeType.REMOVE)
	private Set<Lesson> lesson = new HashSet<Lesson>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public FullName getFullName() {
		return fullName;
	}

	public void setFullName(FullName fullName) {
		this.fullName = fullName;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Set<Activity> getActivityContactUs() {
		return activityContactUs;
	}

	public void setActivityContactUs(Set<Activity> activityContactUs) {
		this.activityContactUs = activityContactUs;
	}

	public Set<Event> getEventContactUs() {
		return eventContactUs;
	}

	public void setEventContactUs(Set<Event> eventContactUs) {
		this.eventContactUs = eventContactUs;
	}

	public Set<Pooja> getPoojaContactUs() {
		return poojaContactUs;
	}

	public void setPoojaContactUs(Set<Pooja> poojaContactUs) {
		this.poojaContactUs = poojaContactUs;
	}

	public Set<Authority> getAuthority() {
		return authority;
	}

	public void setAuthority(Set<Authority> authority) {
		this.authority = authority;
	}

	public Set<Lesson> getLesson() {
		return lesson;
	}

	public void setLesson(Set<Lesson> lesson) {
		this.lesson = lesson;
	}
	
	public Set<Session> getActivitySession() {
		return activitySession;
	}

	public void setActivitySession(Set<Session> activitySession) {
		this.activitySession = activitySession;
	}

	@PreRemove
	public void preRemove(){
	    for(Activity temp : this.activityContactUs){
	        temp.getContactUs().remove(this);
	    }
	    for(Event temp : this.eventContactUs){
	        temp.getContactUs().remove(this);
	    }
	    for(Pooja temp : this.poojaContactUs){
	        temp.getContactUs().remove(this);
	    }
	    for(Authority temp : this.authority){
	        temp.getMandirPeople().remove(this);
	    }
	    for(Session temp : this.activitySession){
	        temp.getInstructor().remove(this);
	    }
	}

}