package org.shreejalarammandir.model.mandirpeople;

import javax.persistence.Embeddable;

@Embeddable
public class FullName {
	private String fName;
	private String lName;
	
	public FullName() {
		
	}
	public FullName(String fName, String lName) {
		this.fName = fName;
		this.lName = lName;
	}
	
	public String getfName() {
		return fName;
	}
	public void setfName(String fName) {
		this.fName = fName;
	}
	public String getlName() {
		return lName;
	}
	public void setlName(String lName) {
		this.lName = lName;
	}
	
	@Override
	public String toString() {
		return this.fName +" "+ this.lName;
	}
	
	
}
