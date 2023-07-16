/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.nyc;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.nyc.model.City;
import it.polito.tdp.nyc.model.Distanza;
import it.polito.tdp.nyc.model.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnAdiacenti"
    private Button btnAdiacenti; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaLista"
    private Button btnCreaLista; // Value injected by FXMLLoader

    @FXML // fx:id="cmbProvider"
    private ComboBox<String> cmbProvider; // Value injected by FXMLLoader

    @FXML // fx:id="cmbQuartiere"
    private ComboBox<City> cmbQuartiere; // Value injected by FXMLLoader

    @FXML // fx:id="txtMemoria"
    private TextField txtMemoria; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader
    
    @FXML // fx:id="clQuartiere"
    private TableColumn<?, ?> clQuartiere; // Value injected by FXMLLoader
 
    @FXML // fx:id="clDistanza"
    private TableColumn<?, ?> clDistanza; // Value injected by FXMLLoader
    
    @FXML // fx:id="tblQuartieri"
    private TableView<?> tblQuartieri; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	String provider=this.cmbProvider.getValue();
    	if(provider==null) {
    		this.txtResult.appendText("E' necessario selezionare un provider");
    		return;
    	}
    	this.model.creaGrafo(provider);
    	
    	this.txtResult.appendText("Grafo creato con "+ this.model.nVertici()+" vertici e "+this.model.getArchi()+" archi\n");
    	
    	    	this.cmbQuartiere.getItems().clear(); //per pulirla dal grafo precedente
    	    	//facendo questa operazione ogni volta che pulisco il grafo
    	
    	//a questo punto col metodo che fatto prima popolo la tendina con la lista dei soli vertici del grafo che prendo dal model
    	this.cmbQuartiere.getItems().addAll(this.model.getCitta()); 
    	//USA QUESTO METODO PER RIPOPOLARE LA SECONDA TENDINA COI VERTICI DEL GRAFO, E' PIù VELOCE ED EVITA MOLTI ERRORI
 	
    }

    @FXML
    void doQuartieriAdiacenti(ActionEvent event) {
    	//prendo la citta scelta e gli passo il metodo che calcola i vicini
    	City scelta=this.cmbQuartiere.getValue();
    	
    	if(scelta==null) {
    		this.txtResult.appendText("Selezionare una citta");
    		return;
    	} 
    	
    	List<Distanza> distanze=this.model.getDistanze(scelta);
    	
    	this.txtResult.appendText("\n La citta selezionata ha i seguenti vicini: \n");
    	for(Distanza d: distanze) {
    		this.txtResult.appendText(d+"\n");
    	}
    	
    	
}
    

    @FXML
    void doSimula(ActionEvent event) {

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnAdiacenti != null : "fx:id=\"btnAdiacenti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaLista != null : "fx:id=\"btnCreaLista\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbProvider != null : "fx:id=\"cmbProvider\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbQuartiere != null : "fx:id=\"cmbQuartiere\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMemoria != null : "fx:id=\"txtMemoria\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert clDistanza != null : "fx:id=\"clDistanza\" was not injected: check your FXML file 'Scene.fxml'.";
        assert clQuartiere != null : "fx:id=\"clQuartiere\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	//metto nella tendina dei provider tutti i provider che prendo dal model
    	//lo faccio direttamente nel setModel cosi che al run dell'applicazione la tendina è gia popolata
    	this.cmbProvider.getItems().addAll(this.model.getAllProviders());
    }

}
