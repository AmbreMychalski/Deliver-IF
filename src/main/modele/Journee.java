package modele;

import java.io.File;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import controleur.ControleurFenetrePrincipale;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    public static final Logger LOGGER = LogManager.getLogger(ControleurFenetrePrincipale.class);
    private Plan plan;
    private List<Livreur> livreurs;

    /**
     * Constructeur avec un seul paramètre
     */
    public Journee() {
        livreurs = new ArrayList<>();
    }

    /**
     * Permet de charger un fichier de demandes de livraison. Traite une
     * exception dans le cas d'un souci de lecture.
     * @param fichier Le fichier que l'on veut charger
     * @param livreur Le livreur auquel on associera les demandes
     * @return La liste des demandes de livraison qui ont pu être lues
     */
    public ArrayList<DemandeLivraison> chargerDemandesLivraison(File fichier, Livreur livreur) {
        Node node;
        NodeList list;

        ArrayList<DemandeLivraison> demandesAjoutees = new ArrayList<>();

        try {
            NamedNodeMap attributs;

            DocumentBuilderFactory      fact = DocumentBuilderFactory.newInstance();
            DocumentBuilder          builder = fact.newDocumentBuilder();
            Document                     doc = builder.parse(fichier);
            Element                       el = doc.getDocumentElement();

            list = el.getChildNodes();

            for (int i = 0; i < list.getLength(); i++) {
                node = list.item(i);

                if(node.getNodeType() == Node.ELEMENT_NODE) {
                    if(node.getNodeName().equals("demandeLivraison")) {
                        Long intersectionId;
                        int  heureDebut;
                        int  heureFin;

                        attributs = node.getAttributes();
                        intersectionId = Long.parseLong(attributs
                                .getNamedItem("intersectionId").getNodeValue());
                        heureDebut = Integer.parseInt(attributs
                                .getNamedItem("heureDebut").getNodeValue());
                        heureFin = Integer.parseInt(attributs
                                .getNamedItem("heureFin").getNodeValue());

                        if(heureFin - heureDebut != 1) {
                            throw new Exception("Plage horaire incompatible");
                        }

                        if(this.plan.estLivrable(this.plan.getIntersections().get(intersectionId))) {
                            DemandeLivraison demande = new DemandeLivraison(
                                    this.plan.getIntersections().get(intersectionId),
                                    new PlageHoraire(heureDebut, heureFin)
                            );

                            if(!livreur.getDemandeLivraisons().contains(demande)) {
                                livreur.ajouterDemandeLivraison(demande);
                                demandesAjoutees.add(demande);
                            }
                        }
                    }
                }
            }
        } catch(Exception e) {
            LOGGER.error("Problème lors de la lecture du fichier \n "+ e);
        }

        notifierObservateurs("AjoutListeDemandeLivraison");
        return demandesAjoutees;
    }

    /**
     * Permet de sauvegarder les demandes de livraison
     * @param fichier Le fichier dans lequel on va sauvegarder les demandes
     * @param livreur Le livreur pour lequel on sauvegarde les demandes
     */
    public void sauvegarderDemandesLivraison(File fichier, Livreur livreur) {
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder        documentBuilder = documentFactory.newDocumentBuilder();
            Document               document = documentBuilder.newDocument();
            Element                root = document.createElement("listeDemandes");

            document.appendChild(root);

            for(DemandeLivraison demande : livreur.getDemandeLivraisons()) {
                Element demandeLivraison = document.createElement("demandeLivraison");

                root.appendChild(demandeLivraison);
                demandeLivraison.setAttribute("intersectionId",
                        demande.getIntersection().getIdIntersection().toString());
                demandeLivraison.setAttribute("heureDebut",
                        Integer.toString(demande.getPlageHoraire().getDebut()));
                demandeLivraison.setAttribute("heureFin",
                        Integer.toString(demande.getPlageHoraire().getFin()));
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer        transformer = transformerFactory.newTransformer();
            DOMSource          domSource = new DOMSource(document);
            StreamResult       streamResult = new StreamResult(fichier);

            transformer.transform(domSource, streamResult);
        } catch (Exception e) {
            LOGGER.error("Erreur lors de la sauvegarde des demandes de livraison : \n)" + e);
        }
    }

    /**
     * Calcul d'une tournée pour un livreur.
     * @param livreur Le livreur pour lequel on veut sauvegarder la tournée
     * @return true si la tournée est complète (et valide), false sinon
     */
    public boolean calculerTournee(Livreur livreur) {
        List<DemandeLivraison> listDemande = new LinkedList<>(livreur.getDemandeLivraisons());
        List<Livraison>        livrList = new LinkedList<>();
        GrapheComplet          g = new GrapheComplet(listDemande,this.plan, this.plan.getEntrepot());
        boolean                tourneeCalcule = true;
        TSP                    tsp = new TSP1();
        boolean                solutionTrouvee = tsp.searchSolution(20000, g);

        if(solutionTrouvee) {
            for (int i = 1; i < listDemande.size() + 1; i++) {
                int x = tsp.getSolution(i);
                DemandeLivraison demande = g.getIdIndexToDemandeLivraison().get(x);

                livreur.getLivraisons().add(new Livraison(demande, 0, livreur, false));
                livrList.add(livreur.getLivraisons().get(livreur.getLivraisons().size() - 1));
            }

            List<Trajet> trajetList = this.creerListTrajet(livrList, g);
            Tournee t = new Tournee(trajetList, livrList);

            livreur.modifierTournee(t);
            this.majHeureLivraison(t, 0);
        } else {
            tourneeCalcule = false;
        }

        LOGGER.info("solutionTrouvee = " + solutionTrouvee);

        return tourneeCalcule;
    }

    /**
     * Notifie les observateurs de l'objet passé en paramètre
     * @param arg L'objet dont on veut notifier les observateurs
     */
    public void notifierObservateurs(Object arg) {
        setChanged();
        notifyObservers(arg);
    }

    /**
     * Permet d'ajouter un livreur à la journée considérée.
     * @param livreur Le livreur que l'on veut ajouter à la liste
     */
    public void ajouterLivreur(Livreur livreur) {
        this.livreurs.add(livreur);
        notifierObservateurs("AjoutLivreur");
    }

    /**
     * Ajoute l'objservateur.
     * @param obs L'observateur que l'on veut ajouter
     */
    public void ajouterObservateur(Observer obs) {
        addObserver(obs);
    }

    /**
     * Modifie la demande de livraison considérée.
     * @param demande La demande de livraison à modifier
     * @param intersection La nouvelle intersection à définir (peut être null)
     * @param plageHoraire La nouvelle plage horaire à définir (peut être null)
     */
    public void modifierDemandeLivraison(Livreur livreur, DemandeLivraison demande,
                                         Intersection intersection,
                                         PlageHoraire plageHoraire) {
        livreur.modifierDemandeLivraison(demande, intersection, plageHoraire);
    }

    /**
     * Permet d'ajouter une demande de livraison à la tournée considérée
     * @param dl La demande de livraison que l'on veut ajouter
     * @param livrAvant La dernière livraison
     * @param livreur Le livreur auquel on veut associer la demande
     * @return La livraison créée et ajoutée
     */
    public Livraison ajouterDemandeLivraisonTournee(DemandeLivraison dl,
                                                    Livraison livrAvant,
                                                    Livreur livreur) {

        Livraison livraison = new Livraison(dl,0, livreur,
                false);

        ajouterLivraisonTournee(livraison, livrAvant, livreur);
        return livraison;
    }
    /**
     * Permet de changer le livreur d'une livraison
     * @param livr La livraison que l'on veut modifier
     * @param livreur Le livreur a qui on va ajouter la livraison
     * @param ancienLivreur Le livreur qui va perdre la livraison
     */
    public void assignerLivraisonNouveauLivreur(Livraison livr, Livreur livreur,
                                                Livreur ancienLivreur) {
        supprimerLivraisonTournee(ancienLivreur, livr);
        livr.setLivreur(livreur);
        livreur.ajouterDemandeLivraison(livr.getDemandeLivraison());
    }

    /**
     * Permet d'ajouter la livraison à la tournée pour le livreur donné
     * @param livr La livraison que l'on veut ajouter
     * @param livrAvant La dernière livraison (avant celle que l'on veut ajouter)
     * @param livreur Le livreur auquel on veut ajouter la livraison
     */
    public void ajouterLivraisonTournee(Livraison livr, Livraison livrAvant,
                                        Livreur livreur) {

        livr.setLivreur(livreur);
        Tournee t = livreur.getTournee();
        Intersection intersectionAmont;
        int index;

        if(livrAvant != null) {
            index = t.getLivraisons().indexOf(livrAvant);
            intersectionAmont =
                    t.getLivraisons().get(index).getDemandeLivraison().getIntersection();
        }else{
            intersectionAmont = plan.getEntrepot();
            index = -1;
        }
        livreur.ajouterLivraisonTournee(index + 1, livr);
        t.getTrajets().remove(index + 1);
        Intersection intersectionAval =
                t.getLivraisons().get(index+1).getDemandeLivraison().getIntersection();
        List<Segment> lisSeg =
                plan.calculerPlusCourtChemin(intersectionAmont, intersectionAval);
        float dist = 0;

        for(Segment seg : lisSeg) {
            dist += seg.getLongueur();
        }

        t.getTrajets().add(index + 1, new Trajet(lisSeg, dist,intersectionAmont, intersectionAval));

        intersectionAmont =
                t.getLivraisons().get(index + 1).getDemandeLivraison().getIntersection();

        if((index + 2) == t.getLivraisons().size()) {
            intersectionAval = this.plan.getEntrepot();
        } else {
            intersectionAval =
                    t.getLivraisons().get(index + 2).getDemandeLivraison().getIntersection();
        }

        lisSeg = plan.calculerPlusCourtChemin(intersectionAmont, intersectionAval);
        dist = 0;

        for(Segment seg : lisSeg) {
            dist += seg.getLongueur();
        }

        t.getTrajets().add(index + 2, new Trajet(lisSeg, dist,intersectionAmont, intersectionAval));
        if(index == -1){
            index = 0;
        }
        this.majHeureLivraison(t, index);
    }

    /**
     * Permet de supprimer une livraison d'une tournée
     * @param livreur Le livreur qui possède la tournée
     * @param livr La livraison qui sera supprimmée de la tournée
     */
    public void supprimerLivraisonTournee(Livreur livreur, Livraison livr) {
        Tournee t = livreur.getTournee();
        if(t.getLivraisons().size() != 1) {
            Intersection intersectionAmont;
            Intersection intersectionAval;
            int          index;

            index = t.getLivraisons().indexOf(livr);

            livreur.supprimerLivraison(livr);
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
            float         dist = 0;

            for (Segment seg : lisSeg) {
                dist += seg.getLongueur();
            }

            t.getTrajets().add(index, new Trajet(lisSeg, dist, intersectionAmont,
                    intersectionAval));
            this.majHeureLivraison(t, index);
        } else {
            livr.getLivreur().supprimerTournee();
        }
    }
    private List<Trajet> creerListTrajet(List<Livraison> livrList, GrapheComplet g) {
        List<Trajet>  trajetList = new LinkedList<>();
        List<Segment> lisSeg = plan.calculerPlusCourtChemin(plan.getEntrepot(),
                livrList.get(0).getDemandeLivraison().getIntersection());
        int           indexCurrentDl = g.getIdDemandeLivraisonToIndex().get(livrList.get(0).getDemandeLivraison());
        int           indexPreviousDl = 0;
        float         dist = g.getCout(indexPreviousDl, indexCurrentDl);

        trajetList.add(new Trajet(lisSeg, dist,plan.getEntrepot(),
                livrList.get(0).getDemandeLivraison().getIntersection()));

        for(int i = 0; i < livrList.size() - 1; i++) {
            indexCurrentDl = g.getIdDemandeLivraisonToIndex()
                    .get(livrList.get(i+1).getDemandeLivraison());
            indexPreviousDl = g.getIdDemandeLivraisonToIndex()
                    .get(livrList.get(i).getDemandeLivraison());
            dist = g.getCout(indexPreviousDl, indexCurrentDl);
            lisSeg = plan.calculerPlusCourtChemin(
                    livrList.get(i).getDemandeLivraison().getIntersection(),
                    livrList.get(i + 1).getDemandeLivraison().getIntersection());
            trajetList.add(new Trajet(lisSeg, dist,
                    livrList.get(i).getDemandeLivraison().getIntersection(),
                    livrList.get(i + 1).getDemandeLivraison().getIntersection()));
        }

        lisSeg = plan.calculerPlusCourtChemin(
                livrList.get(livrList.size() - 1).getDemandeLivraison().getIntersection(),plan.getEntrepot());
        indexCurrentDl = 0;
        indexPreviousDl = g.getIdDemandeLivraisonToIndex().get(livrList.get(livrList.size()-1).getDemandeLivraison());
        dist = g.getCout(indexPreviousDl, indexCurrentDl);

        trajetList.add(new Trajet(lisSeg, dist,
                livrList.get(livrList.size() - 1).getDemandeLivraison().getIntersection(),plan.getEntrepot()));

        return trajetList;
    }
    /**
     *
     * @param t
     * @param startIndex
     */
    private void majHeureLivraison(Tournee t, int startIndex) {
        float heureLivraison;

        if(startIndex == 0) {
            heureLivraison = t.getLivraisons().get(0).getDemandeLivraison()
                              .getPlageHoraire().getDebut();

            t.getLivraisons().get(0).setHeure(heureLivraison);
            t.getLivraisons().get(0).setDansSaPlageHoraire(true);
            startIndex++;
        } else {
            heureLivraison = t.getLivraisons().get(startIndex - 1).getHeure();
        }

        heureLivraison += 5 / 60.0f;

        for(int i = startIndex; i < t.getLivraisons().size(); i++) {
            float dist = t.getTrajets().get(i).getLongueur();
            heureLivraison += dist / (15000.0f);

            if(heureLivraison > t.getLivraisons().get(i).getDemandeLivraison()
                                 .getPlageHoraire().getFin()) {
                t.getLivraisons().get(i).setDansSaPlageHoraire(false);
            } else {
                t.getLivraisons().get(i).setDansSaPlageHoraire(true);
            }

            if(heureLivraison < t.getLivraisons().get(i).getDemandeLivraison()
                                 .getPlageHoraire().getDebut()) {
                heureLivraison = t.getLivraisons().get(i).getDemandeLivraison()
                                  .getPlageHoraire().getDebut();
            }

            t.getLivraisons().get(i).setHeure(heureLivraison);

            heureLivraison += 5 / 60.0f;
        }
    }
}
