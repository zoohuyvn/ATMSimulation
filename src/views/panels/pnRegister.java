/**
 * 
 */
package views.panels;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import ATMSimulation.Main;
import bases.variables;
import daos.UserDao;
import models.User;
import utils.toast;
import utils.validator;
import views.authenView;
import views.mainView;

/**
 * @author zoohuy
 * 19 thg 5, 2024
 */
public class pnRegister extends JPanel {
	JPanel pnNorth = new JPanel();
	JPanel pnSouth = new JPanel();
	JPanel pnWest = new JPanel();
	JPanel pnEast = new JPanel();
	JPanel pnMain = new JPanel();
	JLabel usernameLB, passwordLB, nameLB, rpPasswordLB, pinLB;
	public static JLabel twoFALB = new JLabel();
	public static JTextField usernameTF, fullnameTF, pinTF;
	public static JPasswordField passwordTF, rpPasswordTF;
	public static JButton registerBT, loginBT;
	
	public pnRegister() {
		this.setLayout(new BorderLayout());
	    pnNorth.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
	    pnSouth.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
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
	    pnMain.setLayout(new GridLayout(13, 1));
	    usernameLB = new JLabel("Username");
	    usernameTF = new JTextField();
	    usernameTF.setMargin(new Insets(4, 8, 4, 8));
	    passwordLB = new JLabel("Password");
	    passwordTF = new JPasswordField();
	    passwordTF.setMargin(new Insets(4, 8, 4, 8));
	    rpPasswordLB = new JLabel("Retype password");
	    rpPasswordTF = new JPasswordField();
	    rpPasswordTF.setMargin(new Insets(4, 8, 4, 8));
	    nameLB = new JLabel("Fullname");
	    fullnameTF = new JTextField();
	    fullnameTF.setMargin(new Insets(4, 8, 4, 8));
	    pinLB = new JLabel("PIN");
	    pinTF = new JTextField();
	    pinTF.setMargin(new Insets(4, 8, 4, 8));
	    registerBT = new JButton("REGISTER");
	    registerBT.setForeground(Color.white);
		registerBT.setBackground(Color.decode(variables.primaryColor));
		registerBT.setOpaque(true);
		registerBT.setBorderPainted(false);
	    registerBT.setMargin(new Insets(4, 8, 4, 8));
	    loginBT = new JButton("Already have account? Login now.");
	    loginBT.setBackground(Color.decode(variables.whiteBg));
	    loginBT.setOpaque(true);
	    loginBT.setBorderPainted(false);
	    loginBT.setMargin(new Insets(4, 8, 4, 8));
	    pnMain.setBackground(Color.decode(variables.whiteBg));
	    pnMain.add(usernameLB);
	    pnMain.add(usernameTF);
	    pnMain.add(passwordLB);
	    pnMain.add(passwordTF);
	    pnMain.add(rpPasswordLB);
	    pnMain.add(rpPasswordTF);
	    pnMain.add(nameLB);
	    pnMain.add(fullnameTF);
	    pnMain.add(pinLB);
	    pnMain.add(pinTF);
	    pnMain.add(new JLabel());
	    pnMain.add(registerBT);
	    pnMain.add(loginBT);
	    this.add(pnMain, BorderLayout.CENTER);
	}
	
	public static void show2FAQRCode(String imgSrc) {
		JFrame twoFAQRCode = new JFrame();
		twoFAQRCode.setTitle("2FA QR code");
		try {
			twoFALB.setIcon(new ImageIcon(ImageIO.read(new URL(imgSrc))));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		twoFAQRCode.add(twoFALB);
		twoFAQRCode.setSize(266, 280);
		twoFAQRCode.setLocationRelativeTo(Main.getMainViewInstance());
		twoFAQRCode.setVisible(true);
	}
	
	public int registerHandle() {
		int rowUpdate = 0;
		String imgSrc = "";
		boolean isPasswordMatch = passwordTF.getText().trim().equals(rpPasswordTF.getText().trim());
		if (!validator.validateBlankLength(usernameTF)) toast.showMsg("Warning", "Username can't be blank and min 6 characters", "warning");
		else if (!validator.validateBlankLength(passwordTF)) toast.showMsg("Warning", "Password can't be blank and min 6 characters", "warning");
		else if (!validator.validateBlankLength(rpPasswordTF)) toast.showMsg("Warning", "Password repeat can't be blank and min 6 characters", "warning");
		else if (!isPasswordMatch) toast.showMsg("Warning", "Password not match", "warning");
		else if (!validator.validateBlank(fullnameTF)) toast.showMsg("Warning", "Fullname can't be blank", "warning");
		else if (!validator.validatePin(pinTF)) toast.showMsg("Warning", "PIN can't be blank and must be 6 number", "warning");
		else {
			try (Socket con = new Socket("localhost", Main.PORT);
				ObjectInputStream inObj = new ObjectInputStream(con.getInputStream());
				DataOutputStream outStr = new DataOutputStream(con.getOutputStream());
			) {
				String request = "register" + "|" +
				usernameTF.getText().trim() + "|" +
				passwordTF.getText().trim() + "|" +
				fullnameTF.getText().trim() + "|" +
				pinTF.getText().trim();
				outStr.writeUTF(request);
				outStr.flush();
				rowUpdate = inObj.readInt();
				imgSrc = inObj.readUTF();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (rowUpdate == 1) show2FAQRCode(imgSrc);
			else toast.showMsg("Error", "Can't register, have an error", "error");
		}
		return rowUpdate;
	}
}