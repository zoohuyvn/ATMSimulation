package utils;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * @author zoohuy
 * 28 thg 12, 2023
 */

public class resizeTableColumnWidth {
	public static void rsz(JTable table) {
	    TableColumnModel columnModel = table.getColumnModel();
	    TableModel tableModel = table.getModel();
	    int total = columnModel.getColumnCount();
	    for (int i = 0; i < total; ++i) {
	    	int temp = 0;
	    	int total2 = tableModel.getRowCount();
	    	for (int j = 0; j < total2; ++j) {
	    		if (tableModel.getValueAt(j, i) != null) {
	    			int temp2 = tableModel.getValueAt(j, i).toString().length() * 2;
	    			if (temp2 > temp) temp = temp2;
	    		}
	    		columnModel.getColumn(i).setPreferredWidth(temp);
	    	}
	    }
	}
}