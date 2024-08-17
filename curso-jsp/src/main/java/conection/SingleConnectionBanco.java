package conection;

import java.sql.Connection;
import java.sql.DriverManager;

public class SingleConnectionBanco {

	private static String banco="jdbc:postgresql://localhost:5432/curso-jsp?autoReconnect=true";
	private static String user="postgres";
	private static String senha="Rock@2002";
	private static Connection connection = null;
	
	static{
		connectar();
	}
	
	public SingleConnectionBanco() {
		connectar();
	}
	
	private static void connectar() {
		try {
			if(connection==null) {
				Class.forName("org.postgresql.Driver");
				connection = DriverManager.getConnection(banco, user, senha);
				connection.setAutoCommit(false);
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public static Connection getConnection() {
		return connection;
	}
	
}
