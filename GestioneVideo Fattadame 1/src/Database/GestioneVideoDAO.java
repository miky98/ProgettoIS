package Database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import EntityClass.Giornalista;
import EntityClass.Sport;
import EntityClass.Video;
import EntityClass.VideoApprofondimento;
import EntityClass.VideoEvento;


public class GestioneVideoDAO {
	
	//crea un video nel DB, assume in ingresso il nome, la data e lo sport
	public static Video createVideo(String nome, LocalDate data, Sport sport) throws SQLException {
		
		Video video = new Video(nome, data, sport);
		
		create(video);
		
		return video;
	}
		
	//crea un video evento nel db
	public static VideoEvento createVideoEvento(String nome, LocalDate data, Sport sport) 
	
		throws SQLException {
		
		VideoEvento video = new VideoEvento(nome, data, sport);
		
		create(video);
		
		return video;
	
	}
	
	//crea un video approfondimento nel DB
	public static VideoApprofondimento createVideoApprofondimento(String nome, LocalDate data, Sport sport) throws SQLException {
		
		VideoApprofondimento video = new VideoApprofondimento(nome, data, sport);
		
		create(video);
		
		return video;
	}

    //implementazione della funzione create(): inserisce tramite una query il video nel db
	public static int create(Video v) throws SQLException {
		
		Connection conn = DBManager.getConnection();
		
		int id_video = -1;
		
		String query = "INSERT INTO VIDEO VALUES(NULL,?,?,?,?);";
		
		try(PreparedStatement stmt = conn.prepareStatement(query)) {
			
			stmt.setString(1, v.getNome());
			stmt.setDate(2, Date.valueOf(v.getData()));
			stmt.setString(3, v.getSport().toString());
			
			//inserisce nella query il tipo di video in base a quello passato come parametro
			if(v instanceof VideoEvento) {
				stmt.setString(4, "EVENTO");
			}
			else if(v instanceof VideoApprofondimento) {
				stmt.setString(4, "APPROFONDIMENTO");
			}
			else {
				stmt.setString(4, "");
			}
			
			
			stmt.executeUpdate();
			
			try(ResultSet result = stmt.getGeneratedKeys()) {
				
				if(result.next()) {
					id_video = result.getInt(1);
				}
			}
		}
		
		v.setId(id_video);
		
		return id_video;
	}
	
	//ricerca di un video tramite l'id passato dall'utente
	public static Video read(int id) throws SQLException {
		
		Video v = null;
		
		Connection conn = DBManager.getConnection();
		
		String query = "SELECT NOME,DATA,SPORT,TIPO FROM VIDEO WHERE ID=?;";
		
		try(PreparedStatement stmt = conn.prepareStatement(query)) {
			
			stmt.setInt(1, id);
			
			try(ResultSet result = stmt.executeQuery()) {
				
				if(result.next()) {
					
					String nome = result.getString(1);
					LocalDate data = result.getDate(2).toLocalDate();
					Sport sport = Sport.valueOf(result.getString(3));
					String tipo = result.getString(4);
					
					
					if(tipo.equals("EVENTO")) {
						
						v = new VideoEvento(id, nome, data, sport);
						
					}
					else if(tipo.equals("APPROFONDIMENTO")) {
						
						//se il video è di tipo approfondimento, bisogna visualizzare anche i giornalisti 
						//che l'hanno creato
						//dunque faccio un'altra query in cui ritorno i giornalisti che hanno l'id uguale
						//a quello che ho trovato nella prima query
						
						//mi creo un oggetto videoApprofondimento con i campi trovati in precedenza
						v = new VideoApprofondimento(id, nome, data, sport);
						
						String query2 = "SELECT NOME,COGNOME FROM GIORNALISTI WHERE ID_VIDEO=?;";
						
						try(PreparedStatement stmt2 = conn.prepareStatement(query2)) {
							
							stmt2.setInt(1, id);
							
							try(ResultSet result2 = stmt2.executeQuery()) {
								
								while(result2.next()) {
									
									String nome_g = result.getString(1);
									String cognome_g = result.getString(2);
									
									Giornalista g = new Giornalista(nome_g, cognome_g);
									
									//una volta trovato/i li aggiungo all'oggetto creato i precedenza v
									((VideoApprofondimento)v).addGiornalista(g);
									
								}
							}
						}
					}
					else {
						v = new Video(id, nome, data, sport);
					}
					
				}
			}
		}
		
		return v;

	}

	//preleva tutti i video dal db e li mette dentro una lista, per poi ritornarla al programma principale
	public static ArrayList<Video> readAll() throws SQLException {
		
		ArrayList<Video> lista = new ArrayList<Video>();
		
		Connection conn = DBManager.getConnection();
		
		String query = "SELECT ID,NOME,DATA,SPORT,TIPO FROM VIDEO;";
		
		try(PreparedStatement stmt = conn.prepareStatement(query)) {
						
			try(ResultSet result = stmt.executeQuery()) {
				
				while(result.next()) {
					
					int id = result.getInt(1);
					String nome = result.getString(2);
					LocalDate data = result.getDate(3).toLocalDate();
					Sport sport = Sport.valueOf(result.getString(4));
					String tipo = result.getString(5);
					
					Video v = null;
					
					if(tipo.equals("EVENTO")) {
						v = new VideoEvento(id, nome, data, sport);
					}
					else if(tipo.equals("APPROFONDIMENTO")) {
						v = new VideoApprofondimento(id, nome, data, sport);
					}
					else {
						v = new Video(id, nome, data, sport);
					}
					
					
					lista.add(v);
				}
			}
		}
		
		return lista;

	}
	
	//modifica un video nel db
	public static void update(Video v) throws SQLException {
		
		Connection conn = DBManager.getConnection();
		
		int id_video = v.getId();
		
		
		String query = "UPDATE VIDEO SET NOME=?, DATA=?, SPORT=?, TIPO=? WHERE ID=?";
		
		try(PreparedStatement stmt = conn.prepareStatement(query)) {
			
			stmt.setString(1, v.getNome());
			stmt.setDate(2, Date.valueOf(v.getData()));
			stmt.setString(3, v.getSport().toString());
			
			if(v instanceof VideoEvento) {
				stmt.setString(4, "EVENTO");
			}
			else if(v instanceof VideoApprofondimento) {
				stmt.setString(4, "APPROFONDIMENTO");
			}
			else {
				stmt.setString(4, "");
			}
			
			//dopo aver settato gli altri campi, specifica l'id del video da modificare,passato dall'utente
			stmt.setInt(5, id_video);
			
			stmt.executeUpdate();
		}
		
		
		//dopo aver modificato il video, verifica se era di tipo approfondimento
		//se vero andrà a eliminare dal db tutti i giornalisti a esso associati
		if(v instanceof VideoApprofondimento) {
						
			String query2 = "DELETE FROM GIORNALISTI WHERE ID_VIDEO=?;";
			
			try(PreparedStatement stmt2 = conn.prepareStatement(query2)) {
				
				stmt2.setInt(1, id_video);
				
				stmt2.executeUpdate();
			}
			
			
			//infine andrà ad aggiornare i giornalisti relativi al video modificato, andandoli a prelevare
			//dalla lista giornalisti ad esso associato
			String query3 = "INSERT INTO GIORNALISTI VALUES(?,?,?);";
			
			try(PreparedStatement stmt3 = conn.prepareStatement(query3)) {
				
				ArrayList<Giornalista> lista_giornalisti = ((VideoApprofondimento) v).getGiornalisti();
				
				for(Giornalista g : lista_giornalisti) {
					
					String nome_g = g.getNome();
					String cognome_g = g.getCognome();
				
					stmt3.setInt(1, id_video);
					stmt3.setString(2, nome_g);
					stmt3.setString(3, cognome_g);
				
					stmt3.executeUpdate();
				}
			}
			
		}
		
	}
	
	//elimina un video passato dall'utente
	public static void delete(Video v) throws SQLException {
		
		Connection conn = DBManager.getConnection();
		
		int id_video = v.getId();
		
		String query = "DELETE FROM VIDEO WHERE ID=?;";
		
		try(PreparedStatement stmt = conn.prepareStatement(query)) {
			
			stmt.setInt(1, id_video);
			
			stmt.executeUpdate();
		}
		
		//se il video era un VideoApprofondimento, allora andrà a eliminare anche i giornalisti nel db
		if(v instanceof EntityClass.VideoApprofondimento) {
		
			String query2 = "DELETE FROM GIORNALISTI WHERE ID_VIDEO=?;";
			
			try(PreparedStatement stmt2 = conn.prepareStatement(query2)) {
				
				stmt2.setInt(1, id_video);
				
				stmt2.executeUpdate();
			}
		}
	}
}
