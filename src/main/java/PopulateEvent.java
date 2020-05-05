import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.shreejalarammandir.model.event.DayOfEvent;
import org.shreejalarammandir.model.event.Event;
import org.shreejalarammandir.model.event.EventFlyerUrl;
import org.shreejalarammandir.model.event.Program;
import org.shreejalarammandir.model.event.Seva;
import org.shreejalarammandir.model.event.Sponsorship;
import org.shreejalarammandir.model.event.Ticket;
import org.shreejalarammandir.model.mandirpeople.MandirPeople;
import org.shreejalarammandir.model.venue.Venue;

public class PopulateEvent {

	public static void populateEvent(Session session) {
		Event e;
		
		MandirPeople contactUs = new MandirPeople();
		Query query = session.createQuery("from MandirPeople where fullName.fName = 'Ashish' OR fullName.fName = 'Chirayu' OR fullName.fName = 'Bakul'");
		List<MandirPeople> list = (List<MandirPeople>) query.list();
		Set<MandirPeople> set = new HashSet<MandirPeople>(list);
		
		String eventTitle[] = {"", "NavGrah Pooja", "ShivRatri", "Holi", "HealthFair", "RamNavmi", "Hanuman Jayanti", "Sundarkand Path", "Drama", "Satyanarayan Katha", "Patotsav", "Janmashtmi", "Ganesh Chaturthi", "Bhakti Yoga", "Navratri", "Diwali", "Jalaram Jayanti", "Tulsi Vivah", "Feed My Starving Children"};
		int eventMonthDay[][] = {
				{0, 0, 0},
				{1, 1, 14},
				{1, 3, 4},
				{1, 3, 20},
				{2, 3, 31},
				{2, 4, 7},
				{1, 4, 14},
				{1, 4, 20},
				{1, 6, 7},
				{1, 6, 8},
				{1, 6, 9},
				{11, 7, 6},
				{11, 7, 7},
				{11, 7, 8},
				{11, 7, 9},
				{11, 7, 10},
				{11, 7, 11},
				{11, 7, 12},
				{11, 7, 13},
				{11, 7, 14},
				{11, 7, 15},
				{11, 7, 16},
				{1, 8, 24},
				{1, 9, 2},
				{1, 9, 14},
				{1, 10, 11},
				{4, 10, 25},
				{4, 10, 26},
				{4, 10, 27},
				{4, 10, 28},
				{1, 11, 3},
				{1, 11, 16},
				{1, 12, 6}
		};
		DayOfEvent doe;
		EventFlyerUrl efu;
		Random r = new Random();
		
		//----------------Venue----------------------------
		Venue v = new Venue();
		v.setName("Shree Jalaram Mandir");
		v.setStreet("425 Illinois Blvd.");
		v.setCity("Hoffman Estates");
		v.setState("Illinois");
		v.setZipCode("60169");
		//----------------Venue----------------------------
		
		for(int year = 2003; year <= 2020; year++) {
			int current = 1;
			for (int i = 1; i < eventTitle.length; i++) {
				e = new Event();
				
				e.setTitle(eventTitle[i]);
				e.setYear(year);
				//----------------Venue----------------------------
				v.getEvent().add(e);
				e.setVenue(v);
				session.save(v);
				//----------------Venue----------------------------
				
				for(int noOfDays = 1; noOfDays <= eventMonthDay[current][0]; noOfDays++) {
					
					doe = new DayOfEvent();
					Date d = Date.valueOf(year+"-"+eventMonthDay[current][1]+"-"+eventMonthDay[current][2]);
					doe.setDate(d);
					
					doe.setTitleOfDay("Day"+noOfDays);
					
					SimpleDateFormat df = new SimpleDateFormat("EEEE");
					doe.setWeekDay(df.format(d));
					
					//------------------------------
					Program p = new Program();
					p.setTitle("Pooja");
					p.setFromTime(Time.valueOf( "0"+(r.nextInt(5)+1) +":00:00" ));
					p.setToTime(Time.valueOf( "0"+(r.nextInt(5)+6) +":00:00" ));
					doe.getProgram().add(p);
					p.setDayOfEvent(doe);
//					session.save(p);
					//------------------------------
					
					doe.setEvent(e);
					e.getDayOfEvent().add(doe);
//					session.save(doe);
					
					if(eventMonthDay[current][0] == noOfDays) {
						current++;
						break;
					}
					else {
						current++;
					}

				}
				
				for (int j = 1; j <= 2; j++) {
					Seva s = new Seva();
					if(j == 1) {
						s.setSevaAmount(21);
						s.setSevaCategory("Aarti Seva");
					}
					else {
						s.setSevaAmount(51);
						s.setSevaCategory("Prasad Seva");
					}
					e.getSeva().add(s);
					s.setEvent(e);
//					session.save(s);
				}
				
				if(eventTitle[i] == "Drama") {
					for (int j = 1; j <= 3; j++) {
						Sponsorship ss = new Sponsorship();
						if(j == 1) {
							ss.setSponsorshipAmount(501);
							ss.setSponsorshipCategory("Silver");
						}
						else if(j == 2){
							ss.setSponsorshipAmount(1001);
							ss.setSponsorshipCategory("Gold");
						}
						else {
							ss.setSponsorshipAmount(5001);
							ss.setSponsorshipCategory("Platinum");
						}
						ss.setEvent(e);
						e.getSponsorship().add(ss);
//						session.save(ss);
					}
				}
				
				if(eventTitle[i] == "Drama" || eventTitle[i] == "Navratri") {
					for (int j = 1; j <= 3; j++) {
						Ticket t = new Ticket();
						if(j == 1) {
							t.setTicketAmount(11);
							t.setTicketCategory("Silver");
						}
						else if(j == 2){
							t.setTicketAmount(21);
							t.setTicketCategory("Gold");
						}
						else {
							t.setTicketAmount(51);
							t.setTicketCategory("Platinum");
						}
						t.setEvent(e);
						e.getTicket().add(t);
//						session.save(t);
					}
				}
				
				efu = new EventFlyerUrl();
				efu.setEvent(e);
				String flyerUrl = "img/flyer/event/" + eventTitle[i];
				efu.setFlyerUrl(flyerUrl);
				e.getFlyerUrl().add(efu);
//				session.save(efu);
				
				if(year <= 2019)
					e.setCategory("Past");
				else
					e.setCategory("Upcoming");

				e.setContactUs(set);
				for (MandirPeople mp: set) {
					mp.getEventContactUs().add(e);
					session.update(mp);
				}

				session.save(e);
			}
		}
				
	}

}
