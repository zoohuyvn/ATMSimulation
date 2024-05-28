/**
 * 
 */
package views;
import java.awt.CardLayout;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import controllers.authenController;
import views.panels.pnLogin;
import views.panels.pnRegister;

/**
 * @author zoohuy
 * 19 thg 5, 2024
 */
public class authenView extends JPanel {
	CardLayout cardLayout;
	
	public authenView() {
		cardLayout = new CardLayout();
		this.setLayout(cardLayout);
		pnRegister pnRegister = new pnRegister();
		pnLogin pnLogin = new pnLogin();
		ActionListener acl = new authenController(this, pnLogin, pnRegister);
		pnRegister.registerBT.addActionListener(acl);
		pnRegister.loginBT.addActionListener(acl);
		pnLogin.loginBT.addActionListener(acl);
		pnLogin.registerBT.addActionListener(acl);
		this.add(pnLogin, "pnLogin");
		this.add(pnRegister, "pnRegister");
	}
	
	public void showPnRegister() {
		cardLayout.show(this, "pnRegister");
	}
	
	public void showPnLogin() {
		cardLayout.show(this, "pnLogin");
	}
}