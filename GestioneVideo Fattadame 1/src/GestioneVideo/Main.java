package GestioneVideo;

import java.sql.SQLException; 
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

import ControlClass.GestioneVideoControl;
import EntityClass.Giornalista;
import EntityClass.Sport;
import EntityClass.Video;
import EntityClass.VideoApprofondimento;
import EntityClass.VideoEvento;



public class Main {

	public static void main(String[] args) throws SQLException {

		GestioneVideoControl gestionevideo = new GestioneVideoControl();


		String nome_evento = "Partita Fiorentina-Napoli";

		LocalDate data_evento = LocalDate.of(2019, Month.AUGUST, 24);

		VideoEvento v1 = new VideoEvento(nome_evento, data_evento, Sport.CALCIO);



		nome_evento = "Partita Juventus-Napoli";

		data_evento = LocalDate.of(2019, Month.AUGUST, 31);

		VideoEvento v2 = new VideoEvento(nome_evento, data_evento, Sport.CALCIO);



		nome_evento = "Partita Napoli-Sampdoria";

		data_evento = LocalDate.of(2019, Month.SEPTEMBER, 14);

		VideoEvento v3 = new VideoEvento(nome_evento, data_evento, Sport.CALCIO);



		nome_evento = "Commento Napoli-Sampdoria";

		data_evento = LocalDate.of(2019, Month.SEPTEMBER, 14);

		VideoApprofondimento v4 = new VideoApprofondimento(nome_evento, data_evento, Sport.CALCIO);
		
		
		nome_evento = "Commento Napoli-Lazio";

		data_evento = LocalDate.of(2019, Month.SEPTEMBER, 7);

		VideoApprofondimento v5 = new VideoApprofondimento(nome_evento, data_evento, Sport.CALCIO);

		v4.addGiornalista(new Giornalista("TIZIO", "CAIO"));
		v4.addGiornalista(new Giornalista("PIPPO", "PLUTO"));


		gestionevideo.caricaVideo(v1);
		gestionevideo.caricaVideo(v2);
		gestionevideo.caricaVideo(v3);
		gestionevideo.caricaVideo(v4);
		gestionevideo.caricaVideo(v5);

		ArrayList<Video> v_ricerca = gestionevideo.ricercaVideo(Sport.CALCIO, "Napoli");


		System.out.println("Risultati della ricerca:\n");

		for(Video v : v_ricerca) {

			System.out.println(v+"\n");
		}



		gestionevideo.rimuoviVideo(v1);
		gestionevideo.rimuoviVideo(v2);
		gestionevideo.rimuoviVideo(v3);
		gestionevideo.rimuoviVideo(v4);
		gestionevideo.rimuoviVideo(v5);
	
	}

}
