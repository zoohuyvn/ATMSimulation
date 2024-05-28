/**
 * 
 */
package controllers;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import ATMSimulation.Main;
import models.User;
import views.authenView;
import views.panels.pnLogin;
import views.panels.pnRegister;

/**
 * @author zoohuy
 * 18 thg 5, 2024
 */
public class authenController implements ActionListener {
	private authenView authenViewV;
	private pnRegister pnRegisterV;
	private pnLogin pnLoginV;

	public authenController(authenView authenViewV, pnLogin pnLoginV, pnRegister pnRegisterV) {
		this.authenViewV = authenViewV;
		this.pnLoginV = pnLoginV;
		this.pnRegisterV = pnRegisterV;
	}

	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "REGISTER":
				int rowUpdate = pnRegisterV.registerHandle();
				if (rowUpdate == 1) authenViewV.showPnLogin();
				break;
			case "Already have account? Login now.":
				authenViewV.showPnLogin();
				break;
			case "LOGIN":
				User loginUser = pnLoginV.loginHandle();
				if (loginUser != null) Main.getMainViewInstance().handleLoginSuccess(loginUser);
				break;
			case "Not have account? Register now.":
				authenViewV.showPnRegister();
				break;
			default:
				break;
		}
	}
}