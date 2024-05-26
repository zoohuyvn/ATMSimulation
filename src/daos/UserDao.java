/**
 * 
 */
package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.mindrot.jbcrypt.BCrypt;
import connection.dbConnector;
import models.User;

/**
 * @author zoohuy
 * 18 thg 5, 2024
 */
public class UserDao {
	public static UserDao getInstance() {
		return new UserDao();
	}
	
	public int create(User user) {
		int result = 0;
		try {
			Connection con = dbConnector.getConnection();
			String sql = "INSERT INTO users (username, password, fullname, secretCode, pin, balance) VALUES (?, ?, ?, ?, ?, ?)";
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setString(1, user.getUsername());
			String passwordHash = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(6));
			pst.setString(2, passwordHash);
			pst.setString(3, user.getFullname());
			pst.setString(4, user.getSecretCode());
			pst.setString(5, user.getPin());
			pst.setDouble(6, user.getBalance());
			result = pst.executeUpdate();
			System.out.println("[Query] -> :\n- " + sql + "\n- " + result + " row updated.");
			dbConnector.closeConnection(con);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public User read(User user) {
		User result = null;
		try {
			Connection con = dbConnector.getConnection();
			String sql = "SELECT * FROM users WHERE username=?";
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setString(1, user.getUsername());
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				String username = rs.getString("username");
				String password = rs.getString("password");
				String fullname = rs.getString("fullname");
				String secretCode = rs.getString("secretCode");
				String pin = rs.getString("pin");
				Double balance = rs.getDouble("balance");
				result = new User(username, password, fullname, secretCode, pin, balance);
			}
			System.out.println("[Query] -> : " + sql);
			dbConnector.closeConnection(con);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public int update(User user, String type) {
		int result = 0;
		try {
			Connection con = dbConnector.getConnection();
			String sql = "";
			if (type.equals("recharge") || type.equals("transfer") || type.equals("receive")) {
				sql = "UPDATE users SET balance=? WHERE username=?";
			} else if (type.equals("changepin")) {
				sql = "UPDATE users SET pin=? WHERE username=?";
			}
			PreparedStatement pst = con.prepareStatement(sql);
			if (type.equals("recharge") || type.equals("transfer") || type.equals("receive")) {
				pst.setDouble(1, user.getBalance());
			} else if (type.equals("changepin")) {
				pst.setString(1, user.getPin());
			}
			pst.setString(2, user.getUsername());
			result = pst.executeUpdate();
			System.out.println("[Query] -> :\n- " + sql + "\n- " + result + " row updated.");
			dbConnector.closeConnection(con);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}