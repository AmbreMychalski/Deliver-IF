package exception;

/**
 * Exception spécifique lancée quand on lit un segment liant au moins une
 * intersection inconnue
 */
public class IntersectionIntrouvableException extends Exception {
    public IntersectionIntrouvableException(String errorMessage) {
        super(errorMessage);
    }
}
