package qtclient;

/**
 * La classe eccezione ServerException è sollevata dal sistema server e
 * trasmessa al client dallo stream di connessione.
 */
public class ServerException extends Exception {

	/** Identificatore di versione universale per Serializable. */
	private static final long serialVersionUID = 1L;
	
	/** Messagio che descrive l'eccezione. */
	private String message;

	/**
	 * Costruttore della classe ServerException che crea una nuova eccezione del server.
	 *
	 * @param s stringa con messaggio di errore.
	 */
	
	public ServerException(String s) {
		this.message=s;
	}
	
	/**
	 * Il metodo toString() stampa il messaggio contenuto nell'eccezione .
	 *
	 * @return messaggio dell'eccezione
	 */
	public String toString() {
		return this.message;
	}
}
