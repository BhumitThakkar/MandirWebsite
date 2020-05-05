import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.shreejalarammandir.model.event.DayOfEvent;
import org.shreejalarammandir.model.event.Event;
import org.shreejalarammandir.model.event.EventFlyerUrl;
import org.shreejalarammandir.model.event.EventNote;
import org.shreejalarammandir.model.event.Program;
import org.shreejalarammandir.model.event.Seva;
import org.shreejalarammandir.model.event.Sponsorship;
import org.shreejalarammandir.model.event.Ticket;
import org.shreejalarammandir.model.lesson.ActivityLesson;
import org.shreejalarammandir.model.lesson.EventLesson;
import org.shreejalarammandir.model.lesson.Lesson;
import org.shreejalarammandir.model.mandirpeople.Authority;
import org.shreejalarammandir.model.mandirpeople.MandirPeople;
import org.shreejalarammandir.model.mandirpeople.ServicesGroup;
import org.shreejalarammandir.model.mandirpeople.VolunteerService;
import org.shreejalarammandir.model.publication.Parcha;

public class Testing {
	
	public static void main(String[] args) {
		Session s = getSession();
		Transaction t = s.beginTransaction();
		boolean loop = true;
		while(loop) {
			System.out.println("------------------");
			System.out.println("1: Give the list of Services for which temple has no Volunteers.");
			System.out.println("2: Give the list of Authorities for which temple has no authorized person (MandirPeople or person at POST).");
			System.out.println("3: For a list of Services how many & who are the Volunteers.");
			System.out.println("4: For a list of Authority who are the authorized people (MandirPeople or person at POST).");
			System.out.println("5: Show upcoming Events with their ContactUS people, Ticket (if any), Seva (If any), Sponsorship (If any), DayOfEvent, Program and EventButton (If any).");
			System.out.println("6: Show the Parcha not assigned to any Publication.");
			System.out.println("7: View all Lessons for Activity, Event & General for a specific MandirPeople.");
			System.out.println("8: View distinct Event title.");
			System.out.println("9: Show last Event with their ContactUS people, Ticket (if any), Seva (If any), Sponsorship (If any), DayOfEvent, Program and EventButton(If any) where Event Title = 'selected_event_title'.");
			System.out.println("10: View all Lessons for the next Event / Activity & General Lessons for a specific MandirPeople.");
			System.out.println("11: View Service Group that has no Volunteer Services.");
			System.out.println("------------------");
			System.out.print("Select 0 to exit or Select a query to run: ");
			Scanner myObj = new Scanner(System.in);  // Create a Scanner object
			int ans = myObj.nextInt();
			
			switch (ans) {
				case 0:{
					loop = false;
					myObj.close();
					t.commit();
					s.close();
					System.out.println("Good Bye. Have a Good Day!");
					break;
				}
				case 1:{
					System.out.println("~~~~~~~~~~~~~~~~~~Launching Query "+ans+"~~~~~~~~~~~~~~~~~~");
					executeQuery1(s);
					System.out.println("~~~~~~~~~~~~~~~~Exiting From Query "+ans+"~~~~~~~~~~~~~~~~~");
					break;
				}
				case 2:{
					System.out.println("~~~~~~~~~~~~~~~~~~Launching Query "+ans+"~~~~~~~~~~~~~~~~~~");
					executeQuery2(s);
					System.out.println("~~~~~~~~~~~~~~~~Exiting From Query "+ans+"~~~~~~~~~~~~~~~~~");
					break;			
				}
				case 3:{
					System.out.println("~~~~~~~~~~~~~~~~~~Launching Query "+ans+"~~~~~~~~~~~~~~~~~~");
					executeQuery3(s);
					System.out.println("~~~~~~~~~~~~~~~~Exiting From Query "+ans+"~~~~~~~~~~~~~~~~~");
					break;			
				}
				case 4:{
					System.out.println("~~~~~~~~~~~~~~~~~~Launching Query "+ans+"~~~~~~~~~~~~~~~~~~");
					executeQuery4(s);
					System.out.println("~~~~~~~~~~~~~~~~Exiting From Query "+ans+"~~~~~~~~~~~~~~~~~");
					break;			
				}
				case 5:{
					System.out.println("~~~~~~~~~~~~~~~~~~Launching Query "+ans+"~~~~~~~~~~~~~~~~~~");
					executeQuery5(s);
					System.out.println("~~~~~~~~~~~~~~~~Exiting From Query "+ans+"~~~~~~~~~~~~~~~~~");
					break;			
				}
				case 6:{
					System.out.println("~~~~~~~~~~~~~~~~~~Launching Query "+ans+"~~~~~~~~~~~~~~~~~~");
					executeQuery6(s);
					System.out.println("~~~~~~~~~~~~~~~~Exiting From Query "+ans+"~~~~~~~~~~~~~~~~~");
					break;			
				}
				case 7:{
					System.out.println("~~~~~~~~~~~~~~~~~~Launching Query "+ans+"~~~~~~~~~~~~~~~~~~");
					executeQuery7(s, myObj);
					System.out.println("~~~~~~~~~~~~~~~~Exiting From Query "+ans+"~~~~~~~~~~~~~~~~~");
					break;			
				}
				case 8:{
					System.out.println("~~~~~~~~~~~~~~~~~~Launching Query "+ans+"~~~~~~~~~~~~~~~~~~");
					executeQuery8(s);
					System.out.println("~~~~~~~~~~~~~~~~Exiting From Query "+ans+"~~~~~~~~~~~~~~~~~");
					break;			
				}
				case 9:{
					System.out.println("~~~~~~~~~~~~~~~~~~Launching Query "+ans+"~~~~~~~~~~~~~~~~~~");
					executeQuery9(s, myObj);
					System.out.println("~~~~~~~~~~~~~~~~Exiting From Query "+ans+"~~~~~~~~~~~~~~~~~");
					break;			
				}
				case 10:{
					System.out.println("~~~~~~~~~~~~~~~~~~Launching Query "+ans+"~~~~~~~~~~~~~~~~~~");
					executeQuery10(s, myObj);
					System.out.println("~~~~~~~~~~~~~~~~Exiting From Query "+ans+"~~~~~~~~~~~~~~~~~");
					break;			
				}
				case 11:{
					System.out.println("~~~~~~~~~~~~~~~~~~Launching Query "+ans+"~~~~~~~~~~~~~~~~~~");
					executeQuery11(s);
					System.out.println("~~~~~~~~~~~~~~~~Exiting From Query "+ans+"~~~~~~~~~~~~~~~~~");
					break;			
				}
				default:{
					System.out.println("Wrong Input");
					break;
				}
			}
		}
		
	}

	private static Session getSession() {
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		return session;
	}

	private static void executeQuery11(Session s) {
		// TODO Auto-generated method stub
		
		boolean atleastOneToDisplay = false;
		Query q = s.createQuery("from ServicesGroup");
		List<ServicesGroup> list = (List<ServicesGroup>) q.list();
		System.out.println("Ids & Title of Services Group without any VolunteerServices:");
		System.out.println();
		System.out.println("-------------");
		System.out.println("  Ids | Title");
		System.out.println("-------------");
		for (ServicesGroup sg : list) {
			if(sg.getVolunteerService().size() == 0) {
				System.out.printf("%5d | ",sg.getId());
				System.out.println(sg.getTitle());
				atleastOneToDisplay = true;
			}
		}
		if(!atleastOneToDisplay) {
			System.out.println("System is good, All Services Group have atleast 1 Volunteer Services in it.");
		}
		
	}

	private static void executeQuery10(Session s, Scanner myObj) {
		// TODO Auto-generated method stub
		Query q2 = s.createQuery("from Event where category='Upcoming'");
		List<Event> events = q2.list();
		Event nextEvent = null;
		if(events.size() != 0) {
			Date bestDate = null;
			int count = 1;
			for(Event e : events) {
				for(DayOfEvent doe : e.getDayOfEvent()) {
					if("Day1".compareTo(doe.getTitleOfDay()) == 0) {
						if(count == 1) {
							System.out.println("up");
							bestDate = doe.getDate();
							nextEvent = e;
							count++;
						}
						else {
							System.out.println(doe.getDate()+"  -  "+bestDate);
							if(doe.getDate().before(bestDate)) {
								bestDate = doe.getDate();
								nextEvent = e;
							}
						}
					}
				}
			}
		}
		// We know the next Event
		
		Query q = s.createQuery("from MandirPeople Order By id");
		List<MandirPeople> list = (List<MandirPeople>) q.list();
		boolean again = false;
		do{
			if(list.size() > 0) {
				System.out.println();
				System.out.println("------------");
				System.out.println("  Ids | Name");
				System.out.println("------------");
				for(MandirPeople mp : list) {
					System.out.printf("%5d | ",mp.getId());
					System.out.println(mp.getFullName());
				}
				System.out.println();
				System.out.print("Give person's Id, whose lessons you want to see: ");
				MandirPeople mp = s.get(MandirPeople.class,myObj.nextInt());					// 2 19 .....
				if(mp == null) {
					System.out.println("Please Select Correct Person Id From The List");
					again = true;
				}
				else {
					again = false;
					Query q3 = s.createQuery("from Lesson where mandirPeople.id="+mp.getId());			 // or  mp.getLesson();
					List<Lesson> lessons = q3.list();
					if(lessons.size() != 0) {
						
						System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
						System.out.println("General Lessons written by: "+mp.getFullName());
						System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
						for(Lesson l : lessons) {
							if("General".compareTo(l.getCategory()) == 0) {
								System.out.print(l.getDate()+"  | ");
								System.out.println(l.getDescription());
							}
						}
						System.out.println();

						
						System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
						System.out.println("Activity Lessons written by: "+mp.getFullName());
						System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
						for(Lesson l : lessons) {
							if("Activity".compareTo(l.getCategory()) == 0) {
								System.out.printf("%s | ",s.get(ActivityLesson.class, l.getId()).getActivity().getTitle());
								System.out.print(l.getDate()+"  | ");
								System.out.println(l.getDescription());
							}
						}
						System.out.println();
						
						if(nextEvent != null) {
							for(Lesson l : lessons) {
								if("Event".compareTo(l.getCategory()) == 0) {
									if(s.get(EventLesson.class, l.getId()).getEvent().getTitle().compareTo(nextEvent.getTitle()) == 0) {
										System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
										System.out.println("Event Lessons written by: "+mp.getFullName());
										System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
										System.out.printf("%s | ",nextEvent.getTitle());
										System.out.print(l.getDate()+"  | ");
										System.out.println(l.getDescription());
									}
								}
							}
						}
						else {
							System.out.println("No Upcoming Events yet");
						}
						System.out.println();

					}
					else {
						System.out.println("No Lessons from: "+mp.getFullName());
					}
					
				}
			}
			else {
				System.out.println("No Mandir People, So No Lessons");
			}
		}while(again);
	}

	private static void executeQuery9(Session s, Scanner myObj) {
		Query q = s.createQuery("select distinct title from Event Order By id");
		List<Object[]> eventNames = q.list();
		if(eventNames.size() != 0) {
			boolean again = false;
			do {
				System.out.printf("%5s | Title", "No");
				System.out.println();
				System.out.println("--------------");
				for(int i = 0; i<eventNames.size(); i++) {
					System.out.printf("%5d | ",i+1);
					System.out.println(eventNames.get(i));
				}
				System.out.print("Give Title No: ");
				int no = myObj.nextInt();
				if(no>=1 && no<= eventNames.size()) {
					again = false;
					Query q2 = s.createQuery("from Event where title='"+eventNames.get( no-1 )+"' AND  year=(select max(year) from Event)");
					Event e = (Event) q2.uniqueResult();
					System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
					System.out.println("Details of Lastly Occured Event: "+e.getTitle()+" in "+e.getYear());
					System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
					System.out.println("Title: "+e.getTitle());
					System.out.println();
					System.out.println("Total Days: "+e.getDayOfEvent().size());
					List<DayOfEvent> list = new ArrayList<DayOfEvent>(e.getDayOfEvent());
					Comparator<DayOfEvent> compareByDate = (DayOfEvent d1, DayOfEvent d2) -> d1.getDate().compareTo( d2.getDate() );
					Collections.sort(list, compareByDate);
					for (DayOfEvent doe: list) {
						System.out.println("\tTitle of the Day: "+ doe.getTitleOfDay());
						System.out.println("\tDate of the Day: "+doe.getDate());
						System.out.println("\tDay of the Week: "+doe.getWeekDay());
						System.out.println("\tNo of Programs on "+ doe.getTitleOfDay() +" are: "+ doe.getProgram().size());
						for(Program p : doe.getProgram()) {
							System.out.println("\t\tTitle of the Program: "+ p.getTitle());
							SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
							System.out.print("\t\tTime of the Program: "+ formatter.format(p.getFromTime()) + " - ");
							System.out.println(formatter.format(p.getToTime()));
						}
						System.out.println();
					}
					System.out.println("Venue: "+e.getVenue());
					System.out.println();
					System.out.println("Total Flyers: "+e.getFlyerUrl().size());
					for(EventFlyerUrl efu : e.getFlyerUrl())
						System.out.println("\tFlyer Url: "+efu.getFlyerUrl());
					System.out.println();
					System.out.println("Total Seva: "+ e.getSeva().size());
					
					for(Seva seva : e.getSeva()) {
						System.out.println("\tSeva Title: "+seva.getSevaCategory());
						System.out.println("\tSeva Amount: "+seva.getSevaAmount());
					}
					
					if(e.getSponsorship().size() != 0) {
						System.out.println();
						System.out.println("Sponsorship: "+e.getSponsorship().size());
						for(Sponsorship sp : e.getSponsorship()) {
							System.out.println("\tSponsorship Category: "+sp.getSponsorshipCategory());
							System.out.println("\tSponsorship Amount: "+sp.getSponsorshipAmount());
						}
					}
					
					if(e.getTicket().size() != 0) {
						System.out.println();
						System.out.println("Ticket: "+e.getTicket().size());
						for(Ticket t : e.getTicket()) {
							System.out.println("\tTicket Category: "+t.getTicketCategory());
							System.out.println("\tTicket Amount: "+t.getTicketAmount());
						}
					}
					
					if(e.getContactUs().size() != 0) {
						System.out.println();
						System.out.println("ContactUs: "+e.getContactUs().size());
						for(MandirPeople mp : e.getContactUs()) {
							System.out.println("\t"+mp.getFullName()+": "+mp.getNumber());
						}
					}
					
					if(e.getNote().size() != 0) {
						System.out.println();
						System.out.println("Total Notes: "+e.getContactUs().size());
						for(EventNote n : e.getNote()) {
							System.out.println("\tNote: "+n.getNote());
						}
					}
				}
				else {
					again = true;
				}
			}while(again);
		}
		else {
			System.out.println("No Events in Database");
		}
	}
	

	private static void executeQuery8(Session s) {
		// TODO Auto-generated method stub
		Query q = s.createQuery("select distinct title from Event Order By id");
		List<Object[]> eventNames = q.list();
		if(eventNames.size() != 0) {
			boolean again = false;
			System.out.printf("%5s | Title", "No");
			System.out.println();
			System.out.println("--------------");
			for(int i = 0; i<eventNames.size(); i++) {
				System.out.printf("%5d | ",i+1);
				System.out.println(eventNames.get(i));
			}
		}
	}

	private static void executeQuery7(Session s, Scanner myObj) {
		// TODO Auto-generated method stub
		
		Query q = s.createQuery("from MandirPeople Order By id");
		List<MandirPeople> list = (List<MandirPeople>) q.list();
		boolean again = false;
		do{
			if(list.size() > 0) {
				System.out.println();
				System.out.println("------------");
				System.out.println("  Ids | Name");
				System.out.println("------------");
				for(MandirPeople mp : list) {
					System.out.printf("%5d | ",mp.getId());
					System.out.println(mp.getFullName());
				}
				System.out.println();
				System.out.print("Give person's Id, whose lessons you want to see: ");
				MandirPeople mp = s.get(MandirPeople.class,myObj.nextInt());					// 2 19 .....
				if(mp == null) {
					System.out.println("Please Select Correct Person Id From The List");
					again = true;
				}
				else {
					again = false;
					Query q3 = s.createQuery("from Lesson where mandirPeople.id="+mp.getId());			 // or  mp.getLesson();
					List<Lesson> lessons = q3.list();
					if(lessons.size() != 0) {
						
						System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
						System.out.println("General Lessons written by: "+mp.getFullName());
						System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
						for(Lesson l : lessons) {
							if("General".compareTo(l.getCategory()) == 0) {
								System.out.print(l.getDate()+"  | ");
								System.out.println(l.getDescription());
							}
						}
						System.out.println();

						
						System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
						System.out.println("Activity Lessons written by: "+mp.getFullName());
						System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
						for(Lesson l : lessons) {
							if("Activity".compareTo(l.getCategory()) == 0) {
								System.out.printf("%s | ",s.get(ActivityLesson.class, l.getId()).getActivity().getTitle());
								System.out.print(l.getDate()+"  | ");
								System.out.println(l.getDescription());
							}
						}
						System.out.println();

						
						System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
						System.out.println("Event Lessons written by: "+mp.getFullName());
						System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
						for(Lesson l : lessons) {
							if("Event".compareTo(l.getCategory()) == 0) {
								System.out.printf("%s | ",s.get(EventLesson.class, l.getId()).getEvent().getTitle());
								System.out.print(l.getDate()+"  | ");
								System.out.println(l.getDescription());
							}
						}
						System.out.println();
					}
					else {
						System.out.println("No Lessons from: "+mp.getFullName());
					}
					
				}
			}
			else {
				System.out.println("No Mandir People, So No Lessons");
			}
		}while(again);
	}

	private static void executeQuery6(Session s) {
		// TODO Auto-generated method stub
		Query q = s.createQuery("from Parcha where publication='null'");
		List<Parcha> ps = q.list();
		if(ps.size() == 0) {
			System.out.println("No Parchas Remaining to be Publised So Far");
		}
		else {
			for(Parcha p : ps)
				System.out.println("Parcha id: "+p.getId()+" not yet published");
		}
	}

	private static void executeQuery5(Session s) {
		// TODO Auto-generated method stub
		Query q2 = s.createQuery("from Event where category='Upcoming'");
		List<Event> es = (List<Event>) q2.list();
		for(Event e: es) {
			System.out.println("======================NEW EVENT=============================");
			System.out.println("======================NEW EVENT=============================");
			System.out.println("======================NEW EVENT=============================");
			System.out.println("Title: "+e.getTitle());
			System.out.println();
			System.out.println("Total Days: "+e.getDayOfEvent().size());
			List<DayOfEvent> list = new ArrayList<DayOfEvent>(e.getDayOfEvent());
			Comparator<DayOfEvent> compareByDate = (DayOfEvent d1, DayOfEvent d2) -> d1.getDate().compareTo( d2.getDate() );
			Collections.sort(list, compareByDate);
			for (DayOfEvent doe: list) {
				System.out.println("\tTitle of the Day: "+ doe.getTitleOfDay());
				System.out.println("\tDate of the Day: "+doe.getDate());
				System.out.println("\tDay of the Week: "+doe.getWeekDay());
				System.out.println("\tNo of Programs on "+ doe.getTitleOfDay() +" are: "+ doe.getProgram().size());
				for(Program p : doe.getProgram()) {
					System.out.println("\t\tTitle of the Program: "+ p.getTitle());
					SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
					System.out.print("\t\tTime of the Program: "+ formatter.format(p.getFromTime()) + " - ");
					System.out.println(formatter.format(p.getToTime()));
				}
				System.out.println();
			}
			System.out.println("Venue: "+e.getVenue());
			System.out.println();
			System.out.println("Total Flyers: "+e.getFlyerUrl().size());
			for(EventFlyerUrl efu : e.getFlyerUrl())
				System.out.println("\tFlyer Url: "+efu.getFlyerUrl());
			System.out.println();
			System.out.println("Total Seva: "+ e.getSeva().size());
			
			for(Seva seva : e.getSeva()) {
				System.out.println("\tSeva Title: "+seva.getSevaCategory());
				System.out.println("\tSeva Amount: "+seva.getSevaAmount());
			}
			
			if(e.getSponsorship().size() != 0) {
				System.out.println();
				System.out.println("Sponsorship: "+e.getSponsorship().size());
				for(Sponsorship sp : e.getSponsorship()) {
					System.out.println("\tSponsorship Category: "+sp.getSponsorshipCategory());
					System.out.println("\tSponsorship Amount: "+sp.getSponsorshipAmount());
				}
			}
			
			if(e.getTicket().size() != 0) {
				System.out.println();
				System.out.println("Ticket: "+e.getTicket().size());
				for(Ticket t : e.getTicket()) {
					System.out.println("\tTicket Category: "+t.getTicketCategory());
					System.out.println("\tTicket Amount: "+t.getTicketAmount());
				}
			}
			
			if(e.getContactUs().size() != 0) {
				System.out.println();
				System.out.println("ContactUs: "+e.getContactUs().size());
				for(MandirPeople mp : e.getContactUs()) {
					System.out.println("\t"+mp.getFullName()+": "+mp.getNumber());
				}
			}
			
			if(e.getNote().size() != 0) {
				System.out.println();
				System.out.println("Total Notes: "+e.getContactUs().size());
				for(EventNote n : e.getNote()) {
					System.out.println("\tNote: "+n.getNote());
				}
			}
		}
		System.out.println("======================EVENTS OVER=============================");
	}

	private static void executeQuery4(Session s) {
		// TODO Auto-generated method stub
		Query q = s.createQuery("from Authority");
		List<Authority> as = (List<Authority>) q.list();
		for(Authority a : as) {
			System.out.println("People Authorized for: "+a.getTitle());
			System.out.println("\tMandir People:");
			System.out.println("\t--------------");
			if(a.getMandirPeople().size() == 0) {
				System.out.println("\tNo Mandir People Associated so far");
			}
			else {
				for(MandirPeople mp :a.getMandirPeople()) {
					System.out.printf("\t"+mp.getNumber()+": %s", mp.getFullName());
					System.out.println();
				}
			}
			System.out.println();
			System.out.println("\tCommittee Member:");
			System.out.println("\t-----------------");
			if(a.getPost() == null) {
				System.out.println("\tNo Committee Member Associated so far");
			}
			else {
				for(MandirPeople mp : a.getPost().getCommitteeMember()) {
					System.out.printf("\t"+mp.getNumber()+": %s", mp.getFullName());
					System.out.println();
				}
				if(a.getPost().getCommitteeMember().size() == 0) {
					System.out.println(a.getPost().getTitle()+" is managing the "+a.getTitle()+" but no committee memeber associated to that post currently");
				}
			}
			System.out.println();
			System.out.println();
		}
	}

	private static void executeQuery3(Session s) {
		// TODO Auto-generated method stub
		Query q = s.createQuery("from VolunteerService");
		List<VolunteerService> vss = (List<VolunteerService>) q.list();
		if(vss.size() != 0) {
			for(VolunteerService vs : vss) {
				System.out.println();
				System.out.println("-----------------------------------------");
				System.out.println("Volunteers for "+vs.getTitle());
				System.out.println("-----------------------------------------");
				if(vs.getVolunteer().size() == 0) {
					System.out.println("No volunteers so far for this Volunteer Service");
				}
				else {
					for(MandirPeople mp : vs.getVolunteer()) {
						System.out.printf("\t"+mp.getNumber()+": %s", mp.getFullName());
						System.out.println();
					}
				}
			}
		}
		else {
			System.out.println("No VolunteerServices");
		}
	}

	private static void executeQuery2(Session s) {
		// TODO Auto-generated method stub
		Query q = s.createQuery("from Authority where post='null'");
		List<Authority> as = (List<Authority>) q.list();
		int count = 0;
		for(Authority a : as) {
			if(a.getMandirPeople().size() == 0) {
				System.out.println("No one Associated for: "+a.getTitle());
				count++;
			}
		}
		if(count == 0) {
			System.out.println("Great! You have all authorities assigned");
		}
	}

	private static void executeQuery1(Session s) {
		// TODO Auto-generated method stub
		Query q = s.createQuery("from VolunteerService");
		List<VolunteerService> vss = (List<VolunteerService>) q.list();
		if(vss.size() != 0) {
			System.out.println("-----------------------------------------");
			for (VolunteerService vs : vss) {
				if(vs.getVolunteer().size() == 0) {
					System.out.println("No Volunteers for "+vs.getTitle());
				}
			}
			System.out.println("-----------------------------------------");
		}
		else{
			System.out.println("No VolunteerServices");
		}
	}

}
