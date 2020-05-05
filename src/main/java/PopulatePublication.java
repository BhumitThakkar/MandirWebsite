
import java.util.Date;
import java.util.HashSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.shreejalarammandir.model.publication.Parcha;
import org.shreejalarammandir.model.publication.Publication;

public class PopulatePublication {

	public static void populatePublication(Session session) {
		String[] publishingMonths = {
				"", "March", "June", "September", "December"
		};
		
		Query query = session.createQuery("from Parcha");
		List<Parcha> list = (List<Parcha>) query.list();
		Set<Parcha> set1 = new HashSet<Parcha>();
		Set<Parcha> set2 = new HashSet<Parcha>();
		Set<Parcha> set3 = new HashSet<Parcha>();
		Set<Parcha> set4 = new HashSet<Parcha>();
		
		for (int i = 0; i < list.size(); i++) {
			try {
				Date d = new SimpleDateFormat("yyyy-MM-dd").parse( list.get(i).getDate().toString() );
				if( d.before(new SimpleDateFormat("yyyy-MM-dd").parse("2019-03-31")) ) {
					set1.add(list.get(i));
				}
				else if( d.before(new SimpleDateFormat("yyyy-MM-dd").parse("2019-06-31")) ) {
					set2.add(list.get(i));
				}
				else if( d.before(new SimpleDateFormat("yyyy-MM-dd").parse("2019-09-31")) ) {
					set3.add(list.get(i));
				}
				else if( d.before(new SimpleDateFormat("yyyy-MM-dd").parse("2019-12-31")) ) {
					set4.add(list.get(i));
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		
		for (int i = 1; i < publishingMonths.length; i++) {
			Publication p = new Publication();
			switch (publishingMonths[i]) {
				case "March":{
					p.setMonth(publishingMonths[i]);
					for(Parcha pa : set1) {
						p.getParcha().add(pa);
						pa.setPublication(p);
						session.update(pa);
					}
					break;
				}
				case "June":{
					p.setMonth(publishingMonths[i]);
					for(Parcha pa : set2) {
						p.getParcha().add(pa);
						pa.setPublication(p);
						session.update(pa);
					}
					break;
				}
				case "September":{
					p.setMonth(publishingMonths[i]);
					for(Parcha pa : set3) {
						p.getParcha().add(pa);
						pa.setPublication(p);
						session.update(pa);
					}
					break;
				}
				case "December":{
					p.setMonth(publishingMonths[i]);
					for(Parcha pa : set4) {
						p.getParcha().add(pa);
						pa.setPublication(p);
						session.update(pa);
					}
					break;
				}
			}
			
			p.setYear(2019);
			String publicationUrl = System.getProperty("user.dir") + "\\src\\main\\webapp\\publication\\" + publishingMonths[i]+"2019";
			p.setPublicationUrl(publicationUrl);
			session.save(p);
			
		}
	}

}
