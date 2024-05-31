/**
 * 
 */
package views.panels;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import ATMSimulation.Main;
import bases.variables;
import models.User;
import utils.toast;
import utils.validator;

/**
 * @author zoohuy
 * 19 thg 5, 2024
 */
public class pnLogin extends JPanel {
	JPanel pnNorth = new JPanel();
	JPanel pnSouth = new JPanel();
	JPanel pnWest = new JPanel();
	JPanel pnEast = new JPanel();
	JPanel pnMain = new JPanel();
	JLabel usernameLB, passwordLB, twoFALB;
	static JTextField usernameTF;
	static JPasswordField passwordTF, twoFATF;
	public static JButton registerBT, loginBT;
	
	public pnLogin() {
		this.setLayout(new BorderLayout());
	    pnNorth.setBorder(BorderFactory.createEmptyBorder(80, 0, 0, 0));
	    pnSouth.setBorder(BorderFactory.createEmptyBorder(0, 0, 80, 0));
	    pnWest.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 150));
	    pnEast.setBorder(BorderFactory.createEmptyBorder(0, 150, 0, 0));
	    pnNorth.setBackground(Color.decode(variables.whiteBg));
	    pnSouth.setBackground(Color.decode(variables.whiteBg));
	    pnWest.setBackground(Color.decode(variables.whiteBg));
	    pnEast.setBackground(Color.decode(variables.whiteBg));
	    this.add(pnNorth, BorderLayout.NORTH);
	    this.add(pnSouth, BorderLayout.SOUTH);
	    this.add(pnWest, BorderLayout.WEST);
	    this.add(pnEast, BorderLayout.EAST);
	    pnMain.setLayout(new GridLayout(9, 1));
	    usernameLB = new JLabel("Username");
	    usernameTF = new JTextField();
	    usernameTF.setMargin(new Insets(4, 8, 4, 8));
	    passwordLB = new JLabel("Password");
	    passwordTF = new JPasswordField();
	    passwordTF.setMargin(new Insets(4, 8, 4, 8));
	    twoFALB = new JLabel("2FA code");
	    twoFATF = new JPasswordField();
	    twoFATF.setMargin(new Insets(4, 8, 4, 8));
	    loginBT = new JButton("LOGIN");
	    loginBT.setForeground(Color.white);
	    loginBT.setBackground(Color.decode(variables.primaryColor));
	    loginBT.setOpaque(true);
	    loginBT.setBorderPainted(false);
	    loginBT.setMargin(new Insets(4, 8, 4, 8));
	    registerBT = new JButton("Not have account? Register now.");
	    registerBT.setBackground(Color.decode(variables.whiteBg));
	    registerBT.setOpaque(true);
	    registerBT.setBorderPainted(false);
	    registerBT.setMargin(new Insets(4, 8, 4, 8));
	    pnMain.setBackground(Color.decode(variables.whiteBg));
	    pnMain.add(usernameLB);
	    pnMain.add(usernameTF);
	    pnMain.add(passwordLB);
	    pnMain.add(passwordTF);
	    pnMain.add(twoFALB);
	    pnMain.add(twoFATF);
	    pnMain.add(new JLabel());
	    pnMain.add(loginBT);
	    pnMain.add(registerBT);
	    this.add(pnMain, BorderLayout.CENTER);
	}
	
	public User loginHandle() {
		User loginUser = null;
		if (!validator.validateBlankLength(usernameTF)) toast.showMsg("Warning", "Username can't be blank and min 6 characters", "warning");
		else if (!validator.validateBlankLength(passwordTF)) toast.showMsg("Warning", "Password can't be blank and min 6 characters", "warning");
		else if (!validator.validatePin(twoFATF)) toast.showMsg("Warning", "2FA code can't be blank and must be 6 number", "warning");
		else {
			User userFound = null;
			boolean validatePassword = false;
			boolean validate2FACode = false;
			try (Socket con = new Socket("localhost", Main.PORT);
				ObjectInputStream inObj = new ObjectInputStream(con.getInputStream());
				DataOutputStream outStr = new DataOutputStream(con.getOutputStream());
			) {
				String request = "login|" + usernameTF.getText().trim() + "|" + passwordTF.getText().trim() + "|" + twoFATF.getText().trim();
				outStr.writeUTF(request);
				outStr.flush();
				userFound = (User) inObj.readObject();
				validatePassword = inObj.readBoolean();
				validate2FACode = inObj.readBoolean();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
			if (userFound == null) toast.showMsg("Warning", "Username not found", "warning");
			else if (!validatePassword) toast.showMsg("Warning", "Wrong password", "warning");
			else if (!validate2FACode) toast.showMsg("Warning", "Wrong 2FA", "warning");
			else loginUser = userFound;
		}
		return loginUser;
	}
}