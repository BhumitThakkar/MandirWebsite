package org.shreejalarammandir.model.event;

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

import org.shreejalarammandir.model.lesson.EventLesson;
import org.shreejalarammandir.model.mandirpeople.MandirPeople;
import org.shreejalarammandir.model.venue.Venue;

@Entity
public class Event {

	@Id														// PK, but not enough
	@GeneratedValue(strategy = GenerationType.IDENTITY)		// use this
	private int id;

	private String title;
	private int year;
	private String category;
	
	@ManyToOne
	private Venue venue;

	@ManyToMany
	private Set<MandirPeople> contactUs = new HashSet<MandirPeople>();

	@OneToMany(mappedBy="event", cascade = CascadeType.ALL)
	private Set<EventPicUrl> picUrl = new HashSet<EventPicUrl>();
	// Before deleting event, check for picUrl, if any then alert: "The pic linked to this event be deleted"

	@OneToMany(mappedBy="event", cascade = CascadeType.ALL)
	private Set<EventFlyerUrl> flyerUrl = new HashSet<EventFlyerUrl>();

	@OneToMany(mappedBy="event", cascade = CascadeType.ALL)
	private Set<DayOfEvent> dayOfEvent = new HashSet<DayOfEvent>();

	@OneToMany(mappedBy="event", cascade = CascadeType.ALL)
	private Set<Seva> seva = new HashSet<Seva>();

	@OneToMany(mappedBy="event", cascade = CascadeType.ALL)
	private Set<Sponsorship> sponsorship = new HashSet<Sponsorship>();

	@OneToMany(mappedBy="event", cascade = CascadeType.ALL)
	private Set<Ticket> ticket = new HashSet<Ticket>();	

	@OneToMany(mappedBy="event", cascade = CascadeType.ALL)
	private Set<EventNote> note = new HashSet<EventNote>();

	@OneToMany(mappedBy="event", cascade = CascadeType.ALL)
	private Set<EventLesson> eventLesson = new HashSet<EventLesson>();

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

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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

	public Set<EventPicUrl> getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(Set<EventPicUrl> picUrl) {
		this.picUrl = picUrl;
	}

	public Set<EventFlyerUrl> getFlyerUrl() {
		return flyerUrl;
	}

	public void setFlyerUrl(Set<EventFlyerUrl> flyerUrl) {
		this.flyerUrl = flyerUrl;
	}

	public Set<DayOfEvent> getDayOfEvent() {
		return dayOfEvent;
	}

	public void setDayOfEvent(Set<DayOfEvent> dayOfEvent) {
		this.dayOfEvent = dayOfEvent;
	}

	public Set<Seva> getSeva() {
		return seva;
	}

	public void setSeva(Set<Seva> seva) {
		this.seva = seva;
	}

	public Set<Sponsorship> getSponsorship() {
		return sponsorship;
	}

	public void setSponsorship(Set<Sponsorship> sponsorship) {
		this.sponsorship = sponsorship;
	}

	public Set<Ticket> getTicket() {
		return ticket;
	}

	public void setTicket(Set<Ticket> ticket) {
		this.ticket = ticket;
	}

	public Set<EventNote> getNote() {
		return note;
	}

	public void setNote(Set<EventNote> note) {
		this.note = note;
	}

	public Set<EventLesson> getEventLesson() {
		return eventLesson;
	}

	public void setEventLesson(Set<EventLesson> eventLesson) {
		this.eventLesson = eventLesson;
	}
	
	
}
