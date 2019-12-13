package GestioneVideo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import Database.DBManager;

public class InizializzaDB {

	public static void main(String[] args) {

		try {
			//recupera la connessione dalla classe DBManager
			Connection conn = DBManager.getConnection();
			//creo una stringa che mi servirà da query
			String query;
			
			query = "CREATE TABLE VIDEO("
					+" ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
					+" NOME VARCHAR(30),"
					+" DATA DATE,"
					+" SPORT VARCHAR(30),"
					+" TIPO VARCHAR(30)"
					+");";
			//prova a eseguire la query sul DB
			try(PreparedStatement stmt = conn.prepareStatement(query)) {
				//esegue la query
				stmt.executeUpdate();
			}
			
			//scrivo un'altra query
			query = "CREATE TABLE GIORNALISTI("
					+" ID_VIDEO INT NOT NULL,"
					+" NOME VARCHAR(30) NOT NULL,"
					+" COGNOME VARCHAR(30) NOT NULL,"
					+" PRIMARY KEY(ID_VIDEO,NOME,COGNOME)"
					+");";
			
			//prova eseguire la seconda query
			try(PreparedStatement stmt = conn.prepareStatement(query)) {
				
				stmt.executeUpdate();
			}
			
			
			
			System.out.println("Inizializzazione DB completata.");
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
