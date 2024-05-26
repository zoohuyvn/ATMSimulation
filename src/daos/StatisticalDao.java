/**
 * 
 */
package daos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import connection.dbConnector;

/**
 * @author zoohuy
 * 26 thg 5, 2024
 */
public class StatisticalDao {
	public static StatisticalDao getInstance() {
		return new StatisticalDao();
	}
	
	public ArrayList<double[]> readCount(String usernameInput) {
		ArrayList<double[]> result = new ArrayList<double[]>();
		try {
			Connection con = dbConnector.getConnection();
			String sql = "SELECT COUNT(type) 'count', SUM(amount) 'sum' FROM transactionhistories " +
						 "WHERE username=? AND type IN ('receive', 'recharge', 'transfer') " +
						 "GROUP BY type ORDER BY type";
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setString(1, usernameInput);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				double[] arr = { 0, 0 };
				arr[0] = rs.getDouble("count");
				arr[1] = rs.getDouble("sum");
				result.add(arr);
			}
			for (int i = 3; i > result.size(); --i) {
				double[] arr = { 0, 0 };
				result.add(arr);
			}
			System.out.println("[Query] ->: " + sql);
			dbConnector.closeConnection(con);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public ArrayList<String[]> readChartData(String usernameInput) {
		ArrayList<String[]> result = new ArrayList<String[]>();
		try {
			Connection con = dbConnector.getConnection();
			String sql = "SELECT DATE_FORMAT(STR_TO_DATE(SUBSTRING_INDEX(`time`, ' - ', 1), '%d/%m/%Y'), '%d/%m') 'monthtime', " +
						 "SUM(CASE WHEN type = 'recharge' THEN amount ELSE 0 END) 'recharge', " +
						 "SUM(CASE WHEN type = 'transfer' THEN amount ELSE 0 END) 'transfer', " +
						 "SUM(CASE WHEN type = 'receive' THEN amount ELSE 0 END) 'receive' " +
						 "FROM transactionhistories WHERE STR_TO_DATE(SUBSTRING_INDEX(`time`, ' - ', 1), '%d/%m/%Y') " +
						 "IN (SELECT DISTINCT STR_TO_DATE(SUBSTRING_INDEX(`time`, ' - ', 1), '%d/%m/%Y') 'day' " +
						 "FROM transactionhistories WHERE `time` LIKE '%05/2024%' ORDER BY day DESC) AND username=? " +
						 "GROUP BY monthtime ORDER BY monthtime DESC LIMIT 3";
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setString(1, usernameInput);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				String[] arr = { "nodata", "0", "0", "0" };
				arr[0] = rs.getString("monthtime");
				arr[1] = rs.getString("recharge");
				arr[2] = rs.getString("transfer");
				arr[3] = rs.getString("receive");
				result.add(arr);
			}
			for (int i = 3; i > result.size(); --i) {
				String[] arr = { "nodata", "0", "0", "0" };
				result.add(arr);
			}
			System.out.println("[Query] ->: " + sql);
			dbConnector.closeConnection(con);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}