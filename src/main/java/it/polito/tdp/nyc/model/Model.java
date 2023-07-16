package it.polito.tdp.nyc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.nyc.db.NYCDao;

public class Model {
	private NYCDao dao;
	
	private Graph<City, DefaultWeightedEdge> grafo;
	
	private List<City> cities;
	public Model() {
		dao=new NYCDao();
		
	}
	
	//metodo che mi permette di ottenere una lista di providers cosi che posso passarli al controller per popolare la prima tendina
	public List<String> getAllProviders(){
		List<String> providers=this.dao.getAllProviders();
		return providers;
	}
	
	public void creaGrafo(String provider) {
		this.grafo=new SimpleWeightedGraph<City, DefaultWeightedEdge>(DefaultWeightedEdge.class);
	
		this.cities=this.dao.getVertici(provider); // c'è l'altro metodo del model che me le restituisce, cosi che ogni volta che si crea un grafo i vertici giusti vengono restituiti
		//aggiungiamo i vertici
		
		List<City> vertici=this.dao.getVertici(provider); //devo passargli il parametro
		
		Graphs.addAllVertices(this.grafo, vertici); //al metodo addAllVertices va sempre passata una collection di vertici
		
		//per il peso dell'eventuale arco ora uso la libreria simplelatlng
		//calcoliamo prima il peso tra le due citta e poi lo settiamo
		for(City c1: vertici) {
			for(City c2: vertici) {
				if(!c1.equals(c2)) { //nella classe City abbiamo usato il nome per definire l'uguaglianza
					double peso=LatLngTool.distance(c1.getPosizione(), c2.getPosizione(), LengthUnit.KILOMETER); //perche mi dice che il peso è la distanza media in chilometri
				Graphs.addEdge(this.grafo, c1, c2, peso);
			}}
		}
	
	}
	
	//metodo che mi serve nel controller per stampare il numero di vertici
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	
	//metodo che mi serve nel controller per stampare il numero di archi
	public int getArchi() {
		return this.grafo.edgeSet().size();
	}
	

	
	//ATTENZIONE: QUANDO, COME IN QUESTO CASO, C'è BISOGNO DI POPOLARE UNA SECONDA TENDINA CON I VERTICI DEL GRAFO RISULTANTI DA UN PRIMO PARAMETRO
	//            RICORDA DI FARE QUESTO METODO CHE TI MERMETTE DI OTTENTE UNA LISTA DI SOLI VERTICI DA POTER PASSARE AL CONTROLLER PER POPLARE LA SECONDA TENDINA
	//fai cosi, inizializza la lista direttamente nel metodo del grafo e poi ritornala
	public List<City> getCitta() {
		return cities; 
	}
	
	public List<Distanza> getDistanze(City cittascelta){
		List<Distanza> result=new ArrayList<Distanza>();
		
		//prendo una lista di vicini cosicchè posso avere una lista dei nodi successori a quello selezionato e la loro distanza da essi
		List<City> vicini=Graphs.neighborListOf(this.grafo, cittascelta);
		
		//cosi ho la lista dei vicini di una sola citta
		for(City c: vicini) {
			result.add(new Distanza(c.getNome(), this.grafo.getEdgeWeight(this.grafo.getEdge(cittascelta, c)))); //gli faccio prendere il peso di quell arco tra la citta scelta e la citta ed ognuno dei suoi vicini
			//ATTENZIONE: sono archi questi, quindi il peso viene calcolato gia alla creazione del grafo
		}
		Collections.sort(result, new Comparator<Distanza>() {

			@Override
			public int compare(Distanza o1, Distanza o2) {
				// TODO Auto-generated method stub
				return o1.getDistanza().compareTo(o2.getDistanza()); //perche dice di ordinare in ordine crescente di distanza
			}
			
		});
		return result;
	}
	
	
	
}
