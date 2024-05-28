/**
 * 
 */
package views;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JPanel;
import bases.variables;
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