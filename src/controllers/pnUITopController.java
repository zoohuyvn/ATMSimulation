/**
 * 
 */
package controllers;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import ATMSimulation.Main;
import views.panels.pnUITop;

/**
 * @author zoohuy
 * 21 thg 5, 2024
 */
public class pnUITopController implements ActionListener {
	private pnUITop pnUITopV;

	public pnUITopController(pnUITop pnUITopV) {
		this.pnUITopV = pnUITopV;
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Exit")) {
			Main.getMainViewInstance().handleLogout();
		} else if (e.getActionCommand().contains("$")) {
			if (pnUITopV.isEyeHide == false) pnUITopV.isEyeHide = true;
			else pnUITopV.isEyeHide = false;
		}
	}
}