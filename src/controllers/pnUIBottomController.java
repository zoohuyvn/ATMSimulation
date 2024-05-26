/**
 * 
 */
package controllers;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import views.panels.pnUIBottom;

/**
 * @author zoohuy
 * 22 thg 5, 2024
 */
public class pnUIBottomController implements ActionListener {
	private pnUIBottom pnUIBottomV;

	public pnUIBottomController(pnUIBottom pnUIBottomV) {
		this.pnUIBottomV = pnUIBottomV;
	}
	
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "Recharge":
				pnUIBottomV.rechargeHandle();
				break;
			case "<html><center>Transfer <br> money</center></html>":
				pnUIBottomV.transferHandle();
				break;
			case "<html><center>Transaction <br> history</center></html>":
				pnUIBottomV.historyHandle();
				break;
			case "Change PIN":
				pnUIBottomV.changePinHandle();
				break;
			case "Statistical":
				pnUIBottomV.statisticalHandle();
				break;
			default:
				break;
		}
	}
}