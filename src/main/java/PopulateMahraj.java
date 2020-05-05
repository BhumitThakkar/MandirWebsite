import org.hibernate.Session;
import org.shreejalarammandir.model.mandirpeople.FullName;
import org.shreejalarammandir.model.mandirpeople.Mahraj;

public class PopulateMahraj {

	public static void populateMahraj(Session session) {
		Mahraj m1 = new Mahraj();
		Mahraj m2 = new Mahraj();
		
		m1.setAuthorized("Authorized");
		m1.setCategory("Mahraj");
		m1.setEmail("jalarampa@gmail.com");
		m1.setFullName(new FullName("Dharmendra", "Pandya"));
		m1.setNumber("7175980685");
		m1.setPassword("dharmendrapandya");
		m1.setPicUrl("img/mandirPeople");
		
		m2.setAuthorized("Authorized");
		m2.setCategory("Mahraj");
		m2.setEmail("keyur.usa1987@gmail.com");
		m2.setFullName(new FullName("Keyur", "Sevak"));
		m2.setNumber("2245183300");
		m2.setPassword("keyursevak");
		m2.setPicUrl("img/mandirPeople");
		
		session.save(m1);
		session.save(m2);
	}
}
