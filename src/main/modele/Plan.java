package modele;

import java.io.File;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import exception.FichierNonConformeException;
import exception.IntersectionIntrouvableException;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Plan {

	private Intersection               entrepot;
	private String                     nom;
	private Map<Long, Intersection>    intersections = new HashMap<Long, Intersection>();
	private List<Segment>              segments = new ArrayList<Segment>();
	private Map<Long, List<Segment>> intersectionsVoisines = new HashMap<Long, List<Segment>>();
	private static final Logger        LOGGER = LogManager.getLogger();

	/**
	 * Constructeur de plan.
	 * @param fichier Le fichier à lire
	 * @throws FichierNonConformeException si le fichier n'est pas conforme
	 */
	public Plan(File fichier) throws FichierNonConformeException {
		this.parseXML(fichier);
	}

	/**
	 * Permet de vérifier si une intersection est livrable.
	 * @param intersection L'intersection dont on veut savoir si elle est livrable
	 * @return true si l'intersection est livrable, false sinon
	 */
	public boolean estLivrable(Intersection intersection) {
		if(!sontConnectee(intersection, this.entrepot)) {
			return false;
		} else return sontConnectee(this.entrepot, intersection);
	}

	/**
	 * Permet de calculer le plus court chemin entre deux points
	 * @param depart L'intersection de départ
	 * @param arrivee L'intersection d'arrivée
	 * @return La liste des segments qui composent le plus court chemin entre
	 * le départ et l'arrivée
	 */
	public List<Segment> calculerPlusCourtChemin(Intersection depart, Intersection arrivee) {

		if(depart == arrivee){
			return new LinkedList<Segment>();
		}

	    List <Segment>          chemin = new LinkedList<Segment>();
	    HashMap<Long, Float>    distance = new HashMap<Long, Float>();
        HashMap<Long, Segment>  parents = new HashMap<Long, Segment>(); // <Long idIntersection, Segment segParent>

        Set<Long>               intersectionsGrises = new HashSet<Long>();
        Set<Long>               intersectionsNoires = new HashSet<Long>();
        
        boolean                 arriveeIsBlack = false;
        
	    for(Entry<Long, Intersection> entry : intersections.entrySet()) {
	        distance.put(entry.getKey(), -1.0f);
	    }
	    
	    distance.put(depart.getIdIntersection(), 0f);    
	    intersectionsGrises.add(depart.getIdIntersection());
	    
	    while(intersectionsGrises.size() != 0 && arriveeIsBlack != true) {
	        Long currentInter = obtenirIntersectionLaPlusProche(intersectionsGrises, 
	                                                            distance);
            
            if(intersectionsVoisines.get(currentInter) != null) {
    	        for(Segment seg : intersectionsVoisines.get(currentInter)) {
    	            Intersection successeur = seg.getDestination();
    	           
    	            if(!intersectionsNoires.contains(successeur.getIdIntersection())) {
    	                if(distance.get(currentInter) + seg.getLongueur() 
    	                        < distance.get(successeur.getIdIntersection())
    	                        || distance.get(successeur.getIdIntersection()) == -1.0f) {
    	                    distance.put(successeur.getIdIntersection(), 
    	                                 distance.get(currentInter)+seg.getLongueur());
    	                    parents.put(successeur.getIdIntersection(), seg);
    	                }
    	                
    	                intersectionsGrises.add(successeur.getIdIntersection());
    	            }
    	        }
            }

            intersectionsGrises.remove(currentInter);
	        intersectionsNoires.add(currentInter);
	        
	        if(currentInter == arrivee.getIdIntersection()) {
	            arriveeIsBlack = true;
	        }
	    }
	    
	    Long currentInter = arrivee.getIdIntersection();
	    
	    while(currentInter != depart.getIdIntersection()) {
	        Segment seg = parents.get(currentInter);

	        chemin.add(0, seg);

	        currentInter = seg.getOrigine().getIdIntersection();
	    }
	    return chemin;
	}

	/**
	 * Permet de calculer le plus court chemin entre l'intersection de départ et
	 * chaque élément de la liste
	 * @param listIntersections La liste des intersections que l'on veut atteindre
	 * @param depart L'intersection de départ
	 * @return map contenant, pour chaque élément de la liste d'intersections,
	 * la longueur du plus court chemin à l'intersection de départ
	 */
	public HashMap<Intersection, Float> calculerPlusCourtsChemins(
	        List<Intersection> listIntersections, Intersection depart) {
	    
        HashMap<Long, Float>    distance = new HashMap<Long, Float>();
        Set<Long>               intersectionsAVerifier = new HashSet<Long>();
        Set<Long>               intersectionsGrises = new HashSet<Long>();
        Set<Long>               intersectionsNoires = new HashSet<Long>();
        
        for(Entry<Long, Intersection> entry : intersections.entrySet()) {
            distance.put(entry.getKey(), -1.0f);
        }
        
        distance.put(depart.getIdIntersection(), 0.0f);
        
        for(Intersection inter : listIntersections) {
            intersectionsAVerifier.add(inter.getIdIntersection());
        }
                
        intersectionsGrises.add(depart.getIdIntersection());
        
        while(intersectionsAVerifier.size() != 0
				&& intersectionsGrises.size() != 0) {
            
            Long currentInter = obtenirIntersectionLaPlusProche(intersectionsGrises, distance);

            intersectionsGrises.remove(currentInter);
            
            if(intersectionsVoisines.get(currentInter) != null) {
                for(Segment seg : intersectionsVoisines.get(currentInter)) {
                    Intersection   voisin = seg.getDestination();
                    Long 		 idVoisin = voisin.getIdIntersection();
                    
                    if(!intersectionsNoires.contains(idVoisin)) {
                        if(distance.get(currentInter) + seg.getLongueur() 
                                < distance.get(idVoisin)
                                ||distance.get(idVoisin) == -1) {
                            distance.put(idVoisin,distance.get(currentInter) 
                                    + seg.getLongueur());
                        }                   
                        intersectionsGrises.add(idVoisin);
                    }
                }
            }
            
            intersectionsGrises.remove(currentInter);
            intersectionsAVerifier.remove(currentInter);
            intersectionsNoires.add(currentInter);          
        }
        
        HashMap<Intersection, Float> res = new HashMap<Intersection, Float>();
        
        for(Intersection inter : listIntersections) {
            res.put(inter, distance.get(inter.getIdIntersection()));
        }       
        
        return res;
    }

	/**
	 * Permet la lecture du fichier de plan
	 * @param fichier Le fichier que l'on veut lire
	 * @throws FichierNonConformeException si le fichier n'est pas jugé conforme
	 */
	public void parseXML(File fichier) throws FichierNonConformeException {
        Node            node = null;
        NodeList        list = null;   
        
		nom = fichier.getName().split(Pattern.quote("."))[0];
		
		try {
			DocumentBuilderFactory   fact = DocumentBuilderFactory.newInstance();
			DocumentBuilder          builder = fact.newDocumentBuilder();
			Document                 doc = builder.parse(fichier); 
			Element                  el = doc.getDocumentElement();
            Long                     entrepotId = null;
            NamedNodeMap             attributs;

			list = el.getChildNodes();
            		
			if(list.getLength() < 1) {
			    LOGGER.error("Le fichier XML ne contient pas d'éléments.");
			    throw new Exception();
			}
			
			for (int i = 0; i < list.getLength(); i++) {
			    
				node = list.item(i);
							
				if(node.getNodeType() == Node.ELEMENT_NODE) {
					if(node.getNodeName() == "warehouse") {
						entrepotId = Long.parseLong(node.getAttributes().getNamedItem("address").getNodeValue());
					} else if(node.getNodeName() == "intersection") {
						Long      intersectionId; 
						float     latitude; 
						float     longitude;
						
						attributs = node.getAttributes();
						intersectionId = Long.parseLong(attributs.getNamedItem("id").getNodeValue());
						latitude = Float.parseFloat(attributs.getNamedItem("latitude").getNodeValue());
						longitude = Float.parseFloat(attributs.getNamedItem("longitude").getNodeValue());
						
						this.intersections.put(intersectionId, new Intersection(intersectionId, latitude, longitude));
					} else if(node.getNodeName()== "segment") {
						Long      destinationId; 
						float     longueur; 
						String    nom; 
						Long      origineId;
						
						attributs = node.getAttributes();
						destinationId = Long.parseLong(attributs.getNamedItem("destination").getNodeValue());
						origineId = Long.parseLong(attributs.getNamedItem("origin").getNodeValue());
						nom = attributs.getNamedItem("name").getNodeValue();
						longueur = Float.parseFloat(attributs.getNamedItem("length").getNodeValue());
						
						if(intersections.get(destinationId) == null 
						        || intersections.get(origineId) == null) {
						    LOGGER.error("La destination ou l'origine d'un segment "
						            + "n'a pas été reconnue.");
						    throw new IntersectionIntrouvableException("Intersection introuvable.");
						}
						
						// On gère le voisinage des intersections
						
						segments.add(new Segment(this.intersections.get(origineId), 
						        this.intersections.get(destinationId), longueur, nom));
						
						if(!this.intersectionsVoisines.containsKey(origineId)) {
                            this.intersectionsVoisines.put(origineId, new ArrayList<Segment>());
                        }
						
                        this.intersectionsVoisines.get(origineId).add(
                                segments.get(segments.size() - 1));
					}
				}
			}
			
			this.entrepot = this.intersections.get(entrepotId);
		} catch (Exception e) {
		    
		    // Réinitialisation des attributs du plan courant
		    
			this.intersections =  new HashMap<Long, Intersection>();
			this.segments =  new ArrayList<Segment>();
			this.entrepot = null;

			// Gestion de l'erreur 
			
			LOGGER.error("Erreur lors du parsing du fichier " + fichier);
			throw new FichierNonConformeException("Fichier non conforme");
		}
	}

	/**
	 * Permet d'obtenir l'intersection la plus proche des sommets gris
	 * @param intersectionsGrise La liste des intersections grises
	 * @param distance La map des distances
	 * @return L'ID de l'intersection la plus proche
	 */
	private Long obtenirIntersectionLaPlusProche(Set<Long> intersectionsGrise,
	        HashMap<Long, Float> distance) {
	    
        Long interPlusProche = null;
        float plusPetiteDist = Float.MAX_VALUE;
        
        for (Long idInter : intersectionsGrise) {
            float interDist = distance.get(idInter);
            
            if (interDist < plusPetiteDist) {
                plusPetiteDist = interDist;
                interPlusProche = idInter;
            }
        }
        
        return interPlusProche;
    }

	/**
	 * Permet de vérifier si deux intersections sont connectées
	 * @param depart L'intersection de départ
	 * @param arrivee L'intersection d'arrivée
	 * @return true si les intersections sont connectées, false sinon
	 */
	private boolean sontConnectee(Intersection depart, Intersection arrivee) {
		HashMap<Long, Float>    distance = new HashMap<Long, Float>();
		HashMap<Long, Float>    distanceAndHeuristic = new HashMap<Long, Float>();
		Set<Long>               intersectionsNoires = new HashSet<Long>();
		Queue<Long> intersectionsGrises = new PriorityQueue<Long>(20, new Comparator<Long>() {
			public int compare(Long n1, Long n2) {
				if(distanceAndHeuristic.get(n1)==distanceAndHeuristic.get(n2)){
					return 0;
				}
				else if(distanceAndHeuristic.get(n1)<distanceAndHeuristic.get(n2)){
					return -1;
				}
				return 1;
			}
		});

		for(Entry<Long, Intersection> entry : intersections.entrySet()) {
			distance.put(entry.getKey(), -1.0f);
			distanceAndHeuristic.put(entry.getKey(), -1.0f);
		}

		distance.put(depart.getIdIntersection(), 0.0f);
		distanceAndHeuristic.put(depart.getIdIntersection(), calculHeuristique(depart, arrivee));
		intersectionsGrises.add(depart.getIdIntersection());

		while(!intersectionsGrises.isEmpty()) {
			long idCourrant = intersectionsGrises.poll();
			if(intersectionsVoisines.get(idCourrant) != null) {
				for(Segment seg : intersectionsVoisines.get(idCourrant)) {
					Intersection 	 voisin = seg.getDestination();
					Long 	   	   idVoisin = voisin.getIdIntersection();
					float heuristiqueVoisin = calculHeuristique(voisin, arrivee);

					if(idVoisin == arrivee.getIdIntersection()) {
						return true;
					}

					float g = distance.get(idCourrant)+ seg.getLongueur();
					float f = g + heuristiqueVoisin;

					if(!intersectionsNoires.contains(idVoisin)) {
						if(g<distance.get(idVoisin)
								||distanceAndHeuristic.get(idVoisin) == -1) {
							distance.put(idVoisin,g);
							distanceAndHeuristic.put(idVoisin,f);
							intersectionsGrises.add(idVoisin);
						}
					}
				}
			}

			intersectionsNoires.add(idCourrant);
		}

		return false;
	}

	/**
	 * Permet de calculer l'heuristique pour deux intersections données
	 * @param interCourant L'intersection courante
	 * @param interCible L'intersection cible
	 * @return la valeur de l'heuristique
	 */
	private float calculHeuristique(Intersection interCourant,
									Intersection interCible) {
		double R = 6372.8; // Earth's Radius, in kilometers

		double dLat = Math.toRadians(interCible.getLatitude() - interCourant.getLatitude());
		double dLon = Math.toRadians(interCible.getLongitude() - interCourant.getLongitude());
		double lat1 = Math.toRadians(interCourant.getLatitude());
		double lat2 = Math.toRadians(interCible.getLatitude());

		double a = Math.pow(Math.sin(dLat / 2),2)
				+ Math.pow(Math.sin(dLon / 2),2) * Math.cos(lat1) * Math.cos(lat2);
		double c = 2 * Math.asin(Math.sqrt(a));

		return (float) (R * c);
	}

	public List<String> obtenirRuesIntersection(Intersection intersection) {
		String rue1 = null;
		//float longueurRue1 = 0;
		String rue2 = null;
		//float longueurRue2=0;
		for(Segment seg : segments) {
			if (rue1 == null && (Objects.equals(seg.getOrigine().getIdIntersection(),
					intersection.getIdIntersection()))) {
				rue1 = seg.getNom();
			}

			if (rue2 == null && (Objects.equals(seg.getOrigine().getIdIntersection(),
					intersection.getIdIntersection())) && (!Objects.equals(seg.getNom(), rue1))) {
				rue2 = seg.getNom();
			}
		}
		return Arrays.asList(rue1,rue2);
	}
}

