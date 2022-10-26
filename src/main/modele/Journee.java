package main.modele;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
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
    private List<DemandeLivraison> demandesLivraisonNonTraitees;
    private List<Tournee> tournees;
    //private TemplateTSP template;
    
    public Journee(Plan p) {
        plan = p;
        demandesLivraison = new ArrayList<DemandeLivraison>();
        tournees = new ArrayList<Tournee>();
    }
    
    public Journee() {
        demandesLivraison = new ArrayList<DemandeLivraison>();
        this.demandesLivraisonNonTraitees =  new ArrayList<DemandeLivraison>();
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
        this.demandesLivraison.add(demande);
    }
    
    public void supprimerDemandeLivraison(DemandeLivraison demande) {
        this.demandesLivraison.remove(demande);
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
    
    public void calculerTournee() {
        
        boolean tourneeCalculee = false;
        List<DemandeLivraison> dmdLivrOrdonnee = new LinkedList<DemandeLivraison>();
                
        List<DemandeLivraison> listDemande= new LinkedList<DemandeLivraison>(demandesLivraison);
//      for(DemandeLivraison dl : demandesLivraison) {
//          listDemande.add(dl);
//      }
        
        while(!tourneeCalculee) {
            tourneeCalculee = true;
            CompleteGraph g = new CompleteGraph(listDemande,this.plan, this.plan.getEntrepot());
            dmdLivrOrdonnee.clear();
            
            System.out.println(listDemande);
            g.printGraph();
            
            TSP tsp = new TSP1();
            tsp.searchSolution(20000, g);
            for(int i=1; i<listDemande.size()+1;i++) {
                int x =tsp.getSolution(i);
                dmdLivrOrdonnee.add(g.getIdIndexToDemandeLivraison().get(x));
            }
            
            
            float heureLivraison = (float) dmdLivrOrdonnee.get(0).getPlageHoraire().debut+5/60.0f;  
            
            for(int i=1; i<dmdLivrOrdonnee.size();i++) {
                DemandeLivraison currentDl = dmdLivrOrdonnee.get(i);
                int indexCurrentDl = g.getIdDemandeLivraisonToIndex().get(currentDl);
                int indexPreviousDl = g.getIdDemandeLivraisonToIndex().get(dmdLivrOrdonnee.get(i-1));
                
                float dist = g.getCost(indexPreviousDl, indexCurrentDl);
                heureLivraison+= dist/(15000.0f);
                
                if(heureLivraison>currentDl.getPlageHoraire().getFin()) {
                    tourneeCalculee=false;
                    this.demandesLivraisonNonTraitees.add(currentDl);
                    listDemande.remove(currentDl);
                    break;
                }
                heureLivraison+=5/60.0f;
                
            } 
        }
        
        List<Livraison> livrList = new LinkedList<Livraison>();
        for(DemandeLivraison dl: dmdLivrOrdonnee) {
            livrList.add(new Livraison(dl, null));
        }
        
        List<Trajet> trajetList = new LinkedList<Trajet>();
        List<Segment> lisSeg= plan.calculerPlusCourtChemin(plan.getEntrepot(),livrList.get(0).getDemandeLivraison().getIntersection());     
        trajetList.add(new Trajet(lisSeg));
        
        for(int i=0; i<livrList.size()-1;i++) {
            lisSeg= plan.calculerPlusCourtChemin(livrList.get(i).getDemandeLivraison().getIntersection(),livrList.get(i+1).getDemandeLivraison().getIntersection());
            trajetList.add(new Trajet(lisSeg));
            
            if(lisSeg.get(lisSeg.size()-1).getDestination()!=livrList.get(i+1).getDemandeLivraison().getIntersection()) {
                System.out.println("ATTENTION LE TRAJET DE TERMINE PAS SUR LA DESTINATION");
            }
        }
        lisSeg= plan.calculerPlusCourtChemin(livrList.get(livrList.size()-1).getDemandeLivraison().getIntersection(),plan.getEntrepot());
        trajetList.add(new Trajet(lisSeg));
        
        
        tournees.add(new Tournee(trajetList,livrList ));
        
        
        
    }
}

