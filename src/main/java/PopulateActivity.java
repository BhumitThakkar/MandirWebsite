import java.sql.Date;
import java.sql.Time;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.shreejalarammandir.model.activity.Activity;
import org.shreejalarammandir.model.activity.WeekOfMonth;
import org.shreejalarammandir.model.mandirpeople.MandirPeople;
import org.shreejalarammandir.model.venue.Venue;

public class PopulateActivity {

	public static void populateActivity(Session session) {
		Activity a;

		Query query = session.createQuery("from MandirPeople where fullName.fName = 'Ashish' OR fullName.fName = 'Jigna'");
		List<MandirPeople> list = (List<MandirPeople>) query.list();
		Set<MandirPeople> set = new HashSet<MandirPeople>(list);
		
		query = session.createQuery("from Venue where name = 'Shree Jalaram Mandir'");
		Venue v = (Venue) query.uniqueResult();
		
		String activityTitle[] = {"", "Aayurvedic Clinic", "Health Clinic", "Yoga Classes", "Bal-Vihar Classes", "Bharatnatyam Classes", "Gujarati Classes", "Keyboard Classes", "Tabla Classes"};
		
		for (int i = 1; i < activityTitle.length; i++) {
			a = new Activity();
			
			if(activityTitle[i] == "Aayurvedic Clinic" || activityTitle[i] == "Health Clinic" || activityTitle[i] == "Yoga Classes")
				a.setCategory("Health");
			else if(activityTitle[i] == "Bal-Vihar Classes" || activityTitle[i] == "Bharatnatyam Classes" || activityTitle[i] == "Gujarati Classes" || activityTitle[i] == "Keyboard Classes" || activityTitle[i] == "Tabla Classes")
				a.setCategory("Classes");
			
			a.setActive(true);
			a.setContactUs(set);
			for (MandirPeople mp: set) {
				mp.getActivityContactUs().add(a);
				session.update(mp);
			}
			
			a.setTitle(activityTitle[i]);
			a.setVenue(v);
			v.getActivity().add(a);
			
			//--------------Session--------------------
			org.shreejalarammandir.model.activity.Session s = new org.shreejalarammandir.model.activity.Session();
			
			if(a.getCategory() == "Classes")
				s.setFee(51);
			else
				s.setFee(0);
			
			String FlyerUrl = "img/flyer/activity/" + activityTitle[i];
			s.setFlyerUrl(FlyerUrl);
			
			switch(activityTitle[i]) {
				case "Aayurvedic Clinic":{
					s.setStartDate(null);					// means continuous
					s.setEndDate(null);						// means continuous
					s.setFromTime(Time.valueOf("11:00:00"));
					s.setToTime(Time.valueOf("13:00:00"));
					s.setMinAge(0);
					s.setDayOfWeek("Saturday");
					
					WeekOfMonth wom = new WeekOfMonth();
					wom.setWeekOfMonth(2);
					wom.setSession(s);
					s.getWeekOfMonth().add(wom);
					break;
				}
				case "Health Clinic":{
					s.setStartDate(null);
					s.setEndDate(null);
					s.setFromTime(Time.valueOf("10:00:00"));
					s.setToTime(Time.valueOf("13:00:00"));
					s.setMinAge(0);
					s.setDayOfWeek("Saturday");
					
					WeekOfMonth wom = new WeekOfMonth();
					wom.setWeekOfMonth(4);
					wom.setSession(s);
					s.getWeekOfMonth().add(wom);					
					break;
				}
				case "Yoga Classes":{
					s.setStartDate(null);
					s.setEndDate(null);
					s.setFromTime(Time.valueOf("08:00:00"));
					s.setToTime(Time.valueOf("09:00:00"));
					s.setMinAge(7);
					s.setDayOfWeek("Saturday");
					
					for (int j = 1; j <= 5; j++) {
						WeekOfMonth wom = new WeekOfMonth();
						wom.setWeekOfMonth(j);
						wom.setSession(s);
						s.getWeekOfMonth().add(wom);
					}
					break;
				}
				case "Bal-Vihar Classes":{
					s.setStartDate(null);
					s.setEndDate(null);
					s.setFromTime(Time.valueOf("13:00:00"));
					s.setToTime(Time.valueOf("14:00:00"));
					s.setMinAge(5);
					s.setDayOfWeek("Saturday");
					
					query = session.createQuery("from MandirPeople where fullName.fName = 'Ashish'");
					MandirPeople mp = (MandirPeople) query.uniqueResult();
					s.getInstructor().add(mp);
					session.update(mp);
					
					for (int j = 1; j <= 5; j++) {
						WeekOfMonth wom = new WeekOfMonth();
						wom.setWeekOfMonth(j);
						wom.setSession(s);
						s.getWeekOfMonth().add(wom);
					}
					break;
				}
				case "Bharatnatyam Classes":{
					s.setStartDate(null);
					s.setEndDate(null);
					s.setFromTime(Time.valueOf("17:30:00"));
					s.setToTime(Time.valueOf("18:30:00"));
					s.setMinAge(6);
					s.setDayOfWeek("Tuesday");
					
					for (int j = 1; j <= 5; j++) {
						WeekOfMonth wom = new WeekOfMonth();
						wom.setWeekOfMonth(j);
						wom.setSession(s);
						s.getWeekOfMonth().add(wom);
					}
					break;
				}
				case "Gujarati Classes":{
					s.setStartDate(null);
					s.setEndDate(null);
					s.setFromTime(Time.valueOf("11:30:00"));
					s.setToTime(Time.valueOf("13:30:00"));
					s.setMinAge(5);
					s.setDayOfWeek("Saturday");
					
					for (int j = 1; j <= 5; j++) {
						WeekOfMonth wom = new WeekOfMonth();
						wom.setWeekOfMonth(j);
						wom.setSession(s);
						s.getWeekOfMonth().add(wom);
					}
					break;
				}
				case "Keyboard Classes":{
					s.setStartDate(Date.valueOf("2019-10-12"));
					s.setEndDate(Date.valueOf("2019-12-21"));
					s.setFromTime(Time.valueOf("09:15:00"));
					s.setToTime(Time.valueOf("10:15:00"));
					s.setMinAge(7);
					s.setDayOfWeek("Saturday");
					
					for (int j = 1; j <= 5; j++) {
						WeekOfMonth wom = new WeekOfMonth();
						wom.setWeekOfMonth(j);
						wom.setSession(s);
						s.getWeekOfMonth().add(wom);
					}
					break;
				}
				case "Tabla Classes":{
					s.setStartDate(Date.valueOf("2019-10-12"));
					s.setEndDate(Date.valueOf("2019-12-21"));
					s.setFromTime(Time.valueOf("09:15:00"));
					s.setToTime(Time.valueOf("10:15:00"));
					s.setMinAge(7);
					s.setDayOfWeek("Saturday");
					
					for (int j = 1; j <= 5; j++) {
						WeekOfMonth wom = new WeekOfMonth();
						wom.setWeekOfMonth(j);
						wom.setSession(s);
						s.getWeekOfMonth().add(wom);
					}
					break;
				}
			}		// Switch Complete
			s.setActivity(a);
			a.getSession().add(s);
			session.save(a);
		}
	}
}