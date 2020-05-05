import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.shreejalarammandir.model.activity.Activity;
import org.shreejalarammandir.model.event.Event;
import org.shreejalarammandir.model.lesson.ActivityLesson;
import org.shreejalarammandir.model.lesson.EventLesson;
import org.shreejalarammandir.model.lesson.GeneralLesson;
import org.shreejalarammandir.model.mandirpeople.MandirPeople;

public class PopulateLesson {

	public static void populateLesson(Session session) {
		Query query = session.createQuery("from MandirPeople");
		List<MandirPeople> list = (List<MandirPeople>) query.list();
		
		int current = 0;
		
		// Activity Lesson
		Query query2 = session.createQuery("from Activity");
		List<Activity> list2 = (List<Activity>) query2.list();
		for (int i = 1; i <= 150; i++) {
			ActivityLesson al = new ActivityLesson();
			al.setCategory("Activity");
			
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate localDate = LocalDate.now();
			al.setDate(Date.valueOf(dtf.format(localDate)));
			
			al.setDescription("Discription of mistake related to activity that any volunteer or managing people want to save for future reference");
			
			al.setMandirPeople(list.get(i-1));
			list.get(i-1).getLesson().add(al);
			
			al.setActivity(list2.get(current));
			list2.get(current).getActivityLesson().add(al);
			
			session.update(list.get(i-1));
			session.update(list2.get(current));
			
			session.save(al);
			
			if(current == list2.size()-1 )
				current = 0;
			else
				current++;
		}
		
		// Event Lesson
		Query query3 = session.createQuery("from Event");
		List<Event> list3 = (List<Event>) query3.list();
		for (int i = 1; i <= 150; i++) {
			EventLesson el = new EventLesson();
			el.setCategory("Event");
			
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate localDate = LocalDate.now();
			el.setDate(Date.valueOf(dtf.format(localDate)));
			
			el.setDescription("Discription of mistake related to event that any volunteer or managing people want to save for future reference");
			
			el.setMandirPeople(list.get(i-1));
			list.get(i-1).getLesson().add(el);
			
			el.setEvent(list3.get(i-1));
			list3.get(i-1).getEventLesson().add(el);
			
			session.update(list.get(i-1));
			session.update(list3.get(i-1));
			
			session.save(el);
		}
		
		
		// General Lesson
		for (int i = 1; i <= 150; i++) {
			GeneralLesson gl = new GeneralLesson();
			gl.setCategory("General");
			
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate localDate = LocalDate.now();
			gl.setDate(Date.valueOf(dtf.format(localDate)));
			
			gl.setDescription("Discription of general mistake that any volunteer or managing people want to save for future reference");
			
			gl.setMandirPeople(list.get(i-1));
			list.get(i-1).getLesson().add(gl);
			
			session.update(list.get(i-1));
			
			session.save(gl);
		}
	}
}
