package org.shreejalarammandir.model.lesson;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

import org.shreejalarammandir.model.mandirpeople.MandirPeople;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class Lesson {
	
	@Id														// PK, but not enough
	@GeneratedValue(strategy = GenerationType.IDENTITY)		// use this
	private int id;
	private String description;
	private Date date;
	private String category;
	
	@ManyToOne
	private MandirPeople mandirPeople;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public MandirPeople getMandirPeople() {
		return mandirPeople;
	}

	public void setMandirPeople(MandirPeople mandirPeople) {
		this.mandirPeople = mandirPeople;
	}
	
}
