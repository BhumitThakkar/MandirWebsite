package org.shreejalarammandir.model.pooja;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class PoojaNote {
	@Id														// PK, but not enough
	@GeneratedValue(strategy = GenerationType.IDENTITY)		// use this
	private int id;
	private String note;
	
	@ManyToOne
	private Pooja pooja;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Pooja getPooja() {
		return pooja;
	}

	public void setPooja(Pooja pooja) {
		this.pooja = pooja;
	}
	
}
