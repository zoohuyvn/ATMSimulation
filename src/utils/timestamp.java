/**
 * 
 */
package utils;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author zoohuy
 * 19 thg 5, 2024
 */
public class timestamp {
	public static String getTimeEnglish() {
		long millis = System.currentTimeMillis();
		String time = new SimpleDateFormat("HH:mm:ss - EE, dd", Locale.ENGLISH).format(new Date(millis));
		return time;
	}
	
	public static String getTime() {
		long millis = System.currentTimeMillis();
		String time = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(new Date(millis));
		return time;
	}
}