import java.util.List;
import java.util.Random;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.shreejalarammandir.model.mandirpeople.Authority;
import org.shreejalarammandir.model.mandirpeople.FullName;
import org.shreejalarammandir.model.mandirpeople.Post;
import org.shreejalarammandir.model.mandirpeople.Volunteer;
import org.shreejalarammandir.model.mandirpeople.VolunteerService;

public class PopulateVolunteer {
	
	public static void populateVolunteer(String name[][],Session session) {
		
		String areaCode[] = {"","224", "847", "331", "708", "630", "773", "312"};
		Random r = new Random();
		Volunteer v;
		for (int i = 1; i < 150; i++) {
			v = new Volunteer();
			v.setCategory("Volunteer");
			v.setFullName(new FullName(name[i][1], name[i][2]));
			
			String number = areaCode[r.nextInt(areaCode.length-1)+1];
			for (int j = 1; j <= 7; j++) {
				number = number + r.nextInt(10);
			}
			v.setNumber(number);
			
			v.setPassword( (name[i][1]+name[i][2]).toLowerCase() );
			v.setEmail((name[i][1]+"."+name[i][2]+"@gmail.com").toLowerCase() );
			v.setPicUrl("img/mandirPeople");
			
			session.save(v);
			
			Query q = session.createQuery("from VolunteerService");
			List<VolunteerService> vss = (List<VolunteerService>) q.list();
			for (int registration = r.nextInt(2); registration <= 2 ;registration++) {
				VolunteerService vs = vss.get(r.nextInt(vss.size()));
				if(! v.getVolunteerService().contains(vs)) {
					v.getVolunteerService().add(vs);
					vs.getVolunteer().add(v);
					session.update(vs);
				}
			}
			
			if(r.nextInt(2) == 0) {
				// Don't add Authority
			}
			else {
				String[] authorityTitle = {"",
					"Flyer",
					"Activity Session",
					"Gujarti Calender",
					"Parcha & Publication",
					"Subscriber For Emailing",
					"ImagesCompulsory"
				};
				Query query = session.createQuery("from Authority where title = '"+ authorityTitle[r.nextInt(6)+1] +"'");						
				Authority a = (Authority) query.uniqueResult();
				v.getAuthority().add(a);
				a.getMandirPeople().add(v);
				session.update(a);
			}
			session.update(v);
		}
	}
	
}
