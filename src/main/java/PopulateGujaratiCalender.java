import org.hibernate.Session;
import org.shreejalarammandir.model.gujaraticalender.GujaratiCalender;

public class PopulateGujaratiCalender {

	public static void populateGujaratiCalender(Session session) {
		String month[] = { "", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
		for (int i = 1; i < month.length; i++) {
			GujaratiCalender gc = new GujaratiCalender();
			gc.setMonth(month[i]);
			gc.setYear(2020);
			String picUrl = System.getProperty("user.dir") + "\\src\\main\\webapp\\img\\calender\\" + month[i]+"2020";
			gc.setPicUrl(picUrl);
			session.save(gc);
		}
	}

}
