import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.shreejalarammandir.model.mandirpeople.MandirPeople;
import org.shreejalarammandir.model.pooja.Pooja;
import org.shreejalarammandir.model.pooja.PoojaNote;

public class PopulatePandPN {

	public static void populatePandPN(Session session) {
		Random r = new Random();
		Pooja p;
		Query query = session.createQuery("from MandirPeople where fullName.fName = 'Bhupendra' OR fullName.fName = 'Keyur' OR fullName.fName = 'Dharmendra'");
		List<MandirPeople> list = (List<MandirPeople>) query.list();
		Set<MandirPeople> set = new HashSet<MandirPeople>(list);
		String[] poojaTitle = {"", "Besnu", "Bhajan", "Rental", "Advt", "Tulsiji", "Car Pooja", "SatyaNarayan Katha"};
		for (int i = 1; i < poojaTitle.length; i++) {
			 p = new Pooja();
			 p.setSeva( "" + ((r.nextInt(5)+1) * 10) );
			 p.setTitle(poojaTitle[i]);
			 
			 p.setContactUs(set);
			 for (MandirPeople mp: set) {
					mp.getPoojaContactUs().add(p);
					session.update(mp);
			 }
			 
			 if(poojaTitle[i] == "Rental") {
				 PoojaNote pn = new PoojaNote();
				 pn.setNote("We offer Bhajans with an external fee");
				 p.getNote().add(pn);
				 pn.setPooja(p);
			 }
			 session.save(p);
		}
		
	}

}
