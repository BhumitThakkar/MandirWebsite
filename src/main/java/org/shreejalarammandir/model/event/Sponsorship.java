package org.shreejalarammandir.model.event;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Sponsorship {
	@Id														// PK, but not enough
	@GeneratedValue(strategy = GenerationType.IDENTITY)		// use this
	private int id;
	private String sponsorshipCategory;
	private int sponsorshipAmount;
	
	@ManyToOne
	private Event event;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSponsorshipCategory() {
		return sponsorshipCategory;
	}

	public void setSponsorshipCategory(String sponsorshipCategory) {
		this.sponsorshipCategory = sponsorshipCategory;
	}

	public int getSponsorshipAmount() {
		return sponsorshipAmount;
	}

	public void setSponsorshipAmount(int sponsorshipAmount) {
		this.sponsorshipAmount = sponsorshipAmount;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}
	
}
