package org.shreejalarammandir.model.lesson;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import org.shreejalarammandir.model.event.Event;
import org.shreejalarammandir.model.mandirpeople.MandirPeople;

@Entity
@PrimaryKeyJoinColumn(name = "eventLesson_id")
public class EventLesson extends Lesson{

	@ManyToOne
	private Event event;
	
	public MandirPeople getMandirPeople() {
		return super.getMandirPeople();
	}

	public Event getEvent() {
		return event;
	}
	
	public void setEvent(Event event) {
		this.event = event;
	}
	
	
	
}
