package exception;

public class FichierNonConformeException extends Exception {
    public FichierNonConformeException(String errorMessage) {
        super(errorMessage);
    }
}