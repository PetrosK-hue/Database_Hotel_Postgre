package connectivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class DatabaseConnection {
	private static Connection conn;
	
	private String host;
	private int port; 
	private String database_name; 
	private String username; 
	private String password;
	
	public void setCredentials(Scanner scanner) {
		
		/*host = "localhost";
		port = 5432;
		database_name = "db2021";
		username = "postgres";
		password = "12345";
		*/
		System.out.println("Insert host name:");
		host = scanner.nextLine();
		System.out.println("Insert port:");
		port = scanner.nextInt();
		scanner.nextLine();
		System.out.println("Insert database name:");
		database_name = scanner.nextLine();
		System.out.println("Insert username:");
		username = scanner.nextLine();
	System.out.println("Insert password:");
		password = scanner.nextLine();
	}
	
	public void connect() throws SQLException {  
		try {
			Class.forName("org.postgresql.Driver");
			System.out.println("Connection Established.");
		} 
		catch (ClassNotFoundException e) {  
			System.err.print("ClassNotFoundException: Postgres JDBC");  
			System.err.println(e.getMessage());
		}
	
		try {
			String conn_url = "jdbc:postgresql://"+host+":"+port+"/"+database_name;
			
			conn = DriverManager.getConnection(conn_url, username, password);
			conn.setAutoCommit(false);
		} 
		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("SQLState:" + sqle.getSQLState());
			System.out.println("VendorError:  " + sqle.getErrorCode());
			throw sqle;
		}
	}

	public Connection getConn() {
		return conn;
	}

	public String getBase_name() {
		return database_name;
	}
	
	public static void closeConnection() {
		try {
			conn.close();
		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("SQLState:" + sqle.getSQLState());
			System.out.println("VendorError:  " + sqle.getErrorCode());
		}
	}
	
	public void myDatabaseInfo() {
		host = "localhost";
		port = 5432;
		database_name = "db2021";
		username = "postgres";
		password = "12345";
	}
}

