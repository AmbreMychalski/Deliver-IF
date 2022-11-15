package controleur;

public class ReverseCommande implements Commande{
    private Commande cmd;

    public ReverseCommande(Commande cmd){this.cmd = cmd;}
    public void doCommande(){cmd.undoCommande();}
    public void undoCommande(){cmd.doCommande();}
}
