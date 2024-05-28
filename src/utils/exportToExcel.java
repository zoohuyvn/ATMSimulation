package utils;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import ATMSimulation.Main;
import models.TransactionHistory;

/**
 * @author zoohuy
 * 28 thg 12, 2023
 */

public class exportToExcel {
	public static String path = "";
	static JFileChooser chooser = new JFileChooser();
	public static String firstFileName = Main.getMainViewInstance().user.getUsername() + "-transaction-histories";
	static String filterFields[] = { "No filter", "Code", "Type", "Sender", "Amount", "Time" };
	
	public static boolean export(int searchFilterE, String searchDataE, ArrayList<TransactionHistory> filteredListForExport) {
		File targetFile = null;
	    if (targetFile != null) chooser.setSelectedFile(targetFile);
	    else chooser.setSelectedFile(new File(firstFileName + ".xls"));
	    int option = chooser.showSaveDialog(null);
	    if (option == JFileChooser.APPROVE_OPTION) {
	    	targetFile = chooser.getSelectedFile();
	    }
	    if (targetFile != null && targetFile.getAbsolutePath().endsWith(".xls")) {
		    path = targetFile.toString();
	    } else if (option != JFileChooser.CANCEL_OPTION) {
	    	JOptionPane.showMessageDialog(chooser, "Only allow .xls file", "Warning", JOptionPane.WARNING_MESSAGE);
	    }
		if (!path.equals("")) {
			try {
				HSSFWorkbook workbook = new HSSFWorkbook();
				// Sheet name
				HSSFSheet sheet = workbook.createSheet("Transaction histories");
				// Row head
				HSSFRow rowfilter = sheet.createRow((short) 0);
				rowfilter.createCell(0).setCellValue("Filter: " + filterFields[searchFilterE]);
				rowfilter.createCell(1).setCellValue("Search: \"" + searchDataE + "\"");
				HSSFRow rowhead = sheet.createRow((short) 1);
				rowhead.createCell(0).setCellValue("Code");
				rowhead.createCell(1).setCellValue("Type");
				rowhead.createCell(2).setCellValue("Sender");
				rowhead.createCell(3).setCellValue("Amount");
				rowhead.createCell(4).setCellValue("Time");
				// Row data
				int count = filteredListForExport.size();
				for (int i = 0; i < count; ++i) {
					HSSFRow rowData = sheet.createRow((short) i + 2);
					String notConvertType = filteredListForExport.get(i).getType().toString();
			    	String firstUpperType = notConvertType.substring(0, 1).toUpperCase();
			    	String resultType = firstUpperType + notConvertType.substring(1);
					rowData.createCell(0).setCellValue(filteredListForExport.get(i).getId());
					rowData.createCell(1).setCellValue(resultType);
					rowData.createCell(2).setCellValue(filteredListForExport.get(i).getFrom());
					rowData.createCell(3).setCellValue(filteredListForExport.get(i).getAmount());
					rowData.createCell(4).setCellValue(filteredListForExport.get(i).getTime());
				}
				FileOutputStream fileOut = new FileOutputStream(path);
				workbook.write(fileOut);
				fileOut.close();
				workbook.close();
				if (fileOut != null) return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}