package org.shreejalarammandir.model.activity;

import java.sql.Date;
import java.sql.Time;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;

import org.shreejalarammandir.model.mandirpeople.MandirPeople;


@Entity
public class Session {

	@Id														// PK, but not enough
	@GeneratedValue(strategy = GenerationType.IDENTITY)		// use this
	private int id;
	private Date startDate;
	private Date endDate;
	private Time fromTime;
	private Time toTime;
	private String dayOfWeek;
	private int fee;
	private String flyerUrl;
	private int minAge;
	
	@OneToMany(mappedBy="session", cascade = CascadeType.ALL)
	private Set<SessionNote> note = new HashSet<SessionNote>();
	
	@OneToMany(mappedBy="session", cascade = CascadeType.ALL)
	private Set<WeekOfMonth> weekOfMonth = new HashSet<WeekOfMonth>();
	
	@OneToMany(mappedBy="session", cascade = CascadeType.ALL)
	private Set<SessionButton> sessionButton = new HashSet<SessionButton>();

	@ManyToMany
	private Set<MandirPeople> instructor = new HashSet<MandirPeople>();
	
	@ManyToOne
	private Activity activity;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Time getFromTime() {
		return fromTime;
	}

	public void setFromTime(Time fromTime) {
		this.fromTime = fromTime;
	}

	public Time getToTime() {
		return toTime;
	}

	public void setToTime(Time toTime) {
		this.toTime = toTime;
	}

	public String getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public int getFee() {
		return fee;
	}

	public void setFee(int fee) {
		this.fee = fee;
	}

	public String getFlyerUrl() {
		return flyerUrl;
	}

	public void setFlyerUrl(String flyerUrl) {
		this.flyerUrl = flyerUrl;
	}

	public int getMinAge() {
		return minAge;
	}

	public void setMinAge(int minAge) {
		this.minAge = minAge;
	}

	public Set<SessionNote> getNote() {
		return note;
	}

	public void setNote(Set<SessionNote> note) {
		this.note = note;
	}

	public Set<WeekOfMonth> getWeekOfMonth() {
		return weekOfMonth;
	}

	public void setWeekOfMonth(Set<WeekOfMonth> weekOfMonth) {
		this.weekOfMonth = weekOfMonth;
	}

	public Set<SessionButton> getSessionButton() {
		return sessionButton;
	}

	public void setSessionButton(Set<SessionButton> sessionButton) {
		this.sessionButton = sessionButton;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public Set<MandirPeople> getInstructor() {
		return instructor;
	}

	public void setInstructor(Set<MandirPeople> instructor) {
		this.instructor = instructor;
	}
	
	@PreRemove
	public void preRemove(){
	    for(MandirPeople temp : this.instructor){
	        temp.getActivitySession().remove(this);
	    }
	}

}
