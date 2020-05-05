import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

import org.hibernate.Session;
import org.shreejalarammandir.model.subscriber.Subscriber;

public class PopulateSubscriber {

	public static void populateSubscriber(Session session) {
		String csvPathLastName = System.getProperty("user.dir") + "\\src\\main\\webapp\\csv\\lNames.csv";
		BufferedReader csvLname;
		Random r = new Random();
		int randLine;

		try {
			csvLname =  new BufferedReader(new FileReader(csvPathLastName));
			csvLname.readLine();					// To ignore header
			for (int i = 1; i <= 150; i++) {
				randLine = r.nextInt(1350/150) + 1;
				
				for (int line = 1; line < randLine; line++)
					csvLname.readLine();
				String line = csvLname.readLine();
				
				Subscriber s = new Subscriber();
				s.setEmail(line.split(",")[0]+"."+i+"@gmail.com");
				session.save(s);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
