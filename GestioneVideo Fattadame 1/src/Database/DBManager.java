package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {

	//mi creo una variabile per la connessione
	private static Connection conn = null;
	//costruttore
	private DBManager() {}
	//provo a stabilire una connessione con una eccezione
	public static Connection getConnection() throws SQLException {
		
		if(conn == null) {
			conn = DriverManager.getConnection("jdbc:h2:./DBgestionevideo", "sa", "");
		}
		
		return conn;
	}
	
	//chiusura connessione
	public static void closeConnection() throws SQLException {

		if(conn != null) {
			conn.close();
		}
	}
}
