/**
 * 
 */
package views;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import models.User;

/**
 * @author zoohuy
 * 19 thg 5, 2024
 */
public class mainView extends JPanel {
	public User user;
	authenView authenView;
	uiView uiView;
	
	public mainView() {
		this.setLayout(new BorderLayout());
		user = null;
		authenView = new authenView();
		uiView = new uiView();
		updateView();
	}
	
	public void updateView() {
        this.removeAll();
        if (user == null)
        this.add(authenView);
        else this.add(uiView);
        this.revalidate();
        this.repaint();
    }
	
	public void handleLoginSuccess(User loginUser) {
		user = loginUser;
		try { uiView.pnUITop.start(); } catch (Exception e) { e.printStackTrace(); }
		updateView();
	}
	
	public void handleLogout() {
		user = null;
		for (JFrame popup : uiView.pnUIBottom.openPopups) {
	        popup.dispose();
	    }
	    uiView.pnUIBottom.openPopups.clear();
		updateView();
	}
}