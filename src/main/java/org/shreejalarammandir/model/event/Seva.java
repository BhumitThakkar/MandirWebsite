package org.shreejalarammandir.model.event;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Seva {
	@Id														// PK, but not enough
	@GeneratedValue(strategy = GenerationType.IDENTITY)		// use this
	private int id;
	private String sevaCategory;
	private int sevaAmount;
	
	@ManyToOne
	private Event event;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSevaCategory() {
		return sevaCategory;
	}

	public void setSevaCategory(String sevaCategory) {
		this.sevaCategory = sevaCategory;
	}

	public int getSevaAmount() {
		return sevaAmount;
	}

	public void setSevaAmount(int sevaAmount) {
		this.sevaAmount = sevaAmount;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}
	
}
