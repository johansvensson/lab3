package dbtLab3;

import java.sql.*;
import java.util.ArrayList;

/**
 * Database is a class that specifies the interface to the movie database. Uses
 * JDBC and the MySQL Connector/J driver.
 */
public class Database {
	/**
	 * The database connection.
	 */
	private Connection conn;

	/**
	 * Create the database interface object. Connection to the database is
	 * performed later.
	 */
	public Database() {
		conn = null;
	}

	/**
	 * Open a connection to the database, using the specified user name and
	 * password.
	 * 
	 * @param userName
	 *            The user name.
	 * @param password
	 *            The user's password.
	 * @return true if the connection succeeded, false if the supplied user name
	 *         and password were not recognized. Returns false also if the JDBC
	 *         driver isn't found.
	 */
	public boolean openConnection(String userName, String password) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(
					"jdbc:mysql://puccini.cs.lth.se/" + userName, userName,
					password);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Close the connection to the database.
	 */
	public void closeConnection() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
		}
		conn = null;
	}

	/**
	 * Check if the connection to the database has been established
	 * 
	 * @return true if the connection has been established
	 */
	public boolean isConnected() {
		return conn != null;
	}

	public void loginUser(String userId) {

		PreparedStatement ps = null;

		try {
			String sql = "select username,name from users where username = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, userId);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				CurrentUser.instance().loginAs(rs.getString("username"));
			}
			System.out
					.println("User "
							+ CurrentUser.instance().getCurrentUserId()
							+ " logged in.");
		} catch (SQLException e1) {
			System.out.println("An error has accured");
			e1.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
// GAR ALDRIG IN PA DENNA I TRY METODEN... FAR NULLPOINTEREXCEPTION
	public int getnbrOfSeats(String theaterName) {
		PreparedStatement ps = null;

		try {
			
			String sql = "Select nbrofseats from theater where theatername = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, theaterName);
			ResultSet rs = ps.executeQuery();
			
			int nbrofseats = 0;
			
			if (rs.next()) {
				 nbrofseats = rs.getInt("nbrofseats");
			}
			return nbrofseats;

		} catch (SQLException e1) {
			e1.printStackTrace();
			return (Integer) null;
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
	
	public Performance getDetails(String movieName, String date) {
		
		PreparedStatement ps = null;
		Performance performance = null;
		
		try {
			String sql = "select moviename, datum, theatername from performance where moviename = ? and datum = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, movieName);
			ps.setString(2, date);
			ResultSet rs = ps.executeQuery();
			
			
			if (rs.next()) {
				String theaterName = rs.getString("theaterName");
				 performance = new Performance (rs.getString("datum"), rs.getString("movieName"), theaterName, getnbrOfSeats(theaterName));
				System.out.println("SUCCESSSS");
			}
			return performance;

		} catch (SQLException e1) {
			e1.printStackTrace();
			return null;
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public ArrayList<String> getMovies() {
		PreparedStatement ps = null;

		try {
			String sql = "Select moviename from performance group by moviename";
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			ArrayList<String> result = new ArrayList<String>();
			while (rs.next()) {
				result.add(rs.getString("movieName"));

				System.out.println(rs.getString("movieName"));
			}
			return result;

		} catch (SQLException e1) {
			e1.printStackTrace();
			return null;
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public ArrayList<String> getDates(String moviename) {
		PreparedStatement ps = null;
		System.out.println("Selected movie is: " + moviename);
		try {
			String sql = "Select datum from performance where movieName = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, moviename);
			ResultSet rs = ps.executeQuery();
			ArrayList<String> result = new ArrayList<String>();
			while (rs.next()) {
				Date datum = rs.getDate("datum");
				result.add(datum.toString());
				System.out.println(rs.getString("datum"));

			}
			return result;

		} catch (SQLException e1) {
			e1.printStackTrace();
			return null;
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public int getNbrOfTickets(Performance perf) {
		PreparedStatement ps = null;
	int booked = 0;
		try {
			String sql = "Select count(*) as antal from ticket where movieName = ? and datum = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, perf.getMovieName());
			ps.setString(2, perf.getDate());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				
				booked = rs.getInt("antal");

			}
			return booked;
		} catch (SQLException e1) {
			e1.printStackTrace();
			return (Integer) null;
		} finally {
			
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	
	}
}
