/**
 * 
 */
package views.panels;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import ATMSimulation.Main;
import bases.variables;
import controllers.pnUITopController;
import daos.UserDao;
import models.User;
import views.mainView;

/**
 * @author zoohuy
 * 20 thg 5, 2024
 */
public class pnUITop extends JPanel {
	JPanel pnLeft = new JPanel();
	JPanel pnRight = new JPanel();
	JPanel pnRightInfoTop = new JPanel();
	
	JLabel logoLB = new JLabel("<html><p style='margin-bottom: 200px;'></p></html>"),
			welcomeLB = new JLabel(), userLB = new JLabel(), subBalanceLB;
	JButton logoutBT = new JButton("Exit"), balanceBT = new JButton();
	String username, fullname, time = "";
	public boolean isEyeHide = false;
	
	public pnUITop() {
		this.setLayout(new BorderLayout());
		pnLeft.setLayout(new GridLayout(2, 1, 0, 10));
		pnRight.setLayout(new GridLayout(3, 1));
		this.setBackground(Color.decode(variables.whiteBg));
		pnLeft.setBackground(Color.decode(variables.whiteBg));
		pnRight.setBackground(Color.decode(variables.whiteBg));
		pnRightInfoTop.setBackground(Color.decode(variables.whiteBg));
		balanceBT.setBackground(Color.decode(variables.whiteBg));
		ActionListener acl = new pnUITopController(this);
		logoutBT.addActionListener(acl);
		balanceBT.addActionListener(acl);
		logoLB.setIcon(new ImageIcon(Main.class.getResource("/assets/logo.png")));
		logoutBT.setIcon(new ImageIcon(Main.class.getResource("/assets/exit.png")));
		logoutBT.setForeground(Color.white);
		logoutBT.setBackground(Color.decode("#ff5757"));
		logoutBT.setOpaque(true);
		logoutBT.setBorderPainted(false);
		logoutBT.setMargin(new Insets(4, 8, 4, 8));
		pnRightInfoTop.add(userLB);
		pnRightInfoTop.add(logoutBT);
		subBalanceLB = new JLabel("Your balance", SwingConstants.RIGHT);
		balanceBT.setOpaque(true);
		balanceBT.setBorderPainted(false);
	    Font fontBoldBig = new Font("Roboto", Font.BOLD, 30);
	    balanceBT.setFont(fontBoldBig);
	    balanceBT.setMargin(new Insets(0, 220, 0, 0));
	    balanceBT.setFocusPainted(false);
		pnLeft.add(logoLB);
		pnLeft.add(welcomeLB);
		pnRight.add(pnRightInfoTop);
		pnRight.add(subBalanceLB);
		pnRight.add(balanceBT);
		this.add(pnLeft, BorderLayout.WEST);
		this.add(pnRight, BorderLayout.EAST);
	}
	
	public void start() throws Exception {
        new Thread(() -> {
        	try (Socket con = new Socket("localhost", Main.PORT);
        		DataInputStream inStr = new DataInputStream(con.getInputStream());
        		ObjectInputStream inObj = new ObjectInputStream(con.getInputStream());
        		DataOutputStream outStr = new DataOutputStream(con.getOutputStream());
        	) {
        		username = Main.getMainViewInstance().user.getUsername();
        		fullname = Main.getMainViewInstance().user.getFullname();
        		String request = "getTimeAndBalance|" + username;
        		outStr.writeUTF(request);
        		outStr.flush();
        		while (Main.getMainViewInstance().user != null) {
        			String[] parts = inObj.readUTF().split("\\|");
        			String datetime = parts[0];
        			String balance = parts[1];
        			int hour = Integer.parseInt(datetime.substring(0, 2));
        			if (hour >= 1 && hour <= 10) time = "morning";
        			else if (hour <= 12) time = "noon";
        			else if (hour <= 18) time = "afternoon";
        			else if (hour <= 21) time = "evening";
        			else time = "night";
        			SwingUtilities.invokeLater(() -> {
        				userLB.setText(username + "   |   " + datetime + "   |   ");
        				welcomeLB.setText("<html><b style='font-size: 16px;'>Good " + time + " " + fullname + "</b>" +
        				"<p style='margin-top: 6px;'>How can we help you today?</p></html>");
        				if (isEyeHide) {
        					balanceBT.setIcon(new ImageIcon(Main.class.getResource("/assets/eye_hide.png")));
        					balanceBT.setText(" *** $");
        				} else {
        					balanceBT.setIcon(new ImageIcon(Main.class.getResource("/assets/eye_show.png")));
            				balanceBT.setText(balance);
        				}
        			});
        		}
        	} catch (IOException e) {
        		System.err.println("Connection reset: " + e.getMessage());
        	}
        }).start();
    }
}