package org.shreejalarammandir.model.pooja;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.shreejalarammandir.model.mandirpeople.MandirPeople;

@Entity
public class Pooja {
	@Id														// PK, but not enough
	@GeneratedValue(strategy = GenerationType.IDENTITY)		// use this
	private int id;
	private String title;
	private String seva;
	
	@OneToMany(mappedBy="pooja", cascade = CascadeType.ALL)
	private Set<PoojaNote> note = new HashSet<PoojaNote>();

	@ManyToMany
	private Set<MandirPeople> contactUs = new HashSet<MandirPeople>();

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

	public String getSeva() {
		return seva;
	}

	public void setSeva(String seva) {
		this.seva = seva;
	}

	public Set<PoojaNote> getNote() {
		return note;
	}

	public void setNote(Set<PoojaNote> note) {
		this.note = note;
	}

	public Set<MandirPeople> getContactUs() {
		return contactUs;
	}

	public void setContactUs(Set<MandirPeople> contactUs) {
		this.contactUs = contactUs;
	}
	
}
