import java.util.Random;

import org.hibernate.Session;
import org.shreejalarammandir.model.mandirpeople.ServicesGroup;
import org.shreejalarammandir.model.mandirpeople.VolunteerService;

public class PopulateSGandVS {
	
	public static void populateSGandVS(Session session) {
		for (int i = 1; i <= 150; i++) {
			ServicesGroup sg = new ServicesGroup();
			String sgTitle = "ServiceGroup"+i;
			sg.setTitle(sgTitle);
			
			Random r = new Random();
			VolunteerService vs;
			for (int j = 1; j <= r.nextInt(3); j++) {							// r = 0-2
				vs = new VolunteerService();
				String vsTitle = "VolunteerService"+j+"_"+i;
				vs.setTitle(vsTitle);
				vs.setServicesGroup(sg);
				sg.getVolunteerService().add(vs);
			}
			session.save(sg);
		}
	}

}
