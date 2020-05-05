package org.shreejalarammandir.model.mandirpeople;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "mahraj_id")
public class Mahraj extends MandirPeople {

	private String authorized = "Pending";		// Authorized, Pending, Unauthorized

	public String getAuthorized() {
		return authorized;
	}

	public void setAuthorized(String authorized) {
		this.authorized = authorized;
	}

}
