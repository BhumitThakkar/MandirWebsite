import org.hibernate.Session;
import org.shreejalarammandir.model.mandirpeople.Authority;

public class PopulateAuthority {

	public static void populateAuthority(Session session) {
		String[] authorityTitle = {"",
			"Authorizing Domains",
			"Committee & Mahraj",
			"Post",
			"Event Without Flyer",
			"Flyer",
			"Venue",
			"Activity Without Session",
			"Activity Session",
			"ServicesGroup and Volunteer Services",
			"Pooja",
			"Gujarti Calender",
			"Parcha & Publication",
			"Subscriber For Emailing",
			"ImagesCompulsory"
		};
		for (int i = 1; i < authorityTitle.length; i++) {
			Authority a = new Authority();
			a.setTitle(authorityTitle[i]);
			session.save(a);
		}
	}

}
