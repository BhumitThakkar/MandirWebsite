package org.shreejalarammandir.model.publication;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;

@Entity
public class Publication {
	
	@Id														// PK, but not enough
	@GeneratedValue(strategy = GenerationType.IDENTITY)		// use this
	private int id;
	private String month;
	private int year;
	private String publicationUrl;
	
	@OneToMany(mappedBy="publication")
	private Set<Parcha> parcha = new HashSet<Parcha>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getPublicationUrl() {
		return publicationUrl;
	}

	public void setPublicationUrl(String publicationUrl) {
		this.publicationUrl = publicationUrl;
	}

	public Set<Parcha> getParcha() {
		return parcha;
	}

	public void setParcha(Set<Parcha> parcha) {
		this.parcha = parcha;
	}
	
	@PreRemove
	public void preRemove(){
	    for(Parcha temp : this.parcha){
	        temp.setPublication(null);
	    }
	}
}
