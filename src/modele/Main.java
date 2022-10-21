package modele;

public class Main {

	public static void main(String[] args) {
		String file = "C:\\Users\\admin\\Desktop\\fichiersXML2022\\largeMap.xml";
		Plan p = new Plan(file);
		p.parseXML();

	}
	
}
