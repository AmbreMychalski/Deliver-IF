package controleur;

import java.util.ArrayList;
import java.util.List;

public class ListOfCommands{
    private int i;
    private List<Command> listeCommand;

    public void ListOfCommand(){
        i=-1;
        listeCommand = new ArrayList<>();
    }

    public void ajouterCommand (Command c){
        i++;
        listeCommand.add(i,c);
        c.doCommand();
    }
    public void undoCommand(){
        if (i >= 0) {
            listeCommand.get(i).undoCommand();;
            i--;
        }
    }
    public  void redoCommand(){
        i++;
        listeCommand.get(i).doCommand();
    }
}
