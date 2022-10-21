package controleur;

import modele.Plan;

public class Main {

	public static void main(String[] args) {
		String file = "data\\largeMap.xml";
		Plan p = new Plan(file);
		p.parseXML();
	}
	
}
