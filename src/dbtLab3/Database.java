package dbtLab3;

import java.sql.*;
import java.util.ArrayList;

import javax.swing.DefaultListModel;

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
		// Creates a PreparedStatemet from the String
		try {
			String sql = "select username,name from users where username = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, userId);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				CurrentUser.instance().loginAs(rs.getString("username"));
			}
			System.out
					.println("Now is "
							+ CurrentUser.instance().getCurrentUserId()
							+ " logged in!");
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

	/* --- insert own code here --- */
	public Connection getConnection() {
		return conn;
	}

	public ArrayList<String> getMovies() {
		PreparedStatement ps = null;

		try {
			String sql = "Select * from movie";
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

		try {
			String sql = "Select * from performance where movieName = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, moviename);
			ResultSet rs = ps.executeQuery();
			ArrayList<String> result = new ArrayList<String>();
			int counter = 0;
			while (rs.next()) {
				
				result.add(rs.getNString(counter));
				;
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

}
