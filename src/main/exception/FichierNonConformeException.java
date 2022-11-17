package exception;

/**
 * Exception lancée quand le fichier a pu être chargé, mais qu'il y a une
 * erreur dans la lecture.
 */
public class FichierNonConformeException extends Exception {
    public FichierNonConformeException(String errorMessage) {
        super(errorMessage);
    }
}