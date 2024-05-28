/**
 * 
 */
package ATMSimulation;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import bases.variables;
import views.mainView;

/**
 * @author zoohuy
 * 18 thg 5, 2024
 */
public class Main extends JFrame {
	static JFrame main = null;
	JPanel pnNorth = new JPanel();
	JPanel pnSouth = new JPanel();
	JPanel pnWest = new JPanel();
	JPanel pnEast = new JPanel();
	static mainView mainViewInstance;
	public static int PORT = 9999;
	
	public Main() {
		super("ZBank ATM Simulation");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
	    catch (Exception e) { e.printStackTrace(); }
	    UIDefaults defaults = UIManager.getLookAndFeelDefaults();
	    Font font14 = new Font("Roboto", Font.PLAIN, 14);
	    Font font15 = new Font("Roboto", Font.PLAIN, 15);
	    Font font16 = new Font("Roboto", Font.PLAIN, 16);
	    defaults.put("Label.font", font16);
	    defaults.put("TabbedPane.font", font16);
	    defaults.put("TableHeader.font", font15);
	    defaults.put("Table.font", font14);
	    defaults.put("TextField.font", font15);
	    defaults.put("Button.font", font16);
	    defaults.put("ComboBox.font", font16);
	    pnNorth.setBackground(Color.decode(variables.whiteBg));
	    pnSouth.setBackground(Color.decode(variables.whiteBg));
	    pnWest.setBackground(Color.decode(variables.whiteBg));
	    pnEast.setBackground(Color.decode(variables.whiteBg));
	    pnNorth.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));
	    pnSouth.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
	    pnWest.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 12));
	    pnEast.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 0));
	    getContentPane().add(pnNorth, BorderLayout.NORTH);
	    getContentPane().add(pnSouth, BorderLayout.SOUTH);
	    getContentPane().add(pnWest, BorderLayout.WEST);
	    getContentPane().add(pnEast, BorderLayout.EAST);
	    mainViewInstance = new mainView();
	    getContentPane().add(mainViewInstance, BorderLayout.CENTER);
		this.setIconImage(new ImageIcon(Main.class.getResource("/assets/icon.png")).getImage());
		this.setSize(800, 600);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	public static mainView getMainViewInstance() {
		return mainViewInstance;
	}
	
	public static JFrame getMain() {
		return main;
	}
	
	public static void main(String[] args) throws Exception {
		JFrame x = new Main();
		main = x;
	}
}