package org.shreejalarammandir.model.activity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class SessionButton {
	@Id														// PK, but not enough
	@GeneratedValue(strategy = GenerationType.IDENTITY)		// use this
	private int id;
	private String buttonFor;
	private String buttonTitle;
	private String url;

	@ManyToOne
	private Session session;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getButtonFor() {
		return buttonFor;
	}

	public void setButtonFor(String buttonFor) {
		this.buttonFor = buttonFor;
	}

	public String getButtonTitle() {
		return buttonTitle;
	}

	public void setButtonTitle(String buttonTitle) {
		this.buttonTitle = buttonTitle;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}
	
}
