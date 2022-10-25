package main.modele;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Journee {
	private int nbMaxLivreur;
	private int nbLivreur;
	private Plan plan;
	private List<DemandeLivraison> demandesLivraison;
	private List<Tournee> tournees;
	//private TemplateTSP template;
	
	public Journee(Plan p) {
	    plan = p;
	    demandesLivraison = new ArrayList<DemandeLivraison>();
        tournees = new ArrayList<Tournee>();
	}
	
	public Journee() {
        demandesLivraison = new ArrayList<DemandeLivraison>();
        tournees = new ArrayList<Tournee>();
    }
	
	public void chargerDemandesLivraison(File fichier) {
		if(this.demandesLivraison == null) {
			this.demandesLivraison = new ArrayList<>();
		}
		Node node = null;
		NodeList list=null;	
		
		try {
			DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = fact.newDocumentBuilder();
			Document doc = builder.parse(fichier);
			Element el = doc.getDocumentElement();
			list = el.getChildNodes();
			NamedNodeMap attributs;
			for (int i = 0; i < list.getLength(); i++) {
				node = list.item(i);
				if(node.getNodeType() == Node.ELEMENT_NODE) {
					if(node.getNodeName() == "demandeLivraison") {
						Long intersectionId; 
						int heureDebut;
						int heureFin;
						attributs = node.getAttributes();
						intersectionId = Long.parseLong(attributs.getNamedItem("intersectionId").getNodeValue());
						heureDebut = Integer.parseInt(attributs.getNamedItem("heureDebut").getNodeValue());
						heureFin = Integer.parseInt(attributs.getNamedItem("heureFin").getNodeValue());
						if(heureFin-heureDebut != 1) throw new Exception("Plage horaire incompatible");
						this.demandesLivraison.add(new DemandeLivraison(this.plan.getIntersections().get(intersectionId),new PlageHoraire(heureDebut, heureFin)));
						
					}
				}
			}
		} catch(Exception e) {
			System.err.println("Problème lors de la lecture du fichier \n "+ e);
		}
	}
	
	public void ajouterDemandeLivraison(DemandeLivraison demande) {
	    if (! this.demandesLivraison.contains(demande)) {
	        this.demandesLivraison.add(demande);
	    }
	}
	
	public void sauvegarderDemandesLivraison(File fichier) {
		
		 try {
			 
	            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
	            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
	            Document document = documentBuilder.newDocument();

	            Element root = document.createElement("listeDemandes");
	            document.appendChild(root);
	            for(DemandeLivraison demande : this.demandesLivraison) {
	            	Element demandeLivraison = document.createElement("demandeLivraison");
	            	root.appendChild(demandeLivraison);
	            	demandeLivraison.setAttribute("intersectionId", demande.getIntersection().getIdIntersection().toString());
	            	demandeLivraison.setAttribute("heureDebut", Integer.toString(demande.getPlageHoraire().getDebut()));
	            	demandeLivraison.setAttribute("heureFin", Integer.toString(demande.getPlageHoraire().getFin()));
	            }
	            

	            TransformerFactory transformerFactory = TransformerFactory.newInstance();
	            Transformer transformer = transformerFactory.newTransformer();
	            DOMSource domSource = new DOMSource(document);
	            StreamResult streamResult = new StreamResult(fichier);

	            transformer.transform(domSource, streamResult);
	 
	        } catch (Exception e) {
	        	System.err.println("Erreur lors de la sauvegarde des demandes de livraison : \n)"+e);
	        }
	        
	    }
}
