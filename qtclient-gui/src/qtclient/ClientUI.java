package qtclient;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * La classe ClientUI estende Application e modella il set di parametri per
 * un'applicazione.
 */
public class ClientUI extends Application {

	/** Radio button per learning da tabella. */
	private RadioButton radioLearning;

	/** Radio button per lettura da file. */
	private RadioButton radioReading;

	/** Campo di testo per input del nome della tabella. */
	private TextField textTab;

	/** Campo di testo per input del nome del file. */
	private TextField textFile;

	/** Campo di testo per input del valore del raggio. */
	private TextField textRadius;

	/** Area di testo per output a schermo. */
	private TextArea resultAreaTxt;

	/**
	 * Selezionato learning da database.
	 *
	 * @return true, se andato a buon fine.
	 */
	boolean selectedLearning() {
		return radioLearning.isSelected();
	}

	/**
	 * Selezionato reading da file.
	 *
	 * @return true, se andato a buon fine.
	 */
	boolean selectedReading() {
		return radioReading.isSelected();
	}

	/**
	 * Restituisce il nome della tabella.
	 *
	 * @return nome tabella.
	 */
	TextField getTextTab() {
		return textTab;
	}

	/**
	 * Restituisce il nome del file.
	 *
	 * @return nome file.
	 */
	TextField getTextFile() {
		return textFile;
	}

	/**
	 * Restituisce il raggio.
	 *
	 * @return raggio.
	 */
	TextField getTextRadius() {
		return textRadius;
	}

	/**
	 * Restituisce l'area di testo per i risultati in output.
	 *
	 * @return res area.
	 */
	TextArea getResArea() {
		return resultAreaTxt;
	}

	/**
	 * Il metodo main.
	 *
	 * @param args gli argomenti.
	 */
	public static void main(String[] args) {

		launch(args);

	}

	/**
	 * Start.
	 *
	 * @param st lo Stage.
	 * @throws Exception eccezione generica.
	 */
	@Override
	public void start(Stage st) throws Exception {
		VBox root = new VBox();
		FlowPane parameterSetting = new FlowPane();
		GridPane QTCMining = new GridPane();
		ToggleGroup radio = new ToggleGroup();
		radioLearning = new RadioButton("Learning from db");
		radioLearning.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event arg0) {
				textTab.setDisable(false);
				textFile.setDisable(false);
				textRadius.setDisable(false);
			}
		});
		radioLearning.setTooltip(new Tooltip("Seleziona l'operazione di learning dei dati sulla tabella del database"));
		radioLearning.setSelected(true);
		radioLearning.setToggleGroup(radio);
		radioReading = new RadioButton("Reading from file");
		radioReading.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event arg0) {
				textTab.setDisable(true);
				textRadius.setDisable(true);
			}
		});
		radioReading.setTooltip(new Tooltip("Seleziona l'operazione di lettura dei dati sulla tabella del database"));
		radioReading.setToggleGroup(radio);
		Label labsource = new Label("Selecting data source");
		labsource.setFont(Font.font(null, FontWeight.BOLD, 12));
		QTCMining.add(labsource, 0, 0);
		QTCMining.add(radioLearning, 0, 1);
		QTCMining.add(radioReading, 0, 2);
		FlowPane aprioriInput = new FlowPane();
		Label labtab = new Label("Nome tabella");
		textTab = new TextField();
		textTab.setPrefColumnCount(8);
		textTab.setTooltip(new Tooltip("Nome della tabella da cui prendere i dati."));
		Label labRadius = new Label("Raggio");
		textRadius = new TextField();
		textRadius.setPrefColumnCount(2);
		textRadius.setTooltip(new Tooltip("Valore del raggio dei centroidi da ricavare"));
		Label labfile = new Label("Nome file");
		textFile = new TextField();
		textFile.setPrefColumnCount(8);

		textFile.setTooltip(new Tooltip("Nome del file su cui salvare/da cui caricare i risultati del data-mining"));
		aprioriInput.setVgap(5);
		aprioriInput.getChildren().add(labfile);
		aprioriInput.getChildren().add(textFile);
		aprioriInput.getChildren().add(labtab);
		aprioriInput.getChildren().add(textTab);
		aprioriInput.getChildren().add(labRadius);
		aprioriInput.getChildren().add(textRadius);
		GridPane dataInput = new GridPane();
		Label labinput = new Label("Input parameters");
		labinput.setFont(Font.font(null, FontWeight.BOLD, 12));
		dataInput.add(labinput, 0, 0);
		dataInput.add(aprioriInput, 0, 1);
		dataInput.setVgap(5);
		FlowPane miningCommand = new FlowPane();
		miningCommand.setAlignment(Pos.CENTER);
		miningCommand.setPadding(new Insets(30, 0, 20, 0));
		miningCommand.setHgap(5);
		Button runQTC = new Button("Esegui data-mining");
		runQTC.setTooltip(new Tooltip("Esegue il clustering in base ai parametri inseriti e mostra i risultati."));
		QTCButtonHandler qtcHandler = new QTCButtonHandler(this);
		runQTC.addEventHandler(MouseEvent.MOUSE_CLICKED, qtcHandler);
		miningCommand.getChildren().add(runQTC);
		BorderPane resViewer = new BorderPane();
		Label labViewer = new Label("Messaggi e Risultati");
		labViewer.setFont(Font.font(null, FontWeight.BOLD, 12));
		resultAreaTxt = new TextArea();
		resultAreaTxt.setEditable(false);
		resultAreaTxt.setPrefSize(695, 250);
		resultAreaTxt.setMaxSize(695, 250);
		resViewer.setTop(labViewer);
		BorderPane.setAlignment(labViewer, Pos.TOP_LEFT);
		resViewer.setCenter(resultAreaTxt);
		resViewer.setPadding(new Insets(5, 10, 10, 10));
		parameterSetting.setPadding(new Insets(20, 40, 30, 40));
		parameterSetting.setHgap(60);
		QTCMining.setVgap(5);
		aprioriInput.setHgap(10);
		parameterSetting.getChildren().add(QTCMining);
		parameterSetting.getChildren().add(dataInput);
		root.getChildren().add(parameterSetting);
		root.getChildren().add(miningCommand);
		root.getChildren().add(resViewer);
		Scene scene = new Scene(root, 700, 400);
		st.setScene(scene);
		st.setResizable(false);
		st.setTitle("Quality Threshold Clustering");
		st.show();
		st.setOnCloseRequest(new EventHandler() {
			@Override
			public void handle(Event arg0) {
				qtcHandler.closeConnection();
			}
		});
	}
}