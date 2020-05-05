package org.shreejalarammandir.model.event;

import java.sql.Date;
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
public class DayOfEvent {

	@Id														// PK, but not enough
	@GeneratedValue(strategy = GenerationType.IDENTITY)		// use this
	private int id;
	private String titleOfDay;
	private Date date;
	private String weekDay;
	
	@ManyToOne
	private Event event;
	
	@OneToMany(mappedBy="dayOfEvent", cascade = CascadeType.ALL)
	private Set<Program> program = new HashSet<Program>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitleOfDay() {
		return titleOfDay;
	}

	public void setTitleOfDay(String titleOfDay) {
		this.titleOfDay = titleOfDay;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getWeekDay() {
		return weekDay;
	}

	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public Set<Program> getProgram() {
		return program;
	}

	public void setProgram(Set<Program> program) {
		this.program = program;
	}
	
}
