import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.shreejalarammandir.model.mandirpeople.MandirPeople;
import org.shreejalarammandir.model.publication.Parcha;

public class PopulateParcha {

	public static void populateParcha(Session session) {
		Query query = session.createQuery("from MandirPeople");
		List<MandirPeople> list = (List<MandirPeople>) query.list();
		Random r = new Random();
		for (int i = 1; i <= 150; i++) {
			MandirPeople mp = new MandirPeople();
			Parcha p = new Parcha();
			
			p.setDate(Date.valueOf( "2019-"+ (r.nextInt(12)+1) +"-"+ (r.nextInt(28)+1) ));
			p.setDescription("Despription of parcha according to people who submits");
			p.setEmail(list.get(i).getEmail());
			p.setName(list.get(i).getFullName().toString());
			session.save(p);
		}
	}
	
}
