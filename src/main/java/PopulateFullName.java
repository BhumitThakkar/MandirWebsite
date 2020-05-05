import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

public class PopulateFullName {
	
	public static void main(String[] args) {
		populateFullNames();
	}
	
	public static String[][] populateFullNames() {
		String [][] names = new String[301][3];
		
		String csvPathMale = System.getProperty("user.dir") + "\\src\\main\\webapp\\csv\\maleFNames.csv";
		String csvPathFemale = System.getProperty("user.dir") + "\\src\\main\\webapp\\csv\\femaleFNames.csv";
		String csvPathLastName = System.getProperty("user.dir") + "\\src\\main\\webapp\\csv\\lNames.csv";
		try {
			String col1 = null, col2 = null;
			BufferedReader csvLname =  new BufferedReader(new FileReader(csvPathLastName));
			csvLname.readLine();					// To ignore header
			BufferedReader csvMaleFname = new BufferedReader(new FileReader(csvPathMale));
			csvMaleFname.readLine();
			BufferedReader csvFemaleFname = new BufferedReader(new FileReader(csvPathFemale));
			csvFemaleFname.readLine();
			
			Random r;
			int randInt;
			int randLine;
			for(int i = 1; i<= 150; i++) {
				r = new Random();
				randInt = r.nextInt(2);
				if(randInt == 1) {
					randLine = r.nextInt(2) + 1;
					for (int line = 1; line < randLine; line++)
						csvMaleFname.readLine();
					col1 = csvMaleFname.readLine();
				}
				else if(randInt == 0) {
					randLine = r.nextInt(2) + 1;
					for (int line = 1; line < randLine; line++)
						csvFemaleFname.readLine();
					col1 = csvFemaleFname.readLine();
				}
				names[i][1] = col1.split(",")[0];
				
				randLine = r.nextInt(1350/150) + 1;
				for (int line = 1; line < randLine; line++)
					csvLname.readLine();
				col2 = csvLname.readLine();
				names[i][2] = col2.split(",")[0];
				
				System.out.println(i + " " + names[i][1] +" "+ names[i][2]);
			}
			
			csvMaleFname.close();
			csvFemaleFname.close();
			csvLname.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return names;
	}
}
