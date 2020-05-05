package org.shreejalarammandir.model.event;

import java.sql.Time;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Program {
	@Id														// PK, but not enough
	@GeneratedValue(strategy = GenerationType.IDENTITY)		// use this
	private int id;
	private String title;
	private Time fromTime;
	private Time toTime;
	
	@ManyToOne
	private DayOfEvent dayOfEvent;
	
	@OneToMany(mappedBy="program", cascade = CascadeType.ALL)
	private Set<EventButton> eventButton = new HashSet<EventButton>();

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

	public Time getFromTime() {
		return fromTime;
	}

	public void setFromTime(Time fromTime) {
		this.fromTime = fromTime;
	}

	public Time getToTime() {
		return toTime;
	}

	public void setToTime(Time toTime) {
		this.toTime = toTime;
	}

	public DayOfEvent getDayOfEvent() {
		return dayOfEvent;
	}

	public void setDayOfEvent(DayOfEvent dayOfEvent) {
		this.dayOfEvent = dayOfEvent;
	}

	public Set<EventButton> getEventButton() {
		return eventButton;
	}

	public void setEventButton(Set<EventButton> eventButton) {
		this.eventButton = eventButton;
	}
		
}
