package utils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * @author zoohuy
 * 28 thg 12, 2023
 */

public class exportPDF {
	static String fileName = "statistical-report.pdf";
	public static String path = "C:\\Users\\USER\\OneDrive\\Desktop\\" + fileName + ".pdf";
	static JFileChooser chooser = new JFileChooser();

	public static boolean export(ArrayList<double[]> dataDSHB) {
		Document document = new Document();
        try {
        	// File chooser and open document
        	File targetFile = null;
    	    if (targetFile != null) {
    	    	chooser.setSelectedFile(targetFile);
    	    } else {
    	    	chooser.setSelectedFile(new File(fileName));
    	    }
    	    int option = chooser.showSaveDialog(null);
    	    if (option == JFileChooser.APPROVE_OPTION) targetFile = chooser.getSelectedFile();
    	    if (targetFile != null && targetFile.getAbsolutePath().endsWith(".pdf")) path = targetFile.toString();
    	    else if (option != JFileChooser.CANCEL_OPTION) {
    	    	JOptionPane.showMessageDialog(chooser, "Only allow .pdf file", "Message", JOptionPane.WARNING_MESSAGE);
    	    }
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();
            
            // Add data to document
            // Init font
            BaseFont titleFont = null;
            BaseFont contentFont = null;
            String fontPath = "C:\\Users\\USER\\AppData\\Local\\Microsoft\\Windows\\Fonts\\";
			try {
				titleFont = BaseFont.createFont(fontPath + "Roboto-Medium.ttf", BaseFont.WINANSI, true);
				contentFont = BaseFont.createFont(fontPath + "Roboto-Regular.ttf", BaseFont.WINANSI, true);
			} catch (IOException e) {
				e.printStackTrace();
	            return false;
			}
			
			// Title and date
            Paragraph paragraph1 = new Paragraph();
            Phrase phrase1 = new Phrase("ZBank ATM Simulation");
            paragraph1.setAlignment(Element.ALIGN_CENTER);
            Font font = new Font(titleFont, 17, Font.NORMAL);
            paragraph1.setFont(font);
            paragraph1.add(phrase1);
            Paragraph paragraph2 = new Paragraph();
            Phrase phrase2 = new Phrase("Statistical report");
            paragraph2.setAlignment(Element.ALIGN_CENTER);
            paragraph2.setSpacingBefore(8);
            Font font2 = new Font(titleFont, 20, Font.BOLD);
            font2.setColor(BaseColor.BLUE);
            paragraph2.setFont(font2);
            paragraph2.add(phrase2);
            Paragraph paragraph3 = new Paragraph();
            Phrase phrase3 = new Phrase("Time: " + timestamp.getTime());
            paragraph3.setAlignment(Element.ALIGN_CENTER);
            paragraph3.setSpacingBefore(4);
            Font font3 = new Font(titleFont, 14, Font.NORMAL);
            font3.setColor(BaseColor.GRAY);
            paragraph3.setFont(font3);
            paragraph3.add(phrase3);
            
            // Content
            Paragraph paragraph4 = new Paragraph();
            double totalTransaction = 0;
			for (double[] arr : dataDSHB) { totalTransaction += arr[0]; }
            Phrase phrase4 = new Phrase("Statistical:\n"
            							+ "- Total transactions: " +
            							(totalTransaction+"").substring(0, (totalTransaction+"").indexOf(".")) + "\n"
            							+ "- Total recharge: " + dataDSHB.get(1)[1] + " $\n"
            							+ "- Total transfer: " + dataDSHB.get(2)[1] + " $\n"
            							+ "- Total receive: " + dataDSHB.get(0)[1] + " $\n");
            paragraph4.setMultipliedLeading(Float.parseFloat("1.5"));
            paragraph4.setAlignment(Element.ALIGN_LEFT);
            paragraph4.setSpacingBefore(35);
            Font font4 = new Font(contentFont, 16, Font.NORMAL);
            paragraph4.setFont(font4);
            paragraph4.add(phrase4);
            
            Paragraph paragraph5 = new Paragraph();
            Phrase phrase5 = new Phrase("Chart:\n");
            paragraph5.setAlignment(Element.ALIGN_LEFT);
            paragraph5.setSpacingBefore(35);
            paragraph5.setFont(font4);
            paragraph5.add(phrase5);
            
            String path = "C:\\Users\\USER\\eclipse-workspace\\ATMSimulation\\src\\assets\\chart";
            Image image = null;
			try {
				image = Image.getInstance(path + ".png");
			} catch (IOException e) {
				e.printStackTrace();
	            return false;
			}
			image.scaleToFit(500, 400);

            // Add content
            document.add(paragraph1);
            document.add(paragraph2);
            document.add(paragraph3);
            document.add(paragraph4);
            document.add(paragraph5);
            document.add(image);

            // Close document
            document.close();
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
	}
}