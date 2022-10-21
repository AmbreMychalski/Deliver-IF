package modele;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Plan {

	Intersection entrepot;
	String nom;
	String nomFichier;
	Map<Long, Intersection> intersections =  new HashMap<Long, Intersection>();
	List<Segment> segments =  new ArrayList<Segment>();
	
	public Plan(String nomFichier) {
		String [] split_text = nomFichier.split("\\\\");
		nom = split_text[split_text.length -1 ].split(Pattern.quote("."))[0];
	
		this.nomFichier = nomFichier;
	}
	
	public void parseXML() {
		Node node = null;
		NodeList list=null;	
		
		try {

			DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = fact.newDocumentBuilder();
			Document doc = builder.parse(this.nomFichier);
			Element el = doc.getDocumentElement();
			list = el.getChildNodes();
			NamedNodeMap attributs;
			Long entrepotId = null;
			
			for (int i=0; i<list.getLength(); i++) {
				node = list.item(i);
				if(node.getNodeType() == Node.ELEMENT_NODE) {
					
					if(node.getNodeName() == "warehouse") {
						
						entrepotId = Long.parseLong(node.getAttributes().getNamedItem("address").getNodeValue());

					}
					else if(node.getNodeName() == "intersection") {
						
						Long intersectionId; float latitude; float longitude;
						attributs = node.getAttributes();
						intersectionId = Long.parseLong(attributs.getNamedItem("id").getNodeValue());
						latitude = Float.parseFloat(attributs.getNamedItem("latitude").getNodeValue());
						longitude = Float.parseFloat(attributs.getNamedItem("longitude").getNodeValue());
						
						this.intersections.put(intersectionId, new Intersection(intersectionId, latitude, longitude));
					}
					else if(node.getNodeName()== "segment") {
						
						Long destinationId; float longueur; String nom; Long origineId;
						attributs = node.getAttributes();
						destinationId = Long.parseLong(attributs.getNamedItem("destination").getNodeValue());
						origineId = Long.parseLong(attributs.getNamedItem("origin").getNodeValue());
						nom = attributs.getNamedItem("name").getNodeValue();
						longueur = Float.parseFloat(attributs.getNamedItem("length").getNodeValue());
						
						segments.add(new Segment(this.intersections.get(origineId), this.intersections.get(destinationId), longueur, nom));
					}
				}
			}
			this.entrepot = this.intersections.get(entrepotId);
		}
		catch(Exception e) {
			System.err.println("Erreur lors du parsing du fichier");
		}
	}	
}

