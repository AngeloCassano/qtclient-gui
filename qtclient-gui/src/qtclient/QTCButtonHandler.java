package qtclient;

import javafx.event.Event;

import javafx.event.EventHandler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * La classe QTCButtonHandler implementa EventHandler e contiene tutti i comandi
 * relativi ad eventi specifici.
 */

public class QTCButtonHandler implements EventHandler {

	/** Interfaccia utente. */
	private ClientUI ui;

	/** Stream di output */
	private ObjectOutputStream out;

	/** Stream di input */
	private ObjectInputStream in; //

	/** La socket. */
	private Socket socket = null;

	/** Costante PORT: porta default di connessione per la socket. */
	static final int PORT = 8080;

	/** Indirizzo IP di default per la socket. */
	static final String IP = "localhost";

	/** Area di testo per i risultati in output. */
	private TextArea resArea = null;

	/**
	 * Istanzia un nuovo handler per QTC button.
	 *
	 * @param ui l'interfaccia utente
	 */
	public QTCButtonHandler(ClientUI ui) {
		this.ui = ui;
		InetAddress addr;
		try {
			addr = InetAddress.getByName(IP);
			socket = new Socket(addr, PORT);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * StoreTableFromDb controlla se la tabella è presente nel database.
	 *
	 * @param tabName il nome della tabella
	 * @throws SocketException        eccezione sulla socket.
	 * @throws ServerException        eccezione sul server.
	 * @throws IOException            eccezione sulle operazioni di input/output.
	 * @throws ClassNotFoundException eccezione per la classe non trovata.
	 */
	private void storeTableFromDb(String tabName)
			throws SocketException, ServerException, IOException, ClassNotFoundException {
		out.writeObject(0);
		resArea.appendText("Table name:" + tabName + "\n");
		out.writeObject(tabName);
		String result = (String) in.readObject();
		if (!result.equals("OK"))
			throw new ServerException(result);
	}

	/**
	 * Learning dalla tabella del database.
	 *
	 * @param r the r
	 * @return the string
	 * @throws SocketException        eccezione sulla socket.
	 * @throws ServerException        eccezione sul server.
	 * @throws IOException            eccezione sulle operazioni di input/output.
	 * @throws ClassNotFoundException eccezione per la classe non trovata.
	 */
	private String learningFromDbTable(Double r)
			throws SocketException, ServerException, IOException, ClassNotFoundException {
		out.flush();
		out.writeObject(1);
		out.writeObject(r);
		String result = (String) in.readObject();
		if (result.equals("OK")) {
			resArea.appendText("Number of Clusters:" + in.readObject() + "\n");
			return (String) in.readObject();
		} else
			throw new ServerException(result);

	}

	/**
	 * Salvataggio dei cluster su file.
	 *
	 * @param fileName nome del file.
	 * @throws SocketException        eccezione sulla socket.
	 * @throws ServerException        eccezione sul server.
	 * @throws IOException            eccezione sulle operazioni di input/output.
	 * @throws ClassNotFoundException eccezione per la classe non trovata.
	 */
	private void storeClusterInFile(String fileName)
			throws SocketException, ServerException, IOException, ClassNotFoundException {
		out.flush();
		out.writeObject(2);
		resArea.appendText("Name of the new backup file:" + fileName);
		fileName = fileName + ".dmp";
		out.writeObject(fileName);
		out.flush();
		String result = (String) in.readObject();
		if (!result.equals("OK"))
			throw new ServerException(result);
	}

	/**
	 * Lettura da file.
	 *
	 * @param fileName nome del file.
	 * @return stringa contenente le informazioni memorizzate nel file.
	 * @throws SocketException        eccezione sulla socket.
	 * @throws ServerException        eccezione sul server.
	 * @throws IOException            eccezione sulle operazioni di input/output.
	 * @throws ClassNotFoundException eccezione per la classe non trovata.
	 */
	private String learningFromFile(String fileName)
			throws SocketException, ServerException, IOException, ClassNotFoundException {
		out.writeObject(3);
		resArea.appendText("File Name:" + fileName);
		fileName = fileName + ".dmp";
		out.writeObject(fileName);
		String result = (String) in.readObject();
		if (result.equals("OK"))
			return (String) in.readObject();
		else
			throw new ServerException(result);

	}

	/**
	 * closeConnection chiude la connessione.
	 */
	void closeConnection() {
		try {

			out.writeObject(5);
			int res = 0;
			while (res != 5) {
				res = (int) in.readObject();
			}
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * il metodo handle definisce il comportamento del bottone QTCminer in caso di click.
	 *
	 * @param arg0 argomento utente in base a cui sceglie la stampa da effettuare nella text area.
	 */
	public void handle(Event arg0) {
		resArea = ui.getResArea();
		TextField nomeTab = ui.getTextTab();
		TextField nomeFile = ui.getTextFile();
		TextField radius = ui.getTextRadius();
		String s = nomeFile.getText();
		resArea.clear();
		resArea.appendText("Nome file:" + s + "\n");

		if (ui.selectedReading()) {
			if (nomeFile.getText().equals("")) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Attenzione!");
				alert.setHeaderText("Nome del file non inserito");
				alert.setContentText("Inserire il nome del file in cui si desidera salavare");

				alert.showAndWait();
				return;
			}
			try {
				String kmeans;
				kmeans = this.learningFromFile(s);
				resArea.appendText(kmeans);
			} catch (SocketException e) {
				resArea.appendText(e.toString());
				return;
			} catch (FileNotFoundException e) {
				resArea.appendText(e.toString());
				return;
			} catch (IOException e) {
				resArea.appendText(e.toString());
				return;
			} catch (ClassNotFoundException e) {
				resArea.appendText(e.toString());
				return;
			} catch (ServerException e) {
				resArea.appendText(e.toString());
			}
		} else if (ui.selectedLearning()) {
			if (nomeFile.getText().equals("")) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Attenzione!");
				alert.setHeaderText("Nome del file non inserito");
				alert.setContentText("Inserire il nome del file in cui si desidera salavare");

				alert.showAndWait();
				return;
			}
			if (nomeTab.getText().equals("")) {

				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Attenzione!");
				alert.setHeaderText("Nome della tabella non inserito");
				alert.setContentText("Inserire il nome di una tabella");

				alert.showAndWait();
				return;
			}
			try {
				this.storeTableFromDb(nomeTab.getText());
			} catch (ServerException e) {
				resArea.appendText(e.toString());
				return;
			} catch (SocketException e) {
				resArea.appendText(e.toString());
				return;
			} catch (FileNotFoundException e) {
				resArea.appendText(e.toString());
				return;
			} catch (IOException e) {
				resArea.appendText(e.toString());
				return;
			} catch (ClassNotFoundException e) {
				resArea.appendText(e.toString());
				return;
			}
			try {
				double raggio = Double.parseDouble(radius.getText());
				if (raggio < 0) {
					throw new NumberFormatException();
				}
				String clusterSet = this.learningFromDbTable(raggio);
				resArea.appendText(clusterSet);
				this.storeClusterInFile(s);
			} catch (NumberFormatException e) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Attenzione!");
				alert.setHeaderText("Valore del raggio non valido");
				alert.setContentText("Inserire un valore decimale positivo");
				alert.showAndWait();
				return;
			} catch (SocketException e) {
				resArea.appendText(e.toString());
				return;
			} catch (FileNotFoundException e) {
				resArea.appendText(e.toString());
				return;
			} catch (ClassNotFoundException e) {
				resArea.appendText(e.toString());
				return;
			} catch (IOException e) {
				resArea.appendText(e.toString());
				return;
			} catch (ServerException e) {
				resArea.appendText(e.toString());
				return;
			}
		}

	}

}
