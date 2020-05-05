import java.sql.Date;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.shreejalarammandir.model.mandirpeople.CommitteeMember;
import org.shreejalarammandir.model.mandirpeople.FullName;
import org.shreejalarammandir.model.mandirpeople.Post;

public class PopulateChairman {
	
	public static void populateChairman(Session session) {
		CommitteeMember cm = new CommitteeMember();
		cm.setCategory("Committee Member");
		cm.setEmail("bakulthakkar@comcast.net");
		cm.setNumber("8479519509");
		cm.setPassword("bakulthakkar");
		cm.setAuthorized("Authorized");
		cm.setFullName(new FullName("Bakul", "Thakkar"));
		cm.setPicUrl("img/mandirPeople");
		cm.setStartDate(Date.valueOf("2019-11-25"));
		cm.setBoard("Trust");
		Query query = session.createQuery("from Post where title = 'Chairman' ");
		Post p = (Post) query.uniqueResult();
		cm.getPost().add(p);
		p.getCommitteeMember().add(cm);
		session.save(cm);
		session.update(p);
	}

}
