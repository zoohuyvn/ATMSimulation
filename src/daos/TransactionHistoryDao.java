/**
 * 
 */
package daos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import connection.dbConnector;
import models.TransactionHistory;

/**
 * @author zoohuy
 * 18 thg 5, 2024
 */
public class TransactionHistoryDao {
	public static TransactionHistoryDao getInstance() {
		return new TransactionHistoryDao();
	}
	
	public int create(TransactionHistory th) {
		int result = 0;
		try {
			Connection con = dbConnector.getConnection();
			String sql = "INSERT INTO transactionhistories (id, username, type, `from`, amount, `time`) VALUES (?, ?, ?, ?, ?, ?)";
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, th.getId());
			pst.setString(2, th.getUsername());
			pst.setString(3, th.getType());
			pst.setString(4, th.getFrom());
			pst.setDouble(5, th.getAmount());
			pst.setString(6, th.getTime());
			result = pst.executeUpdate();
			System.out.println("[Query] ->:\n- " + sql + "\n- " + result + " row updated.");
			dbConnector.closeConnection(con);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public int create(TransactionHistory th, Connection con) {
		int result = 0;
		String sql = "INSERT INTO transactionhistories (id, username, type, `from`, amount, `time`) VALUES (?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, th.getId());
			pst.setString(2, th.getUsername());
			pst.setString(3, th.getType());
			pst.setString(4, th.getFrom());
			pst.setDouble(5, th.getAmount());
			pst.setString(6, th.getTime());
			result = pst.executeUpdate();
			System.out.println("[Query] ->:\n- " + sql + "\n- " + result + " row updated.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public TransactionHistory read(TransactionHistory th) {
		TransactionHistory result = null;
		try {
			Connection con = dbConnector.getConnection();
			String sql = "SELECT * FROM transactionhistories WHERE id=?";
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, th.getId());
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String username = rs.getString("username");
				String type = rs.getString("type");
				String from = rs.getString("from");
				Double amount = rs.getDouble("amount");
				String time = rs.getString("time");
				result = new TransactionHistory(id, username, type, from, amount, time);
			}
			System.out.println("[Query] ->: " + sql);
			dbConnector.closeConnection(con);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public ArrayList<TransactionHistory> readByUsername(String usernameInput) {
		ArrayList<TransactionHistory> result = new ArrayList<TransactionHistory>();
		try {
			Connection con = dbConnector.getConnection();
			String sql = "SELECT * FROM transactionhistories WHERE username=? ORDER BY `time` DESC";
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setString(1, usernameInput);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String username = rs.getString("username");
				String type = rs.getString("type");
				String from = rs.getString("from");
				Double amount = rs.getDouble("amount");
				String time = rs.getString("time");
				result.add(new TransactionHistory(id, username, type, from, amount, time));
			}
			System.out.println("[Query] ->: " + sql);
			dbConnector.closeConnection(con);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public ArrayList<TransactionHistory> search(String usernameInput, int searchFilterIndex, String searchData) {
		ArrayList<TransactionHistory> result = new ArrayList<TransactionHistory>();
		try {
			Connection con = dbConnector.getConnection();
			String sql = "SELECT * FROM transactionhistories WHERE username=? ";
			String filterFields[] = { "No filter", "id", "type", "`from`", "amount", "`time`" };
			if (searchData.isEmpty()) {
				// Not do anything
			} else if (searchFilterIndex == 0) {
				sql += "and (id LIKE ? or type LIKE ? or `from` LIKE ? OR amount LIKE ? OR `time` LIKE ?) ";
			} else if (searchFilterIndex != 0) {
				sql += "and " + filterFields[searchFilterIndex] + " LIKE ? ";
			}
			sql += "ORDER BY `time` DESC";
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setString(1, usernameInput);
			if (searchData.isEmpty()) {
				// Not do anything
			} else if (searchFilterIndex == 0) {
				for (int i = 2; i <= 6; ++i) {
					pst.setString(i, "%" + searchData + "%");
				}
			} else if (searchFilterIndex != 0) {
				pst.setString(2, "%" + searchData + "%");
			}
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String username = rs.getString("username");
				String type = rs.getString("type");
				String from = rs.getString("from");
				Double amount = rs.getDouble("amount");
				String time = rs.getString("time");
				result.add(new TransactionHistory(id, username, type, from, amount, time));
			}
			System.out.println("[Query] ->: " + sql);
			dbConnector.closeConnection(con);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}