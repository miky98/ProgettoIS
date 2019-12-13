package Testing;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


import ControlClass.GestioneVideoControl;
import Database.DBManager;
import EntityClass.Sport;
import EntityClass.Video;
import EntityClass.VideoEvento;




public class MieiTest {

	GestioneVideoControl gestionevideo;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		try {
			
			Connection conn = DBManager.getConnection();
			
			String query;
				
				query = "CREATE TABLE VIDEO("
						+" ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
						+" NOME VARCHAR(30),"
						+" DATA DATE,"
						+" SPORT VARCHAR(30),"
						+" TIPO VARCHAR(30)"
						+");";
				
				try(PreparedStatement stmt = conn.prepareStatement(query)) {
					
					stmt.executeUpdate();
				}
				
				
				query = "CREATE TABLE GIORNALISTI("
						+" ID_VIDEO INT NOT NULL,"
						+" NOME VARCHAR(30) NOT NULL,"
						+" COGNOME VARCHAR(30) NOT NULL,"
						+" PRIMARY KEY(ID_VIDEO,NOME,COGNOME)"
						+");";
				
				
				try(PreparedStatement stmt = conn.prepareStatement(query)) {
					
					stmt.executeUpdate();
				}
				
				
				
				System.out.println("Inizializzazione DB completata.");
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@AfterClass
		public static void tearDownAfterClass() throws Exception {
			
			
			try {
				
				Connection conn = DBManager.getConnection();
				
				String query;
				
				query = "DROP TABLE GIORNALISTI; DROP TABLE VIDEO;";

				
				try(PreparedStatement stmt = conn.prepareStatement(query)) {
					
					stmt.executeUpdate();
				}
				
				
				System.out.println("Rimozione DB completata.");
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Before
		public void setUp() throws Exception {
			
			gestionevideo = new GestioneVideoControl();
		}

		@After
		public void tearDown() throws Exception {
			
			gestionevideo = null;
			
			
			Connection conn = DBManager.getConnection();
			
			
			String query = "DELETE FROM VIDEO;";
			
			try(PreparedStatement stmt = conn.prepareStatement(query)) {
				
				stmt.executeUpdate();
			}
			
					
			String query2 = "DELETE FROM GIORNALISTI;";

			try(PreparedStatement stmt2 = conn.prepareStatement(query2)) {
				
				stmt2.executeUpdate();
			}
		}
			
			@Test
			public void test01UnVideoPerNomeNonTrovato() throws SQLException {

				//ricerca uno sport di valore null 
				//ritorna zero in quanto non trova nulla
				
				VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli", 
								LocalDate.of(2019, Month.AUGUST, 24), 
								Sport.CALCIO);
			
				gestionevideo.caricaVideo(v1);

			
				
				ArrayList<Video> v_ricerca = gestionevideo.ricercaVideo(null, "Samp");

				
			
				System.out.println("Risultati della ricerca: " + v_ricerca.size());
				
				for(Video v : v_ricerca) {
					
					System.out.println(v+"\n");
				}

				
				assertEquals(0, v_ricerca.size());
				
				
				gestionevideo.rimuoviVideo(v1);
				
			}
			
			
			@Test
			public void test02UnVideoPerNomeNonTrovato() throws SQLException {

				//si verifica se è presente uno sport appartenente alla categoria
				//formula1. Se non presente ok, altriemnto fault
				
				VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli", 
								LocalDate.of(2019, Month.AUGUST, 24), 
								Sport.CALCIO);
			
				gestionevideo.caricaVideo(v1);
				
				ArrayList<Video> v_ricerca = gestionevideo.ricercaVideo(Sport.FORMULA1, null);
			
				System.out.println("Risultati della ricerca: " + v_ricerca.size());
				
				for(Video v : v_ricerca) {
					
					System.out.println(v+"\n");
				}

				assertEquals(0, v_ricerca.size());
				gestionevideo.rimuoviVideo(v1);
				
			}

			@Test
			public void test03UnVideoPerNomeNonTrovato() throws SQLException {

				//ricerca per nome e sport, ma non restituisce nulla perchè lo sport non è presente
				
				VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli", 
								LocalDate.of(2019, Month.AUGUST, 24), 
								Sport.CALCIO);
			
				gestionevideo.caricaVideo(v1);
				
				ArrayList<Video> v_ricerca = gestionevideo.ricercaVideo(Sport.CALCIO, "Samp");
			
				System.out.println("Risultati della ricerca: " + v_ricerca.size());
				
				for(Video v : v_ricerca) {
					
					System.out.println(v+"\n");
				}

				assertEquals(0, v_ricerca.size());
				gestionevideo.rimuoviVideo(v1);
				
			}
			
			@Test
			public void test04UnVideoPerNomeNonTrovato() throws SQLException {

				//ricerca per nome (Napoli) e sport null, restituisce un solo valore
				
				VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli", 
								LocalDate.of(2019, Month.AUGUST, 24), 
								Sport.CALCIO);
			
				gestionevideo.caricaVideo(v1);
				
				ArrayList<Video> v_ricerca = gestionevideo.ricercaVideo(null, "Napoli");
			
				System.out.println("Risultati della ricerca: " + v_ricerca.size());
				
				for(Video v : v_ricerca) {
					
					System.out.println(v+"\n");
				}

				assertEquals(1, v_ricerca.size());
				gestionevideo.rimuoviVideo(v1);
				
			}
			
			@Test
			public void test05UnVideoPerNomeNonTrovato() throws SQLException {

				//ricerca per nome e sport, ma non restituisce nulla perchè lo sport non è presente
				
				VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli", 
								LocalDate.of(2019, Month.AUGUST, 24), 
								Sport.CALCIO);
			
				gestionevideo.caricaVideo(v1);
				
				ArrayList<Video> v_ricerca = gestionevideo.ricercaVideo(Sport.CALCIO, null);
			
				System.out.println("Risultati della ricerca: " + v_ricerca.size());
				
				for(Video v : v_ricerca) {
					
					System.out.println(v+"\n");
				}

				assertEquals(1, v_ricerca.size());
				gestionevideo.rimuoviVideo(v1);	
			}
			
			@Test
			public void test06UnVideoPerNomeNonTrovato() throws SQLException {

				//ricerca per nome Napoli e sport Calcio, 
				//restituisce lo sport è presente
				
				VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli", 
								LocalDate.of(2019, Month.AUGUST, 24), 
								Sport.CALCIO);
			
				gestionevideo.caricaVideo(v1);
				
				ArrayList<Video> v_ricerca = gestionevideo.ricercaVideo(Sport.CALCIO, "Napoli");
			
				System.out.println("Risultati della ricerca: " + v_ricerca.size());
				
				for(Video v : v_ricerca) {
					
					System.out.println(v+"\n");
				}

				assertEquals(1, v_ricerca.size());
				gestionevideo.rimuoviVideo(v1);
				
			}
			
			@Test
			public void test07UnVideoPerNomeNonTrovato() throws SQLException {

				//ricerca per nome e sport, ma non restituisce nulla perchè lo sport non è presente
				
				VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli", 
								LocalDate.of(2019, Month.AUGUST, 24), 
								Sport.CALCIO);
			
				VideoEvento v2 = new VideoEvento("Partita Juve-Napoli", 
						LocalDate.of(2019, Month.AUGUST, 24), 
						Sport.CALCIO);
				
				VideoEvento v3 = new VideoEvento("Partita Napoli-Sampdoria", 
						LocalDate.of(2019, Month.AUGUST, 24), 
						Sport.CALCIO);
			
				gestionevideo.caricaVideo(v1);
				gestionevideo.caricaVideo(v2);
				gestionevideo.caricaVideo(v3);
				
				ArrayList<Video> v_ricerca = gestionevideo.ricercaVideo(null, "Milan");
			
				System.out.println("Risultati della ricerca: " + v_ricerca.size());
				
				for(Video v : v_ricerca) {
					
					System.out.println(v+"\n");
				}

				assertEquals(0, v_ricerca.size());
				gestionevideo.rimuoviVideo(v1);
				gestionevideo.rimuoviVideo(v2);
				gestionevideo.rimuoviVideo(v3);
			}
			
			@Test
			public void test08UnVideoPerNomeNonTrovato() throws SQLException {

				//ricerca per nome e sport, ma non restituisce nulla perchè lo sport non è presente
				
				VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli", 
								LocalDate.of(2019, Month.AUGUST, 24), 
								Sport.CALCIO);
				
				VideoEvento v2 = new VideoEvento("Partita Juve-Napoli", 
						LocalDate.of(2019, Month.AUGUST, 24), 
						Sport.CALCIO);
				
				VideoEvento v3 = new VideoEvento("Partita Napoli-Sampdoria", 
						LocalDate.of(2019, Month.AUGUST, 24), 
						Sport.CALCIO);
			
				gestionevideo.caricaVideo(v1);
				gestionevideo.caricaVideo(v2);
				gestionevideo.caricaVideo(v3);
				
				ArrayList<Video> v_ricerca = gestionevideo.ricercaVideo(Sport.FORMULA1, null);
			
				System.out.println("Risultati della ricerca: " + v_ricerca.size());
				
				for(Video v : v_ricerca) {
					
					System.out.println(v+"\n");
				}

				assertEquals(0, v_ricerca.size());
				gestionevideo.rimuoviVideo(v1);
				gestionevideo.rimuoviVideo(v2);
				gestionevideo.rimuoviVideo(v3);
			}
			
			@Test
			public void test09UnVideoPerNomeNonTrovato() throws SQLException {

				//ricerca per nome e sport, ma non restituisce nulla perchè lo sport non è presente
				
				VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli", 
								LocalDate.of(2019, Month.AUGUST, 24), 
								Sport.CALCIO);
				
				VideoEvento v2 = new VideoEvento("Partita Juve-Napoli", 
						LocalDate.of(2019, Month.AUGUST, 24), 
						Sport.CALCIO);
				
				VideoEvento v3 = new VideoEvento("Partita Napoli-Sampdoria", 
						LocalDate.of(2019, Month.AUGUST, 24), 
						Sport.CALCIO);
			
				gestionevideo.caricaVideo(v1);
				gestionevideo.caricaVideo(v2);
				gestionevideo.caricaVideo(v3);
				
				ArrayList<Video> v_ricerca = gestionevideo.ricercaVideo(Sport.CALCIO, "Milan");
			
				System.out.println("Risultati della ricerca: " + v_ricerca.size());
				
				for(Video v : v_ricerca) {
					
					System.out.println(v+"\n");
				}

				assertEquals(0, v_ricerca.size());
				gestionevideo.rimuoviVideo(v1);
				gestionevideo.rimuoviVideo(v2);
				gestionevideo.rimuoviVideo(v3);
			}
			
			@Test
			public void test10UnVideoPerNomeNonTrovato() throws SQLException {

				//ricerca per nome e sport, ma non restituisce nulla perchè lo sport non è presente
				
				VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli", 
								LocalDate.of(2019, Month.AUGUST, 24), 
								Sport.CALCIO);
				
				VideoEvento v2 = new VideoEvento("Partita Juve-Napoli", 
						LocalDate.of(2019, Month.AUGUST, 24), 
						Sport.CALCIO);
				
				VideoEvento v3 = new VideoEvento("Partita Napoli-Sampdoria", 
						LocalDate.of(2019, Month.AUGUST, 24), 
						Sport.CALCIO);
			
				gestionevideo.caricaVideo(v1);
				gestionevideo.caricaVideo(v2);
				gestionevideo.caricaVideo(v3);
				
				ArrayList<Video> v_ricerca = gestionevideo.ricercaVideo(null, "Samp");
			
				System.out.println("Risultati della ricerca: " + v_ricerca.size());
				
				for(Video v : v_ricerca) {
					
					System.out.println(v+"\n");
				}

				assertEquals(1, v_ricerca.size());
				gestionevideo.rimuoviVideo(v1);
				gestionevideo.rimuoviVideo(v2);
				gestionevideo.rimuoviVideo(v3);
			}
			
			@Test
			public void test11UnVideoPerNomeNonTrovato() throws SQLException {

				//ricerca per nome e sport, ma non restituisce nulla perchè lo sport non è presente
				
				VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli", 
								LocalDate.of(2019, Month.AUGUST, 24), 
								Sport.CALCIO);
				
				VideoEvento v2 = new VideoEvento("Partita Juve-Napoli", 
						LocalDate.of(2019, Month.AUGUST, 24), 
						Sport.CALCIO);
				
				VideoEvento v3 = new VideoEvento("Partita Napoli-Sampdoria", 
						LocalDate.of(2019, Month.AUGUST, 24), 
						Sport.CALCIO);
				
				VideoEvento v4 = new VideoEvento("GP MonzaCiaoTiVoglioBeneghfjkgiurgfiure",    //scrivo qualcosa per eccedere
						LocalDate.of(2019, Month.AUGUST, 24), 					//i 30 caratteri
						Sport.FORMULA1);
			
				gestionevideo.caricaVideo(v1);
				gestionevideo.caricaVideo(v2);
				gestionevideo.caricaVideo(v3);
				gestionevideo.caricaVideo(v4);
				
				ArrayList<Video> v_ricerca = gestionevideo.ricercaVideo(Sport.FORMULA1, null);
			
				System.out.println("Risultati della ricerca: " + v_ricerca.size());
				
				for(Video v : v_ricerca) {
					
					System.out.println(v+"\n");
				}

				assertEquals(1, v_ricerca.size());
				gestionevideo.rimuoviVideo(v1);
				gestionevideo.rimuoviVideo(v2);
				gestionevideo.rimuoviVideo(v3);
				gestionevideo.rimuoviVideo(v4);
			}
			
			@Test
			public void test12UnVideoPerNomeNonTrovato() throws SQLException {

				//ricerca per nome e sport, ma non restituisce nulla perchè lo sport non è presente
				
				VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli", 
								LocalDate.of(2019, Month.AUGUST, 24), 
								Sport.CALCIO);
				
				VideoEvento v2 = new VideoEvento("Partita Juve-Napoli", 
						LocalDate.of(2019, Month.AUGUST, 24), 
						Sport.CALCIO);
				
				VideoEvento v3 = new VideoEvento("Partita Napoli-Sampdoria", 
						LocalDate.of(2019, Month.AUGUST, 24), 
						Sport.CALCIO);
			
				gestionevideo.caricaVideo(v1);
				gestionevideo.caricaVideo(v2);
				gestionevideo.caricaVideo(v3);
				
				ArrayList<Video> v_ricerca = gestionevideo.ricercaVideo(Sport.CALCIO, "Samp");
			
				System.out.println("Risultati della ricerca: " + v_ricerca.size());
				
				for(Video v : v_ricerca) {
					
					System.out.println(v+"\n");
				}

				assertEquals(1, v_ricerca.size());
				gestionevideo.rimuoviVideo(v1);
				gestionevideo.rimuoviVideo(v2);
				gestionevideo.rimuoviVideo(v3);
			}
			
			@Test
			public void test13UnVideoPerNomeNonTrovato() throws SQLException {

				//ricerca per nome e sport, ma non restituisce nulla perchè lo sport non è presente
				
				VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli", 
								LocalDate.of(2019, Month.AUGUST, 24), 
								Sport.CALCIO);
				
				VideoEvento v2 = new VideoEvento("Partita Juve-Napoli", 
						LocalDate.of(2019, Month.AUGUST, 24), 
						Sport.CALCIO);
				
				VideoEvento v3 = new VideoEvento("Partita Napoli-Sampdoria", 
						LocalDate.of(2019, Month.AUGUST, 24), 
						Sport.CALCIO);
			
				gestionevideo.caricaVideo(v1);
				gestionevideo.caricaVideo(v2);
				gestionevideo.caricaVideo(v3);
				
				ArrayList<Video> v_ricerca = gestionevideo.ricercaVideo(null, "Napoli");
			
				System.out.println("Risultati della ricerca: " + v_ricerca.size());
				
				for(Video v : v_ricerca) {
					
					System.out.println(v+"\n");
				}

				assertEquals(3, v_ricerca.size());
				gestionevideo.rimuoviVideo(v1);
				gestionevideo.rimuoviVideo(v2);
				gestionevideo.rimuoviVideo(v3);
			}
			
			@Test
			public void test14UnVideoPerNomeNonTrovato() throws SQLException {

				//ricerca per nome e sport, ma non restituisce nulla perchè lo sport non è presente
				
				VideoEvento v1 = new VideoEvento("Commento Fiorentina-Napoli", 
								LocalDate.of(2019, Month.AUGUST, 24), 
								Sport.CALCIO);
				
				VideoEvento v2 = new VideoEvento("Commento Juve-Napoli", 
						LocalDate.of(2019, Month.AUGUST, 24), 
						Sport.CALCIO);
				
				VideoEvento v3 = new VideoEvento("Commento Napoli-Sampdoria", 
						LocalDate.of(2019, Month.AUGUST, 24), 
						Sport.CALCIO);
			
				gestionevideo.caricaVideo(v1);
				gestionevideo.caricaVideo(v2);
				gestionevideo.caricaVideo(v3);
				
				ArrayList<Video> v_ricerca = gestionevideo.ricercaVideo(null, "Napoli");
			
				System.out.println("Risultati della ricerca: " + v_ricerca.size());
				
				for(Video v : v_ricerca) {
					
					System.out.println(v+"\n");
				}

				assertEquals(3, v_ricerca.size());
				gestionevideo.rimuoviVideo(v1);
				gestionevideo.rimuoviVideo(v2);
				gestionevideo.rimuoviVideo(v3);
			}
						
			@Test
			public void test15UnVideoPerNomeNonTrovato() throws SQLException {

				//ricerca per nome e sport, ma non restituisce nulla perchè lo sport non è presente
				
				VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli", 
								LocalDate.of(2019, Month.AUGUST, 24), 
								Sport.CALCIO);
				
				VideoEvento v2 = new VideoEvento("Partita Juve-Napoli", 
						LocalDate.of(2019, Month.AUGUST, 24), 
						Sport.CALCIO);
				
				VideoEvento v3 = new VideoEvento("Partita Napoli-Sampdoria", 
						LocalDate.of(2019, Month.AUGUST, 24), 
						Sport.CALCIO);
			
				VideoEvento v4 = new VideoEvento("Commento Napoli-Sampdoria", 
						LocalDate.of(2019, Month.AUGUST, 24), 
						Sport.CALCIO);
				
				gestionevideo.caricaVideo(v1);
				gestionevideo.caricaVideo(v2);
				gestionevideo.caricaVideo(v3);
				gestionevideo.caricaVideo(v4);
				
				ArrayList<Video> v_ricerca = gestionevideo.ricercaVideo(Sport.CALCIO, "Samp");
			
				System.out.println("Risultati della ricerca: " + v_ricerca.size());
				
				for(Video v : v_ricerca) {
					
					System.out.println(v+"\n");
				}

				assertEquals(2, v_ricerca.size());
				gestionevideo.rimuoviVideo(v1);
				gestionevideo.rimuoviVideo(v2);
				gestionevideo.rimuoviVideo(v3);
				gestionevideo.rimuoviVideo(v4);
			}
			
			@Test
			public void test16UnVideoPerNomeNonTrovato() throws SQLException {

				//ricerca per nome e sport, ma non restituisce nulla perchè lo sport non è presente
				
				ArrayList<Video> v_ricerca = gestionevideo.ricercaVideo(null, "Napoli");
			
				System.out.println("Risultati della ricerca: " + v_ricerca.size());
				
				for(Video v : v_ricerca) {
					
					System.out.println(v+"\n");
				}

				assertEquals(0, v_ricerca.size());
			}
			
			@Test
			public void test17UnVideoPerNomeNonTrovato() throws SQLException {

				//ricerca per nome e sport, ma non restituisce nulla perchè lo sport non è presente
				
				VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli", 
								LocalDate.of(2019, Month.AUGUST, 24), 
								Sport.CALCIO);
				
				gestionevideo.caricaVideo(v1);
				
				ArrayList<Video> v_ricerca = gestionevideo.ricercaVideo(null, null);
			
				System.out.println("Risultati della ricerca: " + v_ricerca.size());
				
				for(Video v : v_ricerca) {
					
					System.out.println(v+"\n");
				}

				assertEquals(1, v_ricerca.size());
				gestionevideo.rimuoviVideo(v1);
			}
	}
