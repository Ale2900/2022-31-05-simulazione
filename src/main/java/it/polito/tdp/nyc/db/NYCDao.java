package it.polito.tdp.nyc.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javadocmd.simplelatlng.LatLng;

import it.polito.tdp.nyc.model.City;
import it.polito.tdp.nyc.model.Hotspot;

public class NYCDao {
	
	public List<Hotspot> getAllHotspot(){
		String sql = "SELECT * FROM nyc_wifi_hotspot_locations";
		List<Hotspot> result = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Hotspot(res.getInt("OBJECTID"), res.getString("Borough"),
						res.getString("Type"), res.getString("Provider"), res.getString("Name"),
						res.getString("Location"),res.getDouble("Latitude"),res.getDouble("Longitude"),
						res.getString("Location_T"),res.getString("City"),res.getString("SSID"),
						res.getString("SourceID"),res.getInt("BoroCode"),res.getString("BoroName"),
						res.getString("NTACode"), res.getString("NTAName"), res.getInt("Postcode")));
			}
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}

		return result;
	}
	
	
	//QUERY CHE MI POPOLA LA TENDINA DA CUI SCEGLIERE L'INPUT DEL GRAFO
	public List<String> getAllProviders(){
		String sql="SELECT DISTINCT provider "         //perche non voglio i duplicati
				+ "FROM nyc_wifi_hotspot_locations "   
				+ "ORDER BY provider";                //cosi la lista sarà in ordine alfabetico per nome, le stringhe sanno da sole come ordinarsi
		List<String> result=new ArrayList<String>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			
			while(res.next()) { //finche stiamo scorrendo il risultato della query
				result.add(res.getString("Provider")); //mi interessa solo il nome perche la sto considerando come una stringa
				
				
			}
			conn.close();
			return result;
			
			
		}catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
			
		}

				}
	
	
	//QUERY PER I VERTICI DEL GRAFO: mi da una lista di citta (classe che ho creato nuova)  a cui viene fornito almeno un hotspot da quel provider
	
	
	public List<City> getVertici(String provider){ 
		//infatti il provider è il parametro che devo passare per la creazione del grafo
		String sql="SELECT  distinct City, AVG(Latitude) AS Lat, AVG(Longitude) AS Lng, COUNT(*) AS num "  //media delle latituidi e media delle longituidini come distanza, mentre il numero è quanti provider uguali nelle due citta
				+ "FROM nyc_wifi_hotspot_locations "
				+ "WHERE Provider=? "                                                                      //sarà sempre quello che mi passa l'utente
				+ "GROUP BY City "
				+ "ORDER BY City";
	
		//ATTENZIONE: la libreria LatLng calcola da sola la distanza tra due vertici, considerando, come dice il testo, sia la media delle latitudini sia la media delle longitudini
		//di tutti gli hotspot che sono presenti nel DB per quel provider selezionato
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, provider);
			ResultSet res = st.executeQuery();
			List<City> result=new ArrayList<City>();
			
			while(res.next()) {
				result.add(new City(res.getString("City"), new LatLng(res.getDouble("Lat"), res.getDouble("Lng")), res.getInt("num")));
				//Lat e lng corrispondono alla posizione
				
			}
			conn.close();
			return result;
			
			
		}catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		
		}
		
	}
	
}
