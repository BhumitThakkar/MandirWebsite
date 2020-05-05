package org.shreejalarammandir.model.lesson;

import javax.persistence.Entity;

import javax.persistence.PrimaryKeyJoinColumn;

import org.shreejalarammandir.model.mandirpeople.MandirPeople;

@Entity
@PrimaryKeyJoinColumn(name = "generalLesson_id")
public class GeneralLesson extends Lesson{

	public MandirPeople getMandirPeople() {
		return super.getMandirPeople();
	}
	
}
