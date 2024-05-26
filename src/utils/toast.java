package utils;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import ATMSimulation.Main;
import views.mainView;

/**
 * @author zoohuy
 * 28 thg 12, 2023
 */

public class toast {
	private static Runnable runnable = (Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.exclamation");
    
	public static void showMsg(String title, String content, String type) {
		switch (type) {
			case "success":
			case "info":
				JOptionPane.showMessageDialog(Main.getMainViewInstance(), content, title, JOptionPane.INFORMATION_MESSAGE);
				break;
			case "warning":
			    if (runnable != null) runnable.run();
				JOptionPane.showMessageDialog(Main.getMainViewInstance(), content, title, JOptionPane.WARNING_MESSAGE);
				break;
			case "error":
			    if (runnable != null) runnable.run();
				JOptionPane.showMessageDialog(Main.getMainViewInstance(), content, title, JOptionPane.ERROR_MESSAGE);
				break;
			default:
				break;
		}
	}
	
	public static boolean showCfm(String title, String content) {
		if (runnable != null) runnable.run();
		int result = JOptionPane.showConfirmDialog(Main.getMainViewInstance(), content, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (result == JOptionPane.YES_OPTION) {
			return true;
		} else {
			return false;
		}
	}
}