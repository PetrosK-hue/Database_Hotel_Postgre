package main;

import connectivity.DatabaseConnection;

public class MainClass {

	public static void main(String[] args) {
		Menu menu = new Menu();
		
		try {
			System.out.print("Welcome!");
			menu.select();
			DatabaseConnection.closeConnection();
		} catch (Exception e) {
			System.out.print(e.getMessage());
			System.out.println(e.getClass().getCanonicalName());
		}
	}
}
