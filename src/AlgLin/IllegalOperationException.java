package AlgLin;

public class IllegalOperationException extends Exception {

    // Constructeur sans message
    public IllegalOperationException() {
        super();  // Appelle le constructeur de la classe Exception
    }

    // Constructeur avec un message d'erreur personnalisé
    public IllegalOperationException(String message) {
        super(message);  // Appelle le constructeur de la classe Exception avec un message
    }

    // Constructeur avec un message d'erreur personnalisé et une cause
    public IllegalOperationException(String message, Throwable cause) {
        super(message, cause);  // Appelle le constructeur de la classe Exception avec un message et une cause
    }

    // Constructeur avec une cause
    public IllegalOperationException(Throwable cause) {
        super(cause);  // Appelle le constructeur de la classe Exception avec une cause
    }
}
