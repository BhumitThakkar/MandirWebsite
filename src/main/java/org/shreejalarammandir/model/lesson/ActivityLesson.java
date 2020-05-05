package org.shreejalarammandir.model.lesson;

import 
javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import org.shreejalarammandir.model.activity.Activity;
import org.shreejalarammandir.model.mandirpeople.MandirPeople;

@Entity
@PrimaryKeyJoinColumn(name = "activityLesson_id")
public class ActivityLesson extends Lesson{

	@ManyToOne
	private Activity activity;
	
	public MandirPeople getMandirPeople() {
		return super.getMandirPeople();
	}
	
	public Activity getActivity() {
		return activity;
	}
	
	public void setActivity(Activity activity) {
		this.activity = activity;
	}

}
