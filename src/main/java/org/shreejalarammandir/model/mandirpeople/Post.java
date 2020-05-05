package org.shreejalarammandir.model.mandirpeople;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;

@Entity
public class Post {

	@Id														// PK, but not enough
	@GeneratedValue(strategy = GenerationType.IDENTITY)		// use this
	private int id;
	private String title;
	
	@ManyToMany(mappedBy="post")
	private Set<CommitteeMember> committeeMember = new HashSet<CommitteeMember>();
	
	@OneToMany(mappedBy="post")
	private Set<Authority> authority = new HashSet<Authority>();

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

	public Set<CommitteeMember> getCommitteeMember() {
		return committeeMember;
	}

	public void setCommitteeMember(Set<CommitteeMember> committeeMember) {
		this.committeeMember = committeeMember;
	}

	public Set<Authority> getAuthority() {
		return authority;
	}

	public void setAuthority(Set<Authority> authority) {
		this.authority = authority;
	}
	
	@PreRemove
	public void preRemove(){
	    for(CommitteeMember temp : this.committeeMember){
	        temp.getPost().remove(this);
	    }
	    for(Authority temp : this.authority){
	        temp.setPost(null);
	    }
	}
}
