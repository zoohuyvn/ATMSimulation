/**
 * 
 */
package daos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import connection.dbConnector;
import utils.timestamp;

/**
 * @author zoohuy
 * 26 thg 5, 2024
 */
public class StatisticalDao {
	public static StatisticalDao getInstance() {
		return new StatisticalDao();
	}
	
	public ArrayList<double[]> readCount(String usernameInput) {
	    Map<String, double[]> result = new HashMap<>();
	    result.put("receive", new double[] {0, 0});
	    result.put("recharge", new double[] {0, 0});
	    result.put("transfer", new double[] {0, 0});

	    try (Connection con = dbConnector.getConnection();
	         PreparedStatement pst = con.prepareStatement(
	             "SELECT type, COUNT(type) 'count', SUM(amount) 'sum' " +
	             "FROM transactionhistories " +
	             "WHERE username=? AND type IN ('receive', 'recharge', 'transfer') " +
	             "GROUP BY type ORDER BY type"
	         )) {

	        pst.setString(1, usernameInput);
	        ResultSet rs = pst.executeQuery();

	        while (rs.next()) {
	            String type = rs.getString("type");
	            result.get(type)[0] = rs.getDouble("count");
	            result.get(type)[1] = rs.getDouble("sum");
	        }
	        System.out.println("[Query] ->: " + pst);

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    ArrayList<double[]> resultList = new ArrayList<>();
	    resultList.add(result.get("receive"));
	    resultList.add(result.get("recharge"));
	    resultList.add(result.get("transfer"));
	    return resultList;
	}


	public ArrayList<String[]> readChartData(String usernameInput) {
		ArrayList<String[]> result = new ArrayList<String[]>();
		for (int i = 0; i < 3; ++i) {
			result.add(new String[] { timestamp.getDayAfter(i), "0", "0", "0" });
		}
		try {
			Connection con = dbConnector.getConnection();
			String sql = "SELECT DATE_FORMAT(STR_TO_DATE(SUBSTRING_INDEX(`time`, ' - ', 1), '%d/%m/%Y'), '%d/%m') 'monthtime', " +
						 "SUM(CASE WHEN type = 'recharge' THEN amount ELSE 0 END) 'recharge', " +
						 "SUM(CASE WHEN type = 'transfer' THEN amount ELSE 0 END) 'transfer', " +
						 "SUM(CASE WHEN type = 'receive' THEN amount ELSE 0 END) 'receive' " +
						 "FROM transactionhistories WHERE STR_TO_DATE(SUBSTRING_INDEX(`time`, ' - ', 1), '%d/%m/%Y') " +
						 "IN (SELECT DISTINCT STR_TO_DATE(SUBSTRING_INDEX(`time`, ' - ', 1), '%d/%m/%Y') 'day' " +
						 "FROM transactionhistories WHERE `time` LIKE '%" + timestamp.getMonthTime() + "%' ORDER BY day DESC) AND username=? " +
						 "GROUP BY monthtime ORDER BY monthtime DESC LIMIT 3";
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setString(1, usernameInput);
			ResultSet rs = pst.executeQuery();
			int index = 0;
			while (rs.next()) {
				result.get(index)[0] = rs.getString("monthtime");
                result.get(index)[1] = rs.getString("recharge");
                result.get(index)[2] = rs.getString("transfer");
                result.get(index)[3] = rs.getString("receive");
                ++index;
			}
			System.out.println("[Query] ->: " + sql);
			dbConnector.closeConnection(con);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}