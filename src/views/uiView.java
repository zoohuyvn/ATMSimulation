/**
 * 
 */
package views;
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import ATMSimulation.Main;
import bases.variables;
import server.server;
import views.panels.pnUITop;
import views.panels.pnUIBottom;

/**
 * @author zoohuy
 * 19 thg 5, 2024
 */
public class uiView extends JPanel {
	public pnUITop pnUITop;
	pnUIBottom pnUIBottom;
	
	public uiView() {
		this.setLayout(new BorderLayout(0, 20));
		pnUITop = new pnUITop();
		pnUIBottom = new pnUIBottom();
		this.setBackground(Color.decode(variables.whiteBg));
		this.add(pnUITop, BorderLayout.NORTH);
		this.add(pnUIBottom, BorderLayout.CENTER);
	}
}