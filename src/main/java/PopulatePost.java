import org.hibernate.Session;
import org.shreejalarammandir.model.mandirpeople.Post;

public class PopulatePost {

	public static void populatePost(Session session) {
		String postTitle[] = {
				"",
				"Vice Chairman",
				"General Secretary",
				"Comptroller",
				"Treasurer",
				"Office",
				"Bhajan",
				"Cultural",
				"Maintenance",
				"Kitchen",
				
				"President",
				"Vice President",
				"Secretary",
				"Jt. Cultural",
				"Event Planner",
				"Bal Vihar"
		};
		for (int i = 1; i < postTitle.length; i++) {
			Post post = new Post();
			post.setTitle(postTitle[i]);
			session.save(post);
		}
	}

}
