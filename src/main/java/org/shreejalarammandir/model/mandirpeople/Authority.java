package org.shreejalarammandir.model.mandirpeople;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Authority {
	@Id														// PK, but not enough
	@GeneratedValue(strategy = GenerationType.IDENTITY)		// use this
	private int id;
	private String title;
	
	@ManyToOne
	private Post post;
	
	@ManyToMany
	private Set<MandirPeople> mandirPeople = new HashSet<MandirPeople>();

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

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public Set<MandirPeople> getMandirPeople() {
		return mandirPeople;
	}

	public void setMandirPeople(Set<MandirPeople> mandirPeople) {
		this.mandirPeople = mandirPeople;
	}
	
}
