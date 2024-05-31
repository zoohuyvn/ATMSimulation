package utils;
import java.util.regex.*;

import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * @author zoohuy
 * 28 thg 12, 2023
 */

public class validator {
	private static Pattern pattern;
	private static Matcher matcher;
	private static String pinRGX = "^[0-9]{6}$";
	private static String balanceRGX = "^\\d{0,5}(\\.\\d{0,2})?$";

	public static boolean validateBlank(JTextField tf) {
		boolean isEmpty = tf.getText().trim().isEmpty();
		if (isEmpty) return false;
	    return true;
	}
	
	public static boolean validateBlankLength(JTextField tf) {
		int tfLength = tf.getText().trim().length();
		if (tfLength < 6) return false;
	    return true;
	}
	
	public static boolean validateBlankLength(JPasswordField pf) {
		int tfLength = pf.getPassword().length;
		if (tfLength < 6) return false;
	    return true;
	}
	
	public static boolean validatePin(JPasswordField pf) {
		pattern = Pattern.compile(pinRGX);
	    matcher = pattern.matcher(pf.getText());
	    if (!matcher.find()) return false;
	    return true;
	}
	
	public static boolean validateBalance(JTextField tf) {
		pattern = Pattern.compile(balanceRGX);
	    matcher = pattern.matcher(tf.getText());
	    if (!matcher.find()) return false;
	    return true;
	}
}