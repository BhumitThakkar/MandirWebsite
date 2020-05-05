package org.shreejalarammandir.model.activity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.shreejalarammandir.model.lesson.ActivityLesson;
import org.shreejalarammandir.model.mandirpeople.MandirPeople;
import org.shreejalarammandir.model.venue.Venue;

@Entity
public class Activity {
	
	@Id														// PK, but not enough
	@GeneratedValue(strategy = GenerationType.IDENTITY)		// use this
	private int id;
	private boolean active = true;
	private String title;
	private String category;
	
	@OneToMany(mappedBy="activity", cascade = CascadeType.ALL)
	private Set<ActivityPicUrl> picUrl = new HashSet<ActivityPicUrl>();
	// Before deleting event, check for picUrl, if any then alert: "The pic linked to this event be deleted"
	
	@OneToMany(mappedBy="activity", cascade = CascadeType.ALL)
	private Set<Session> session = new HashSet<Session>();
	
	@OneToMany(mappedBy="activity", cascade = CascadeType.ALL)
	private Set<ActivityLesson> activityLesson = new HashSet<ActivityLesson>();

	@ManyToOne
	private Venue venue;

	@ManyToMany
	private Set<MandirPeople> contactUs = new HashSet<MandirPeople>();
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Set<ActivityPicUrl> getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(Set<ActivityPicUrl> picUrl) {
		this.picUrl = picUrl;
	}

	public Set<Session> getSession() {
		return session;
	}

	public void setSession(Set<Session> session) {
		this.session = session;
	}

	public Venue getVenue() {
		return venue;
	}

	public void setVenue(Venue venue) {
		this.venue = venue;
	}

	public Set<MandirPeople> getContactUs() {
		return contactUs;
	}

	public void setContactUs(Set<MandirPeople> contactUs) {
		this.contactUs = contactUs;
	}

	public Set<ActivityLesson> getActivityLesson() {
		return activityLesson;
	}

	public void setActivityLesson(Set<ActivityLesson> activityLesson) {
		this.activityLesson = activityLesson;
	}
	
}
