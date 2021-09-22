package main;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;


import connectivity.DatabaseConnection;
import queries.HotelQueries;



public class Menu {

	private DatabaseConnection datacon = new DatabaseConnection();
	private HotelQueries hotelQ = new HotelQueries();
	
	public void showMenu() {
		System.out.println("\nChoose an action:");
		System.out.println("1: User login to database");
		System.out.println("2: Search Hotels with given Prefix");
		System.out.println("3: Exit Database.");
		System.out.println("__________________________________________________________________");		
		System.out.println("|5: Fixed Database Creditentials(localhost,port,db2021,user,pass).|");	

	}
	
	public void select() throws IOException, ClassNotFoundException {
		int selection = 0;
		Scanner scanner = new Scanner(System.in);
		
		while (selection != 3) {
			this.showMenu();
			selection = scanner.nextInt();
			scanner.nextLine();
			
			switch(selection) {
			case 1:
				datacon.setCredentials(scanner);
				
				try {
					datacon.connect();
				} catch (SQLException sqle) {
					System.out.println("SQLException: " + sqle.getMessage());
					System.out.println("SQLState:" + sqle.getSQLState());
					System.out.println("VendorError:  " + sqle.getErrorCode());
				}
				break;
			case 2:
				hotelQ.showHotels(datacon);
				break;
			case 3:
				System.out.println("_________________________________________");			
				System.out.println("Aufwiedersehen!");
				System.out.println("_________________________________________");

				break;
			case 5:
				datacon.myDatabaseInfo();
				try {
					datacon.connect();
				} catch (SQLException sqle) {
					System.out.println("SQLException: " + sqle.getMessage());
					System.out.println("SQLState:" + sqle.getSQLState());
					System.out.println("VendorError:  " + sqle.getErrorCode());
				}
				break;	
				
			default:
				System.out.println("Select again.");
			}
		}
		scanner.close();
	}
}
