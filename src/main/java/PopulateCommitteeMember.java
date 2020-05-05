import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.shreejalarammandir.model.mandirpeople.Authority;
import org.shreejalarammandir.model.mandirpeople.CommitteeMember;
import org.shreejalarammandir.model.mandirpeople.FullName;
import org.shreejalarammandir.model.mandirpeople.Post;

public class PopulateCommitteeMember {

	public static void populateCommitteeMember(Session session) {
		String[][] fNameLNameTrustBoardWithPost = {
				{""},
				{"", "Ashish", "Thakkar", "Vice Chairman"},
				{"", "Yogesh", "Thakkar", "General Secretary"},
				{"", "Jashvant", "Patel", "Comptroller"},
				{"", "Bhupendra", "Thakkar", "Treasurer"},
				{"", "Ashwin", "Thakkar", "Office"},
				{"", "Rajendra", "Thakkar", "Bhajan"},
				{"", "Mahesh", "Patel",	"Cultural"},
				{"", "Hasmukh", "Thakkar", "Maintenance"},
				{"", "Chandulal", "Thakkar", "Maintenance"},
				{"", "Vijay", "Thakkar", "Kitchen"},
		};
		String[][] fNameLNameExecutiveBoardWithPost = {
				{},
				{"", "Dipak", "Thakkar", "President"},
				{"", "Chirayu", "Parikh", "Vice President"},
				{"", "Jayshree", "Thakkar", "Secretary"},
				{"", "Bhavna", "Thakkar", "Cultural"},
				{"", "Nayna", "Soni", "Jt. Cultural"},
				{"", "Chandrakant", "Thakkar", "Kitchen"},
				{"", "Dhaval", "Thakkar", "Kitchen"},
				{"", "Alpesh", "Thakkar", "Kitchen"},
				{"", "Ghanshyam", "Thakkar", "Maintenance"},
				{"", "Rajesh", "Thakkar", "Event Planner"},
				{"", "Jigna", "Thakkar", "Bal Vihar"}
		};
		String areaCode[] = {"","224", "847", "331", "708", "630", "773", "312"};
		CommitteeMember cm;
		Random r = new Random();
		
		for (int i = 1; i < fNameLNameTrustBoardWithPost.length; i++) {
			cm = new CommitteeMember();
			cm.setCategory("Committee Member");
			cm.setEmail( (fNameLNameTrustBoardWithPost[i][1]+"."+fNameLNameTrustBoardWithPost[i][2]+"@gmail.com").toLowerCase() );
			String number = areaCode[r.nextInt(areaCode.length-1)+1];
			for (int j = 1; j <= 7; j++) {
				number = number + r.nextInt(10);
			}
			cm.setNumber(number);
			cm.setPassword((fNameLNameTrustBoardWithPost[i][1]+fNameLNameTrustBoardWithPost[i][2]).toLowerCase());
			cm.setAuthorized("Authorized");
			cm.setFullName(new FullName(fNameLNameTrustBoardWithPost[i][1], fNameLNameTrustBoardWithPost[i][2]));
			cm.setPicUrl("img/mandirPeople");
			cm.setStartDate(Date.valueOf("2019-11-25"));
			cm.setBoard("Trust");
			Query query = session.createQuery("from Post where title = '"+fNameLNameTrustBoardWithPost[i][3]+"'");
			Post p = (Post) query.uniqueResult();
			cm.getPost().add(p);
			p.getCommitteeMember().add(cm);
			session.save(cm);
			session.update(p);
		}
		
		for (int i = 1; i < fNameLNameExecutiveBoardWithPost.length; i++) {
			cm = new CommitteeMember();
			cm.setCategory("Committee Member");
			cm.setEmail(fNameLNameExecutiveBoardWithPost[i][1]+"."+fNameLNameExecutiveBoardWithPost[i][2]+"@gmail.com");
			String number = areaCode[r.nextInt(areaCode.length-1)+1];
			for (int j = 1; j <= 7; j++) {
				number = number + r.nextInt(10);
			}
			cm.setNumber(number);
			cm.setPassword((fNameLNameExecutiveBoardWithPost[i][1]+fNameLNameExecutiveBoardWithPost[i][2]).toLowerCase());
			cm.setAuthorized("Authorized");
			cm.setFullName(new FullName(fNameLNameExecutiveBoardWithPost[i][1], fNameLNameExecutiveBoardWithPost[i][2]));
			cm.setPicUrl("img/mandirPeople");
			cm.setStartDate(Date.valueOf("2019-11-25"));
			cm.setBoard("Executive");
			Query query = session.createQuery("from Post where title = '"+fNameLNameExecutiveBoardWithPost[i][3]+"'");
			Post p = (Post) query.uniqueResult();
			cm.getPost().add(p);
			p.getCommitteeMember().add(cm);
			session.save(cm);
			session.update(p);
		}
		
		// Assigning Authority
		Query query = session.createQuery("from Post where title = 'Chairman'");
		Post p = (Post) query.uniqueResult();
		
		String authorityForChairman[] = {
			"",
			"Authorizing Domains",
			"Committee & Mahraj",
			"Post",
			"Event Without Flyer",
			"Venue",
			"Activity Without Session",
			"ServicesGroup and Volunteer Services",
			"Pooja",
		};
		
		for (int i = 1; i < authorityForChairman.length; i++) {
			query = session.createQuery("from Authority where title ='"+ authorityForChairman[i] +"'");
			Authority a = (Authority) query.uniqueResult();
			p.getAuthority().add(a);
			a.setPost(p);
			session.update(a);
			session.update(p);
		}
		
	}

}
