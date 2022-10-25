package main.modele;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
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

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Plan {

	private Intersection entrepot;
	private String nom;
	private Map<Long, Intersection> intersections =  new HashMap<Long, Intersection>();
	private List<Segment> segments =  new ArrayList<Segment>();
	private Map<Long, List<Segment>> intersectionsNeighbours = new HashMap<Long, List<Segment>>();
	
	
	public Plan(File fichier) {
		this.parseXML(fichier);
	}
	
	public HashMap<Intersection, Float> calculerPlusCourtsChemins(List<Intersection> listIntersections, Intersection depart){
        HashMap<Long, Float> distance = new HashMap<Long, Float>();
        Set<Long> intersectionsAVerifier = new HashSet<Long>();
        Set<Long> intersectionsGrises = new HashSet<Long>();
        Set<Long> intersectionsNoires = new HashSet<Long>();
        for(var entry : intersections.entrySet()) {
            distance.put(entry.getKey(),-1.0f);
        }
        distance.put(depart.getIdIntersection(), 0.0f);
        
        for(Intersection inter: listIntersections) {
            intersectionsAVerifier.add(inter.getIdIntersection());
        }
                
        intersectionsGrises.add(depart.getIdIntersection());
        
        while(intersectionsAVerifier.size()!=0 && intersectionsGrises.size()!=0) {
            
            Long currentInter = obtenirIntersectionLaPlusProche(intersectionsGrises, distance);
            System.out.println("");
            intersectionsGrises.remove(currentInter);
            for(Segment seg : intersectionsNeighbours.get(currentInter) ) {
                Intersection voisin = seg.getDestination();
                Long idVoisin = voisin.getIdIntersection();
                if(!intersectionsNoires.contains(idVoisin)) {
                    if(distance.get(currentInter)+seg.getLongueur()<distance.get(idVoisin)
                            ||distance.get(idVoisin)==-1) {
                        distance.put(idVoisin,distance.get(currentInter)+seg.getLongueur());
                    }                   
                    intersectionsGrises.add(idVoisin);
                }
            }
            intersectionsGrises.remove(currentInter);
            intersectionsAVerifier.remove(currentInter);
            intersectionsNoires.add(currentInter);          
        }
        
        HashMap<Intersection, Float>res = new HashMap<Intersection, Float>();
        for(Intersection inter: listIntersections) {
            res.put(inter, distance.get(inter.getIdIntersection()));
        }       
        
        return res;
    }

	public void parseXML(File fichier) {
		nom = fichier.getName().split(Pattern.quote("."))[0];
		
		Node node = null;
		NodeList list=null;	
		
		try {
			DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = fact.newDocumentBuilder();
			Document doc = builder.parse(fichier);
			Element el = doc.getDocumentElement();
			list = el.getChildNodes();
			NamedNodeMap attributs;
			Long entrepotId = null;
			
			for (int i = 0; i < list.getLength(); i++) {
				node = list.item(i);
				
				if(node.getNodeType() == Node.ELEMENT_NODE) {
					if(node.getNodeName() == "warehouse") {
						entrepotId = Long.parseLong(node.getAttributes().getNamedItem("address").getNodeValue());
					} else if(node.getNodeName() == "intersection") {
						Long intersectionId; 
						float latitude; 
						float longitude;
						
						attributs = node.getAttributes();
						intersectionId = Long.parseLong(attributs.getNamedItem("id").getNodeValue());
						latitude = Float.parseFloat(attributs.getNamedItem("latitude").getNodeValue());
						longitude = Float.parseFloat(attributs.getNamedItem("longitude").getNodeValue());
						
						this.intersections.put(intersectionId, new Intersection(intersectionId, latitude, longitude));
					} else if(node.getNodeName()== "segment") {
						Long destinationId; 
						float longueur; 
						String nom; 
						Long origineId;
						
						attributs = node.getAttributes();
						destinationId = Long.parseLong(attributs.getNamedItem("destination").getNodeValue());
						origineId = Long.parseLong(attributs.getNamedItem("origin").getNodeValue());
						nom = attributs.getNamedItem("name").getNodeValue();
						longueur = Float.parseFloat(attributs.getNamedItem("length").getNodeValue());
						
						segments.add(new Segment(this.intersections.get(origineId), this.intersections.get(destinationId), longueur, nom));
						if(!this.intersectionsNeighbours.containsKey(origineId)) {
                            this.intersectionsNeighbours.put(origineId, new ArrayList<Segment>());
                        }
                        this.intersectionsNeighbours.get(origineId).add(segments.get(segments.size()-1));
					}
				}
			}
			
			this.entrepot = this.intersections.get(entrepotId);
		} catch (Exception e) {
			System.err.println("Erreur lors du parsing du fichier \n"+e);
			this.intersections =  new HashMap<Long, Intersection>();
			this.segments =  new ArrayList<Segment>();
			this.entrepot = null;
		}
	}

	private Long obtenirIntersectionLaPlusProche(Set < Long > intersectionsGrise, HashMap<Long, Float> distance ) {
        Long interPlusProche = null;
        float plusPetiteDist = Float.MAX_VALUE;
        for (Long idInter: intersectionsGrise) {
            float interDist = distance.get(idInter);
            if (interDist < plusPetiteDist) {
                plusPetiteDist = interDist;
                interPlusProche = idInter;
            }
        }
        return interPlusProche;
    }
}

