package modele;

import java.io.File;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Journee extends Observable {
    private int nbMaxLivreur;
    private int nbLivreur = 0;
    private Plan plan;
    private List<DemandeLivraison> demandesLivraison;
    private List<Livraison> livraisons;
    private List<Livreur> livreurs;

    public Journee(Plan p) {
        plan = p;
        demandesLivraison = new ArrayList<DemandeLivraison>();
        livreurs = new ArrayList<>();
        livraisons = new ArrayList<Livraison>();
    }

    public Journee() {
        demandesLivraison = new ArrayList<DemandeLivraison>();
        livreurs = new ArrayList<>();
        livraisons =  new ArrayList<Livraison>();
    }

    public ArrayList<DemandeLivraison> chargerDemandesLivraison(File fichier) {
        if(this.demandesLivraison == null) {
            this.demandesLivraison = new ArrayList<>();
        }

        Node node = null;
        NodeList list = null;

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

                        if(heureFin - heureDebut != 1) {
                            throw new Exception("Plage horaire incompatible");
                        }

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
        notifierObservateurs(null);
        return demandesAjoutees;
    }

    public void ajouterDemandeLivraison(DemandeLivraison demande) {
        this.demandesLivraison.add(demande);
        notifierObservateurs(null);
    }

    public void supprimerDemandeLivraison(DemandeLivraison demande) {
        this.demandesLivraison.remove(demande);
        notifierObservateurs(null);
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

    public boolean calculerTournee(Livreur livreur) {
        boolean tourneeComplete = true;
        boolean tourneeCalculee = false;
        List<DemandeLivraison> dmdLivrOrdonnee = new LinkedList<>();
        List<DemandeLivraison> listDemande = new LinkedList<>(demandesLivraison);
        List<Livraison> livrList = new LinkedList<>();
        GrapheComplet g = new GrapheComplet(listDemande,this.plan, this.plan.getEntrepot());
        TSP tsp = new TSP1();

        tsp.searchSolution(20000, g);
        for(int i = 1; i < listDemande.size() + 1; i++) {
            int x = tsp.getSolution(i);
            DemandeLivraison demande = g.getIdIndexToDemandeLivraison().get(x);

            this.livraisons.add(new Livraison(demande, 0, livreur, false));
            livrList.add(this.livraisons.get(this.livraisons.size()-1));
        }

        List<Trajet> trajetList = this.creerListTrajet(livrList, g);
        Tournee t = new Tournee(trajetList, livrList);
        livreur.setTournee(t);
        this.majHeureLivraison(t,0);
        System.out.println("tourneeComplete = " + tourneeComplete);
        return tourneeComplete;
    }
    public void notifierObservateurs(Object arg){
        setChanged();
        notifyObservers(arg);
    }
    public boolean calculerTourneeNouveauLivreur(Livreur livreur) {
        boolean tourneeComplete = true;
        boolean tourneeCalculee = false;
        List<DemandeLivraison> dmdLivrOrdonnee = new LinkedList<>();
        List<DemandeLivraison> listDemande = new LinkedList<>(livreur.getDemandeLivraisons());
        List<Livraison> livrList = new LinkedList<>();
        GrapheComplet g = new GrapheComplet(listDemande,this.plan, this.plan.getEntrepot());
        TSP tsp = new TSP1();

        tsp.searchSolution(20000, g);
        for(int i = 1; i < listDemande.size() + 1; i++) {
            int x = tsp.getSolution(i);
            DemandeLivraison demande = g.getIdIndexToDemandeLivraison().get(x);

            this.livraisons.add(new Livraison(demande, 0, livreur, false));
            livrList.add(this.livraisons.get(this.livraisons.size()-1));
        }

        List<Trajet> trajetList = this.creerListTrajet(livrList, g);
        Tournee t = new Tournee(trajetList, livrList);
        livreur.setTournee(t);
        this.majHeureLivraison(t,0);
        System.out.println("tourneeComplete = " + tourneeComplete);
        return tourneeComplete;
    }
    /*
    public boolean calculerTourneeNouveauLivreur(Livreur livreur) {
        Map<DemandeLivraison, Livraison> demandeToLivr = new HashMap<>();
        boolean tourneeComplete = true;
        boolean tourneeCalculee = false;
        List<DemandeLivraison> dmdLivrOrdonnee = new LinkedList<>();
        //List<DemandeLivraison> listDemande = new LinkedList<>(demandesLivraison);
        List<DemandeLivraison> listDemande = new LinkedList<>();
        List<Livraison> livrList = new LinkedList<>();


        for(Livraison livr : livreur.getLivraisonsHorsTournee()) {
            listDemande.add(livr.getDemandeLivraison());
            demandeToLivr.put(livr.getDemandeLivraison(), livr);
        }

        GrapheComplet g = new GrapheComplet(listDemande, this.plan,
                this.plan.getEntrepot());
        TSP tsp = new TSP1();

        tsp.searchSolution(20000, g);

        for(int i = 1; i < listDemande.size() + 1; i++) {
            int x = tsp.getSolution(i);
            DemandeLivraison demande = g.getIdIndexToDemandeLivraison().get(x);

            livrList.add(demandeToLivr.get(demande));
        }

        List<Trajet> trajetList = this.creerListTrajet(livrList, g);

        Tournee t = new Tournee(trajetList, livrList);
        livreur.setTournee(t);
        this.majHeureLivraison(t,0);
        livreur.getLivraisonsHorsTournee().clear();

        return true;
    }
     */

    public void ajouterLivreur(Livreur livreur) {
        this.livreurs.add(livreur);
        this.nbLivreur++;
        notifierObservateurs("AjoutLivreur");
    }

    public void ajouterObservateur(Observer obs) {
        addObserver(obs);
    }

    public void modifierDemandeLivraison(DemandeLivraison demande,
                                         Intersection intersection,
                                         PlageHoraire plageHoraire) {
        demande.modifierDemandeLivraison(intersection, plageHoraire);
        notifierObservateurs(null);
    }

    public void supprimerLivraisonJournee(Livreur livreur, Livraison livr) {
        this.livraisons.remove(livr);
        this.supprimerLivraisonTournee(livreur, livr);

    }
    public Livraison ajouterDemandeLivraisonTournee(DemandeLivraison dl,
                                                    Livraison livrAvant, Livreur livreur) {

        Livraison livraison = new Livraison(dl,0,livrAvant.getLivreur(),
                            false);

        this.livraisons.add(livraison);
        ajouterLivraisonTournee(livraison, livrAvant, livreur);
        return livraison;
    }
    public void assignerLivraisonNouveauLivreur(Livraison livr, Livreur livreur, Livreur ancienLivreur) {
        supprimerLivraisonTournee(ancienLivreur, livr);
        livr.setLivreur(livreur);
        livreur.ajouterDemandeLivraison(livr.getDemandeLivraison());
    }

    public void ajouterLivraisonTournee(Livraison livr,Livraison livrAvant, Livreur livreur) {
        livr.setLivreur(livreur);

        Tournee t = livreur.getTournee();
        int index = t.getLivraisons().indexOf(livrAvant);

        t.getLivraisons().add(index+1,livr);
        t.getTrajets().remove(index+1);

        Intersection intersectionAmont =
                t.getLivraisons().get(index).getDemandeLivraison().getIntersection();
        Intersection intersectionAval =
                t.getLivraisons().get(index+1).getDemandeLivraison().getIntersection();
        List<Segment> lisSeg =
                plan.calculerPlusCourtChemin(intersectionAmont, intersectionAval);
        float dist = 0;

        for(Segment seg : lisSeg) {
            dist += seg.getLongueur();
        }

        t.getTrajets().add(index + 1, new Trajet(lisSeg, dist));

        intersectionAmont =
                t.getLivraisons().get(index + 1).getDemandeLivraison().getIntersection();

        if((index + 2) == t.getLivraisons().size()) {
            intersectionAval = this.plan.getEntrepot();
        } else {
            intersectionAval =
                    t.getLivraisons().get(index+2).getDemandeLivraison().getIntersection();
        }

        lisSeg = plan.calculerPlusCourtChemin(intersectionAmont, intersectionAval);
        dist = 0;

        for(Segment seg : lisSeg) {
            dist += seg.getLongueur();
        }

        t.getTrajets().add(index + 2, new Trajet(lisSeg, dist));
        this.majHeureLivraison(t, index);
    }

    public boolean dernierLivreurEstSansTourneeCalculee(){
        return (this.livreurs.get(this.livreurs.size()-1).getTournee() == null);
    }
    private void majHeureLivraison(Tournee t, int startIndex) {
        float heureLivraison;

        if(startIndex == 0) {
            heureLivraison =
                    t.getLivraisons().get(0).getDemandeLivraison().getPlageHoraire().getDebut();

            t.getLivraisons().get(0).setHeure(heureLivraison);
            t.getLivraisons().get(0).setDansSaPlageHoraire(true);
            startIndex++;
        } else {
            heureLivraison = t.getLivraisons().get(startIndex - 1).getHeure();
        }

        heureLivraison += 5 / 60.0f;

        for(int i = startIndex; i < t.getLivraisons().size(); i++){
            float dist = t.getTrajets().get(i).getLongueur();
            heureLivraison += dist / (15000.0f);
            
            Livraison vielleLivraison = new Livraison(t.getLivraisons().get(i));

            if(heureLivraison>t.getLivraisons().get(i).getDemandeLivraison().getPlageHoraire().getFin()) {
                t.getLivraisons().get(i).setDansSaPlageHoraire(false);
            } else {
                t.getLivraisons().get(i).setDansSaPlageHoraire(true);
            }

            if(heureLivraison < t.getLivraisons().get(i).getDemandeLivraison().getPlageHoraire().getDebut()) {
                heureLivraison = t.getLivraisons().get(i).getDemandeLivraison().getPlageHoraire().getDebut();
            }

            t.getLivraisons().get(i).setHeure(heureLivraison);

            heureLivraison += 5 / 60.0f;
        }
    }

    private List<Trajet> creerListTrajet(List<Livraison> livrList, GrapheComplet g) {
        List<Trajet> trajetList = new LinkedList<Trajet>();
        List<Segment> lisSeg = plan.calculerPlusCourtChemin(
                plan.getEntrepot(),livrList.get(0).getDemandeLivraison().getIntersection());
        int indexCurrentDl =
                g.getIdDemandeLivraisonToIndex().get(livrList.get(0).getDemandeLivraison());
        int indexPreviousDl = 0;
        float dist = g.getCout(indexPreviousDl, indexCurrentDl);

        trajetList.add(new Trajet(lisSeg, dist));

        for(int i = 0; i < livrList.size() - 1; i++) {
            indexCurrentDl = g.getIdDemandeLivraisonToIndex().get(livrList.get(i+1).getDemandeLivraison());
            indexPreviousDl = g.getIdDemandeLivraisonToIndex().get(livrList.get(i).getDemandeLivraison());
            dist = g.getCout(indexPreviousDl, indexCurrentDl);
            lisSeg = plan.calculerPlusCourtChemin(livrList.get(i).getDemandeLivraison().getIntersection(),livrList.get(i+1).getDemandeLivraison().getIntersection());
            dist = g.getCout(indexPreviousDl, indexCurrentDl);
            lisSeg = plan.calculerPlusCourtChemin(
                    livrList.get(i).getDemandeLivraison().getIntersection(),
                    livrList.get(i + 1).getDemandeLivraison().getIntersection());
            trajetList.add(new Trajet(lisSeg, dist));
        }

        lisSeg = plan.calculerPlusCourtChemin(
                livrList.get(livrList.size() - 1).getDemandeLivraison().getIntersection(),plan.getEntrepot());

        indexCurrentDl = 0;
        indexPreviousDl = g.getIdDemandeLivraisonToIndex().get(livrList.get(livrList.size()-1).getDemandeLivraison());
        dist = g.getCout(indexPreviousDl, indexCurrentDl);
        trajetList.add(new Trajet(lisSeg, dist));

        return trajetList;
    }

    private void supprimerLivraisonTournee(Livreur livreur, Livraison livr) {
        Tournee t = livreur.getTournee();
        if(t.getLivraisons().size() != 1){
            int index;
            Intersection intersectionAmont;
            Intersection intersectionAval;
            index = t.getLivraisons().indexOf(livr);

            t.getLivraisons().remove(livr);
            t.getTrajets().remove(index);
            t.getTrajets().remove(index);
                if (index == 0) {
                    intersectionAmont = plan.getEntrepot();
                } else {
                    intersectionAmont = t.getLivraisons().get(index - 1).getDemandeLivraison().getIntersection();
                }

                if (index == t.getLivraisons().size()) {
                    intersectionAval = plan.getEntrepot();
                } else {
                    intersectionAval = t.getLivraisons().get(index).getDemandeLivraison().getIntersection();
                }

                List<Segment> lisSeg = plan.calculerPlusCourtChemin(intersectionAmont, intersectionAval);
                float dist = 0;

                for (Segment seg : lisSeg) {
                    dist += seg.getLongueur();
                }

                t.getTrajets().add(index, new Trajet(lisSeg, dist));
                this.majHeureLivraison(t, index);
                notifierObservateurs("SuppressionLivraison");
        }else{
            livr.getLivreur().supprimerTournee();
        }
    }
}

