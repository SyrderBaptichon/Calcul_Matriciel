package AlgLin;

/**
 * Exception lancée lorsqu'un système linéaire est trouvé irrégulier lors de sa résolution.
 */
public class IrregularSysLinException extends Exception {
	/**
     * Construit une nouvelle IrregularSysLinException vide.
     */
	public IrregularSysLinException() {
		super("Le système linéaire est irrégulier");
	}
	
	/**
     * Construit une nouvelle IrregularSysLinException avec un message personnalisé.
     * @param message Message personnalisé décrivant l'irrégularité.
     */
	public IrregularSysLinException(String message) {
		super(message);
	}
	
	/**
     * Retourne une représentation sous forme de chaîne de l'exception.
     * @return Le message de l'irrégularité du système.
     */
	@Override
    public String toString() {
        return "IrregularSysLinException: " + super.getMessage();
    }
}