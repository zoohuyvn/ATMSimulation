/**
 * 
 */
package connection;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author zoohuy
 * 18 thg 5, 2024
 */
public class dbConnector {
	public static Connection getConnection() {
		Connection con = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String DB_URL = "jdbc:mysql://127.0.0.1:3306/atmsimulation";
			String USERNAME = "root";
			String PASSWORD = "";
			con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			System.out.println("[DB] -> Connect successfully");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[DB] -> Connect failure");
		}
		return con;
	}

	public static void closeConnection(Connection con) {
		try {
			if (con != null) con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}