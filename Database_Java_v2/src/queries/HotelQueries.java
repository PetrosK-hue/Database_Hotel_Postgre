package queries;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import connectivity.DatabaseConnection;

public class HotelQueries {

	Scanner scanner = new Scanner(System.in);
	public int choice, hotelchoice = 0;

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////	FUNCTION 2.(0)				///////////////////////////////////////////////////////////////////			
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void showHotels(DatabaseConnection datacon) {
		String prefix;

		String chosenname;
		int i = 1;

		System.out.println("Insert prefix letter of Hotel you want to search :");
		prefix = scanner.nextLine();

		String query = "SELECT name FROM \"hotel\" hot WHERE left(hot.\"name\",1) = \'" + prefix + "\'";

		try {

			Statement stmt = datacon.getConn().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ResultSet res = stmt.executeQuery(query);

			System.out.println("NumberOfChoice" + "Hotel Name\t");
			System.out.println("_________________________________________");

			while (res.next()) {

				System.out.println(i + ") " + res.getString("name"));
				i++;
			}

			System.out.println("		");
			System.out.println("Choose Hotel to work on:");
			choice = scanner.nextInt();
			scanner.nextLine();
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////			HOTEL MENU						///////////////////////////////////////////////////////////////////			
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			res.absolute(choice);
			chosenname = res.getString("name");
			System.out.println("		");
			System.out.println("You Chose...:" + chosenname);
			System.out.println("		");
			System.out.println("---------HOTEL ACTIONS MENU:----------	");
			System.out.println("		");

			while (hotelchoice != 4) {
				this.showMenuHotel();
				hotelchoice = scanner.nextInt();
				scanner.nextLine();
				hotelaction(hotelchoice, res, datacon);
			}

			res.close();
			stmt.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private void hotelaction(int hotelchoice, ResultSet rs, DatabaseConnection datacon) throws SQLException {

		switch (hotelchoice) {
		case 1:
			searchHotelClients(rs, datacon);
			break;

		case 2:
			viewHotelBookings(rs, datacon);
			break;
		case 3:
			viewAvailableRooms_SpecificDate(rs, datacon);
			break;
		case 4:
			System.out.println("Exiting Hotel Menu.");
			return;

		}
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////	FUNCTION 2. 1					///////////////////////////////////////////////////////////////////			
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void searchHotelClients(ResultSet rs, DatabaseConnection datacon) throws SQLException {
		int i = 0;
		System.out.println("Insert prefix letter of Client's Last Name :");
		String prefix1;
		prefix1 = scanner.nextLine();
		rs.absolute(choice);
		String hotname = rs.getString("name");

		String query1 = "SELECT lname , fname, \"idPerson\"  FROM \"person\" per, \"hotel\" hot, \"room\" r , \"roombooking\" rb WHERE( hot.name = \'"
				+ hotname + "\' AND left(per.lname,1) = \'" + prefix1
				+ "\' AND r.\"idHotel\" = hot.\"idHotel\" AND r.\"idRoom\" = rb.\"roomID\" AND rb.\"bookedforpersonID\" = per.\"idPerson\" AND per.\"idPerson\" NOT IN(SELECT \"idEmployee\" FROM employee )) ORDER BY per.lname";

		Statement stmt = datacon.getConn().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		ResultSet res1 = stmt.executeQuery(query1);

		System.out.println("NumOfClients:" + "Last Name\t" + "First Name\t" + "ID PERSON \t");
		System.out.println("_________________________________________");

		while (res1.next()) {

			System.out.println(String.format(" %d) \t %-12s   %-12s   %-12d ", i, res1.getString("lname"),
					res1.getString("fname"), res1.getInt("idPerson")));
			i++;
		}
		System.out.println("_________________________________________");
		System.out.println("  	   ");
		return;
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////	FUNCTION 2. 2					///////////////////////////////////////////////////////////////////			
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void viewHotelBookings(ResultSet rs, DatabaseConnection datacon) throws SQLException {

		int i = 1;
		rs.absolute(choice);
		String hotname = rs.getString("name");

		System.out.println("Insert Client's ID to Show His/Her Bookings :");
		int IDclient;
		IDclient = scanner.nextInt();
		scanner.nextLine();
		String query2 = "SELECT hb.\"idhotelbooking\", rb.\"roomID\",rb.\"checkin\" ,rb.\"checkout\",rb.\"rate\"\r\n"
				+ "FROM \"hotelbooking\" hb , \"client\" cli , \"roombooking\" rb , \"room\" r,\"hotel\" hot \r\n"
				+ "WHERE(hb.\"idhotelbooking\" = rb.\"hotelbookingID\" AND rb.\"roomID\" = r.\"idRoom\" \r\n"
				+ "	  AND r.\"idHotel\" = hot.\"idHotel\" AND hot.name = \'" + hotname
				+ "\' AND hb.\"bookedbyclientID\" = cli.\"idClient\" AND cli.\"idClient\" = \'" + IDclient + "\' )\r\n"
				+ "ORDER BY hb.\"idhotelbooking\" ";

		Statement stmt2 = datacon.getConn().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		ResultSet res2 = stmt2.executeQuery(query2);

		System.out.println("NumOfBookings:  " + "Booking ID  " + "Room ID  " + "CheckIn DATE  " + "CheckOut DATE  "
				+ "  Room Price  ");
		System.out.println("_________________________________________");

		while (res2.next()) {

			System.out.println(String.format(" %d) \t\t  %-7d   %-7d   %-15s  %-15s  %-10d ", i, res2.getInt(1),
					res2.getInt(2), res2.getString(3), res2.getString(4), res2.getInt(5)));
			i++;
		}
		System.out.println("_________________________________________");
		System.out.println("  	   ");

		System.out.println("_________________________________________");
		System.out.println(" Do you want to Update A Booking ? ");
		System.out.println(" 1-Yes  |  2-No ");
		int updatechoice = scanner.nextInt();
		scanner.nextLine();

		if (updatechoice == 1) {
			updateHotelBooking(res2, datacon);
		}

		return;
	}

	private void updateHotelBooking(ResultSet res2, DatabaseConnection datacon) throws SQLException {

		System.out.println(" Choose A Booking ( from NumOfBookings) ");
		int bookchoice = scanner.nextInt();
		scanner.nextLine();
		res2.absolute(bookchoice);
		int idhotelbooking = res2.getInt(1);
		int roomid = res2.getInt(2);
		System.out.println(" You Chose: " + idhotelbooking);

		int updatechoice = 0;
		while (updatechoice != 4) {
			System.out.println(" Choose Update on: 1) Check-In 2)Check-Out 3)Rate ");
			System.out.println(" OR 4) to Exit.");
			updatechoice = scanner.nextInt();
			scanner.nextLine();
			switch (updatechoice) {
			case 1:
				System.out.println(" Choose NEW Check In DATE:( YYYY-MM-DD) ");
				String checkin = scanner.nextLine();
				String queryIn = "UPDATE roombooking\r\n" + "SET checkin = ?  \r\n" + "WHERE \"hotelbookingID\" =? AND \"roomID\" = ? ";
				System.out.println(" UPDATE DONE. ");
				System.out.println("_________________________________________");
				try {
					PreparedStatement stmt4 = datacon.getConn().prepareStatement(queryIn);
					stmt4.setDate(1, java.sql.Date.valueOf(checkin));
					stmt4.setInt(2, idhotelbooking);
					stmt4.setInt(3, roomid);
					stmt4.executeUpdate();
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				break;

			case 2:
				System.out.println(" Choose NEW Check OUT DATE:( YYYY-MM-DD) ");
				String checkOut = scanner.nextLine();
				String queryOut = "UPDATE roombooking\r\n" + "SET checkout = ?  \r\n" + "WHERE \"hotelbookingID\" =?  AND \"roomID\" = ? ";
				System.out.println(" UPDATE DONE. ");
				System.out.println("_________________________________________");
				try {
					PreparedStatement stmt4 = datacon.getConn().prepareStatement(queryOut);
					stmt4.setDate(1, java.sql.Date.valueOf(checkOut));
					stmt4.setInt(2, idhotelbooking);
					stmt4.setInt(3, roomid);
					stmt4.executeUpdate();
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				break;

			case 3:
				System.out.println(" Choose NEW Room Price: ");
				int price = scanner.nextInt();
				scanner.nextLine();
				String queryPrice = "UPDATE roombooking\r\n" + "SET checkout = ?  \r\n"
						+ "WHERE \"hotelbookingID\" =? AND \"roomID\" = ? ";
				System.out.println(" UPDATE DONE. ");
				System.out.println("_________________________________________");
				try {
					PreparedStatement stmt4 = datacon.getConn().prepareStatement(queryPrice);
					stmt4.setInt(1, price);
					stmt4.setInt(2, idhotelbooking);
					stmt4.setInt(3, roomid);
					stmt4.executeUpdate();
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				break;
			default:
				break;

			}
		}

	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////	FUNCTION 2. 3					///////////////////////////////////////////////////////////////////			
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	

	private void viewAvailableRooms_SpecificDate(ResultSet rs, DatabaseConnection datacon) throws SQLException {

		int i = 1;
		int bookchoice;
		rs.absolute(choice);
		String hotname = rs.getString("name");

		System.out.println("Insert The Starting Date :( YYYY-MM-DD)");
		String dateIN = scanner.nextLine();
		System.out.println("Insert The Ending Date :( YYYY-MM-DD)");
		String dateOUT = scanner.nextLine();

		String query3 = "SELECT DISTINCT r_1.\"number\" ,r_1.\"idRoom\", r_1.roomtype\r\n"
				+ "FROM room r_1, \"hotel\" hot ,\"roombooking\" rb \r\n"
				+ "WHERE hot.\"idHotel\"= r_1.\"idHotel\" AND hot.name = \'" + hotname
				+ "\' AND  r_1.\"idRoom\" = rb.\"roomID\" AND rb.\"roomID\" NOT IN ( SELECT DISTINCT r_2.\"idRoom\"\r\n"
				+ "					   FROM hotel h, room r_2,roombooking rb_1\r\n"
				+ "                       WHERE r_2.\"idRoom\" = rb_1.\"roomID\" \r\n"
				+ "						   AND (rb_1.checkin <= \'" + dateIN + "\' AND rb_1.checkout >= \'" + dateIN
				+ "\' \r\n" + "								OR rb_1.checkin < \'" + dateOUT
				+ "\' AND rb_1.checkout >= \'" + dateOUT + "\' \r\n" + "								OR \'" + dateIN
				+ "\' <= rb_1.checkin AND \'" + dateOUT + "\' >= rb_1.checkin))\r\n" + "ORDER BY r_1.\"number\"";

		Statement stmt3 = datacon.getConn().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		ResultSet res3 = stmt3.executeQuery(query3);

		System.out.println("NumOfAvailableRooms:  " + "Room Number  " + "Room ID  " + " Room Type");
		System.out.println("_________________________________________");

		while (res3.next()) {

			System.out.println(String.format(" %d) \t\t\t  %-9d   %-9d   %-12s   ", i, res3.getInt(1), res3.getInt(2),
					res3.getString(3)));
			i++;
		}
		System.out.println("_________________________________________");
		System.out.println("  	   ");

		System.out.println("_________________________________________");
		System.out.println(" Do you want to Reserve ? ");
		System.out.println(" 1-Yes  |  2-No ");
		bookchoice = scanner.nextInt();
		scanner.nextLine();

		if (bookchoice == 1) {
			bookAnRoom(res3, datacon);
		}

		return;

	}

	//////////////////// INSERT NEW RESERVATION OF ROOM
	//////////////////// ///////////////////////////////////////////
	private void bookAnRoom(ResultSet res3, DatabaseConnection datacon) throws SQLException {
		System.out.println(" Choose Room ( from NumOfAvailableRooms) ");
		int roomchoice = scanner.nextInt();
		scanner.nextLine();

		res3.absolute(roomchoice);
		int roomid = res3.getInt(2);
		System.out.println(" You Chose: " + roomid);

		System.out.println(" Please enter Client ID: ");
		int enterID = scanner.nextInt();
		scanner.nextLine();

		System.out.println(" Please enter VALID(!) checkIn DATE: ( YYYY-MM-DD) ");
		String checkin = scanner.nextLine();
		System.out.println(" Please enter VALID(!) checkOUT DATE:( YYYY-MM-DD) ");
		String checkout = scanner.nextLine();

		String ins_HBquery = "INSERT INTO hotelbooking(\"reservationdate\" , \"cancellationdate\", \"bookedbyclientID\" ,\"payed\",  \"status\" )\r\n"
				+ "VALUES (current_date, current_date + 20, ? ,'false', 'confirmed' )";

		System.out.println(" Inserting new Hotel Booking.");
		try {
			PreparedStatement stmt4 = datacon.getConn().prepareStatement(ins_HBquery);
			stmt4.setInt(1, enterID);
			stmt4.executeUpdate();
			System.out.println(" Insertion Done.");

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		Statement stmt_h = datacon.getConn().createStatement();
		String Maxidhotelbooking = "SELECT MAX(idhotelbooking) FROM hotelbooking";
		ResultSet maxid = stmt_h.executeQuery(Maxidhotelbooking);

		System.out.println(" Updating Room Booking.");

		while (maxid.next()) {
			try {
				String ins_RBquery = "INSERT INTO roombooking(\"hotelbookingID\", \"roomID\",\"bookedforpersonID\",\"checkin\",\"checkout\"  )\r\n"
						+ "VALUES ( ? , ? , ? , ? , ?  )";
				PreparedStatement stmt5 = datacon.getConn().prepareStatement(ins_RBquery);
				stmt5.setInt(1, maxid.getInt(1));
				stmt5.setInt(2, roomid);
				stmt5.setInt(3, enterID);
				stmt5.setDate(4,  java.sql.Date.valueOf(checkin));
				stmt5.setDate(5,  java.sql.Date.valueOf(checkout));
				stmt5.executeUpdate();
				
				
				stmt5.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			break;
		}

		System.out.println(" Update Done.");

		System.out.println("_________________________________________");
		System.out.println("");
		System.out.println("_________________________________________");

		stmt_h.close();
		return;
	}

	private void showMenuHotel() {

		System.out.println("1: Search Hotel Clients with Prefix of Last name");
		System.out.println("2: Show Client's Hotel Bookings (in_parameter: idClient )");
		System.out.println("3: Show available Rooms for specific period of Date ");
		System.out.println("4: Exit.");

	}

}
