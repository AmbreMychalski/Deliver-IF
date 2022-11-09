package modele;

import java.io.File;
import java.util.*;

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
public class Journee extends Observable {
    private int nbMaxLivreur;
    private int nbLivreur =0;
    private Plan plan;
    private List<DemandeLivraison> demandesLivraison;

    private List<Livraison> livraisons;
    private List<Livraison> livraisonsNonValides;
    private List<Livraison> livraisonsNouveauLivreur;
    private List<Tournee> tournees;
    //private TemplateTSP template;
    
    public Journee(Plan p) {
        plan = p;
        demandesLivraison = new ArrayList<DemandeLivraison>();
        tournees = new ArrayList<Tournee>();
        livraisonsNonValides = new ArrayList<>();
        livraisons = new ArrayList<Livraison>();
    }
    
    public Journee() {
        demandesLivraison = new ArrayList<DemandeLivraison>();
        this.livraisonsNonValides =  new ArrayList<Livraison>();
        tournees = new ArrayList<Tournee>();
        livraisons =  new ArrayList<Livraison>();
        livraisonsNouveauLivreur =  new ArrayList<Livraison>();
    }

    public List<Livraison> getLivraisonsLivreur(int livreur){
        List<Livraison> livraisonsDuLivreur = new ArrayList<>();
        for(Livraison l : livraisons){
            if(l.getLivreur()==livreur){
                livraisonsDuLivreur.add(l);
            }
        }
        return livraisonsDuLivreur;
    }

    public ArrayList<DemandeLivraison> chargerDemandesLivraison(File fichier) {
        if(this.demandesLivraison == null) {
            this.demandesLivraison = new ArrayList<>();
        }
        Node node = null;
        NodeList list=null; 
        
        ArrayList<DemandeLivraison> demandesAjoutees = new ArrayList<>();
        
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
                        if(this.plan.estLivrable(this.plan.getIntersections().get(intersectionId))) {
                            DemandeLivraison demande = new DemandeLivraison(this.plan.getIntersections().get(intersectionId), new PlageHoraire(heureDebut, heureFin));
                            ajouterDemandeLivraison(demande);
                            demandesAjoutees.add(demande);
                        }
                    }
                }
            }
        } catch(Exception e) {
            System.err.println("Problème lors de la lecture du fichier \n "+ e);
        }
        return(demandesAjoutees);
    }
    
    public void ajouterDemandeLivraison(DemandeLivraison demande) {
        this.demandesLivraison.add(demande);
        notifierObservateurs("ChangementDemandeLivraison");
    }
    
    public void supprimerDemandeLivraison(DemandeLivraison demande) {
        this.demandesLivraison.remove(demande);
        notifierObservateurs("ChangementDemandeLivraison");
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
    
    public boolean calculerTournee() {
        boolean tourneeComplete = true;
        boolean tourneeCalculee = false;
        List<DemandeLivraison> dmdLivrOrdonnee = new LinkedList<>();
                

        List<DemandeLivraison> listDemande= new LinkedList<DemandeLivraison>(demandesLivraison);

        List<Livraison> livrList = new LinkedList<Livraison>();

        CompleteGraph g = new CompleteGraph(listDemande,this.plan, this.plan.getEntrepot());

        TSP tsp = new TSP1();
        tsp.searchSolution(20000, g);
        for(int i=1; i<listDemande.size()+1;i++) {
            int x =tsp.getSolution(i);
            DemandeLivraison demande = g.getIdIndexToDemandeLivraison().get(x);
            this.livraisons.add(new Livraison(demande, 0, 1));
            livrList.add(this.livraisons.get(this.livraisons.size()-1));
        }

        List<Trajet> trajetList = this.creerListTrajet(livrList, g);

        tournees.add(new Tournee(trajetList,livrList));

        this.majHeureLivraison(tournees.get(tournees.size()-1),0);

        System.out.println("tourneeComplete ="+tourneeComplete);

        this.nbLivreur=1;

        return tourneeComplete;

    }
    public void notifierObservateurs(String args){
        setChanged();
        notifyObservers(args);
    }

    public boolean calculerTourneeNouveauLivreur() {
        Map<DemandeLivraison, Livraison> demandeToLivr = new HashMap<DemandeLivraison, Livraison>();
        boolean tourneeComplete = true;
        boolean tourneeCalculee = false;
        List<DemandeLivraison> dmdLivrOrdonnee = new LinkedList<>();

        List<DemandeLivraison> listDemande= new LinkedList<DemandeLivraison>(demandesLivraison);

        List<Livraison> livrList = new LinkedList<Livraison>();

        for(Livraison livr : livraisonsNouveauLivreur){
            listDemande.add(livr.getDemandeLivraison());
            demandeToLivr.put(livr.getDemandeLivraison(), livr);
        }

        CompleteGraph g = new CompleteGraph(listDemande,this.plan, this.plan.getEntrepot());

        TSP tsp = new TSP1();
        tsp.searchSolution(20000, g);
        for(int i=1; i<listDemande.size()+1;i++) {
            int x =tsp.getSolution(i);
            DemandeLivraison demande = g.getIdIndexToDemandeLivraison().get(x);
            livrList.add(demandeToLivr.get(demande));
        }

        List<Trajet> trajetList = this.creerListTrajet(livrList, g);

        tournees.add(new Tournee(trajetList,livrList));

        this.majHeureLivraison(tournees.get(tournees.size()-1),0);


        return true;
    }

    public void ajouterObservateur(Observer obs) {
        addObserver(obs);
    }

    public void modifierDemandeLivraison(DemandeLivraison demande, Intersection intersection,PlageHoraire plageHoraire) {
        demande.modifierDemandeLivraison(intersection, plageHoraire);
        notifierObservateurs("ChangementDemandeLivraison");
    }

    public void supprimerLivraisonTournee(Livraison livr ){

        Tournee t = tournees.get(livr.getLivreur()-1);
        int index;
        index = t.getLivraisons().indexOf(livr);
        t.getLivraisons().remove(livr);

        t.getTrajets().remove(index);
        t.getTrajets().remove(index);

        Intersection intersectionAmont;
        if(index ==0){
            intersectionAmont = plan.getEntrepot();
        }
        else{
            intersectionAmont = t.getLivraisons().get(index-1).getDemandeLivraison().getIntersection();
        }
        Intersection intersectionAval ;
        if(index ==t.getLivraisons().size()){
            intersectionAval = plan.getEntrepot();
        }
        else{
            intersectionAval = t.getLivraisons().get(index).getDemandeLivraison().getIntersection();
        }

        List<Segment> lisSeg = plan.calculerPlusCourtChemin(intersectionAmont, intersectionAval);

        float dist = 0;

        for(Segment seg : lisSeg){
            dist+=seg.getLongueur();
        }

        t.getTrajets().add(index, new Trajet(lisSeg,dist));

        this.majHeureLivraison(t,index);



    }

    public void supprimerLivraisonJournee(Livraison livr){
        this.livraisons.remove(livr);
        if(this.livraisonsNouveauLivreur.contains(livr)){
            this.livraisonsNouveauLivreur.remove(livr);
        }
        else{
            this.supprimerLivraisonTournee(livr);
        }
    }

    public void ajouterDemandeLivraisonTournee(DemandeLivraison dl, Livraison livrAvant){
        /*Tournee t = tournees.get(livrAvant.getLivreur()-1);
        int index = t.getLivraisons().indexOf(livrAvant);
        t.getLivraisons().add(index+1,new Livraison(dl,0,livrAvant.getLivreur()));
        t.getTrajets().remove(index+1);

        this.livraisons.add(t.getLivraisons().get(t.getLivraisons().size()-1));

        if(t.getLivraisons().size()>index+1){

        }

        List<Segment> lisSeg = plan.calculerPlusCourtChemin(t.getLivraisons().get(index).getDemandeLivraison().getIntersection(),
                t.getLivraisons().get(index+1).getDemandeLivraison().getIntersection());

        float dist = 0;

        for(Segment seg : lisSeg){
            dist+=seg.getLongueur();
        }
        t.getTrajets().add(index+1, new Trajet(lisSeg,dist));*/

    }
    public void assignerLivraisonNouveauLivreur(Livraison livr){

        supprimerLivraisonTournee(livr);
        if(this.livraisonsNouveauLivreur.size()==0){
            this.nbLivreur++;
        }
        livr.setLivreur(this.nbLivreur);
        this.livraisonsNouveauLivreur.add(livr);
    }

    private void majHeureLivraison(Tournee t, int startIndex){
        float heureLivraison;
        if(startIndex==0){
            heureLivraison = t.getLivraisons().get(0).getDemandeLivraison().getPlageHoraire().getDebut();
            startIndex ++;
        }
        else{
            heureLivraison = t.getLivraisons().get(startIndex-1).getDate();
        }
        heureLivraison+=5/60.0f;
        for(int i=startIndex; i<t.getLivraisons().size(); i++){

            float dist = t.getTrajets().get(i).getLongueur();
            heureLivraison+= dist/(15000.0f);

            Livraison vielleLivraison = new Livraison(t.getLivraisons().get(i));
            t.getLivraisons().get(i).setDate(heureLivraison);
            if(this.livraisonsNonValides.contains(vielleLivraison)){
                this.livraisonsNonValides.remove(vielleLivraison);
            }
            if(heureLivraison>t.getLivraisons().get(i).getDemandeLivraison().getPlageHoraire().getFin()) {
                this.livraisonsNonValides.add(t.getLivraisons().get(i));
            }
            if(heureLivraison < t.getLivraisons().get(i).getDemandeLivraison().getPlageHoraire().getDebut()) {
                heureLivraison = t.getLivraisons().get(i).getDemandeLivraison().getPlageHoraire().getDebut();
            }
            heureLivraison+=5/60.0f;
        }
    }

    private List<Trajet> creerListTrajet(List<Livraison> livrList, CompleteGraph g ){
        List<Trajet> trajetList = new LinkedList<Trajet>();
        List<Segment> lisSeg= plan.calculerPlusCourtChemin(plan.getEntrepot(),livrList.get(0).getDemandeLivraison().getIntersection());
        int indexCurrentDl = g.getIdDemandeLivraisonToIndex().get(livrList.get(0).getDemandeLivraison());
        int indexPreviousDl = 0;
        float dist =  g.getCost(indexPreviousDl, indexCurrentDl);
        trajetList.add(new Trajet(lisSeg, dist));

        for(int i=0; i<livrList.size()-1;i++) {

            indexCurrentDl = g.getIdDemandeLivraisonToIndex().get(livrList.get(i+1).getDemandeLivraison());
            indexPreviousDl = g.getIdDemandeLivraisonToIndex().get(livrList.get(i).getDemandeLivraison());
            dist =  g.getCost(indexPreviousDl, indexCurrentDl);

            lisSeg= plan.calculerPlusCourtChemin(livrList.get(i).getDemandeLivraison().getIntersection(),livrList.get(i+1).getDemandeLivraison().getIntersection());
            trajetList.add(new Trajet(lisSeg, dist));
        }
        lisSeg= plan.calculerPlusCourtChemin(livrList.get(livrList.size()-1).getDemandeLivraison().getIntersection(),plan.getEntrepot());

        indexCurrentDl = g.getIdDemandeLivraisonToIndex().get(livrList.get(0).getDemandeLivraison());
        indexPreviousDl = g.getIdDemandeLivraisonToIndex().get(livrList.get(livrList.size()-1).getDemandeLivraison());
        dist =  g.getCost(indexPreviousDl, indexCurrentDl);
        trajetList.add(new Trajet(lisSeg, dist));

        return trajetList;
    }


}

