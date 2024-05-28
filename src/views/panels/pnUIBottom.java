/**
 * 
 */
package views.panels;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.EventObject;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import ATMSimulation.Main;
import bases.variables;
import controllers.pnUIBottomController;
import models.TransactionHistory;
import models.User;
import utils.exportPDF;
import utils.exportToExcel;
import utils.generateColumnChart;
import utils.resizeTableColumnWidth;
import utils.toast;
import utils.validator;

/**
 * @author zoohuy
 * 20 thg 5, 2024
 */
public class pnUIBottom extends JPanel {
	JPanel pnRight = new JPanel();
	JButton recharge, transfer, history, changePin, statistical, exportBT;
	private ArrayList<TransactionHistory> filteredListForExport;
	private ArrayList<double[]> dataDSHBForExport;
	public ArrayList<JFrame> openPopups = new ArrayList<>();
	
	public pnUIBottom() {
		this.setLayout(new BorderLayout(16, 0));
		pnRight.setLayout(new GridLayout(2, 2, 16, 16));
		this.setBackground(Color.decode(variables.whiteBg));
		pnRight.setBackground(Color.decode(variables.whiteBg));
		recharge = new JButton("Recharge");
		recharge.setPreferredSize(new Dimension(300, 0));
		transfer = new JButton("<html><center>Transfer <br> money</center></html>");
		history = new JButton("<html><center>Transaction <br> history</center></html>");
		changePin = new JButton("Change PIN");
		statistical = new JButton("Statistical");
		recharge.setIcon(new ImageIcon(Main.class.getResource("/assets/recharge.png")));
		transfer.setIcon(new ImageIcon(Main.class.getResource("/assets/transfer.png")));
		history.setIcon(new ImageIcon(Main.class.getResource("/assets/history.png")));
		changePin.setIcon(new ImageIcon(Main.class.getResource("/assets/change_pin.png")));
		statistical.setIcon(new ImageIcon(Main.class.getResource("/assets/statistical.png")));
		recharge.setBackground(Color.decode(variables.whiteBg));
		transfer.setBackground(Color.decode(variables.whiteBg));
		history.setBackground(Color.decode(variables.whiteBg));
		changePin.setBackground(Color.decode(variables.whiteBg));
		statistical.setBackground(Color.decode(variables.whiteBg));
		recharge.setFocusable(false);
		transfer.setFocusable(false);
		history.setFocusable(false);
		changePin.setFocusable(false);
		statistical.setFocusable(false);
		recharge.setVerticalTextPosition(JButton.BOTTOM);
		recharge.setHorizontalTextPosition(JButton.CENTER);
		recharge.setIconTextGap(10);
		transfer.setVerticalTextPosition(JButton.BOTTOM);
		transfer.setHorizontalTextPosition(JButton.CENTER);
		transfer.setIconTextGap(10);
		history.setVerticalTextPosition(JButton.BOTTOM);
		history.setHorizontalTextPosition(JButton.CENTER);
		history.setIconTextGap(10);
		changePin.setVerticalTextPosition(JButton.BOTTOM);
		changePin.setHorizontalTextPosition(JButton.CENTER);
		changePin.setIconTextGap(10);
		statistical.setVerticalTextPosition(JButton.BOTTOM);
		statistical.setHorizontalTextPosition(JButton.CENTER);
		statistical.setIconTextGap(10);
	    Font fontBig = new Font("Roboto", Font.PLAIN, 20);
	    recharge.setFont(fontBig);
	    transfer.setFont(fontBig);
	    history.setFont(fontBig);
	    changePin.setFont(fontBig);
	    statistical.setFont(fontBig);
	    ActionListener acl = new pnUIBottomController(this);
	    recharge.addActionListener(acl);
	    transfer.addActionListener(acl);
	    history.addActionListener(acl);
	    changePin.addActionListener(acl);
	    statistical.addActionListener(acl);
		pnRight.add(transfer);
		pnRight.add(history);
		pnRight.add(changePin);
		pnRight.add(statistical);
		this.add(recharge, BorderLayout.WEST);
		this.add(pnRight, BorderLayout.CENTER);
	}
	
	static JFrame showPopupJFrame(String title, int width, int height, JPanel pnMain) {
		JFrame popupJf = new JFrame();
		popupJf.setTitle(title);
		JPanel pnWrap = new JPanel();
		pnWrap.setLayout(new BorderLayout());
		pnWrap.setBackground(Color.decode(variables.whiteBg));
		pnMain.setBackground(Color.decode(variables.whiteBg));
		JPanel pnNorth = new JPanel();
		JPanel pnSouth = new JPanel();
		JPanel pnWest = new JPanel();
		JPanel pnEast = new JPanel();
		pnNorth.setBackground(Color.decode(variables.whiteBg));
	    pnSouth.setBackground(Color.decode(variables.whiteBg));
	    pnWest.setBackground(Color.decode(variables.whiteBg));
	    pnEast.setBackground(Color.decode(variables.whiteBg));
	    pnNorth.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
	    pnSouth.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
	    pnWest.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
	    pnEast.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
	    pnWrap.add(pnNorth, BorderLayout.NORTH);
	    pnWrap.add(pnSouth, BorderLayout.SOUTH);
	    pnWrap.add(pnWest, BorderLayout.WEST);
	    pnWrap.add(pnEast, BorderLayout.EAST);
	    pnWrap.add(pnMain, BorderLayout.CENTER);
		popupJf.setContentPane(pnWrap);
		popupJf.setSize(width, height);
		popupJf.setLocationRelativeTo(Main.getMainViewInstance());
		popupJf.setVisible(true);
		return popupJf;
	}
	
	public void rechargeHandle() {
		JPanel pnMain = new JPanel();
		pnMain.setLayout(new GridLayout(5, 1, 0, 16));
		JLabel amountLB = new JLabel("Amount"), pinLB = new JLabel("PIN");
		JTextField amoutnTF = new JTextField(), pinTF = new JTextField();
		JButton rechargeBT = new JButton("RECHARGE");
		amoutnTF.setMargin(new Insets(4, 8, 4, 8));
		pinTF.setMargin(new Insets(4, 8, 4, 8));
		rechargeBT.setBackground(Color.decode(variables.primaryColorLight));
		rechargeBT.setOpaque(true);
		rechargeBT.setBorderPainted(false);
		rechargeBT.setMargin(new Insets(4, 8, 4, 8));
		pnMain.add(amountLB);
		pnMain.add(amoutnTF);
		pnMain.add(pinLB);
		pnMain.add(pinTF);
		pnMain.add(rechargeBT);
		JFrame popup = showPopupJFrame("Recharge", 400, 300, pnMain);
		openPopups.add(popup);
		rechargeBT.addActionListener(e -> {
			String pin = pinTF.getText().trim();
			if (!validator.validateBlank(amoutnTF) || !validator.validateBalance(amoutnTF)) toast.showMsg("Warning", "Amount can't be blank and must be number", "warning");
			else if (!validator.validatePin(pinTF)) toast.showMsg("Warning", "PIN can't be blank and must be 6 number", "warning");
			else if (!pin.equals(Main.getMainViewInstance().user.getPin())) toast.showMsg("Error", "Wrong PIN", "error");
			else {
				try (Socket con = new Socket("localhost", Main.PORT);
					ObjectInputStream inObj = new ObjectInputStream(con.getInputStream());
		            DataOutputStream outStr = new DataOutputStream(con.getOutputStream());
				) {
					String username = Main.getMainViewInstance().user.getUsername();
					String amount = amoutnTF.getText().trim();
					String request = "recharge|" + username + "|" + amount;
					outStr.writeUTF(request);
					outStr.flush();
					int rowUserUpdate = inObj.readInt();
					int rowThUpdate = inObj.readInt();
					if (rowUserUpdate != 1 || rowThUpdate != 1) toast.showMsg("Error", "Can't recharge, have an error", "error");
					popup.dispose();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
	}
	
	public void transferHandle() {
		JPanel pnMain = new JPanel();
		pnMain.setLayout(new GridLayout(9, 1, 0, 16));
		JLabel receiverUsernameLB = new JLabel("Receiver username"),
			   amountLB = new JLabel("Amount"),
			   pinLB = new JLabel("PIN"),
			   retypePinLB = new JLabel("Retype PIN");
		JTextField receiverUsernameTF = new JTextField(),
				   amoutnTF = new JTextField(),
				   pinTF = new JTextField(),
				   retypePinTF = new JTextField();
		JButton transferBT = new JButton("TRANSFER");
		receiverUsernameTF.setMargin(new Insets(4, 8, 4, 8));
		amoutnTF.setMargin(new Insets(4, 8, 4, 8));
		pinTF.setMargin(new Insets(4, 8, 4, 8));
		retypePinTF.setMargin(new Insets(4, 8, 4, 8));
		transferBT.setBackground(Color.decode(variables.primaryColorLight));
		transferBT.setOpaque(true);
		transferBT.setBorderPainted(false);
		transferBT.setMargin(new Insets(4, 8, 4, 8));
		pnMain.add(receiverUsernameLB);
		pnMain.add(receiverUsernameTF);
		pnMain.add(amountLB);
		pnMain.add(amoutnTF);
		pnMain.add(pinLB);
		pnMain.add(pinTF);
		pnMain.add(retypePinLB);
		pnMain.add(retypePinTF);
		pnMain.add(transferBT);
		JFrame popup = showPopupJFrame("Transfer", 400, 480, pnMain);
		openPopups.add(popup);
		transferBT.addActionListener(e -> {
			String pin = pinTF.getText().trim();
			Double amount = Double.parseDouble(amoutnTF.getText().trim().isEmpty() ? "0" : amoutnTF.getText().trim());
			String transferUsername = Main.getMainViewInstance().user.getUsername();
			String receiverUsername = receiverUsernameTF.getText().trim();
			User transferUser = null, receiverUser = null;
			try (Socket con = new Socket("localhost", Main.PORT);
				ObjectInputStream inObj = new ObjectInputStream(con.getInputStream());
				DataOutputStream outStr = new DataOutputStream(con.getOutputStream());
			) {
				String request = "transferCheckUser|" + transferUsername + "|" + receiverUsername;
				outStr.writeUTF(request);
				outStr.flush();
				transferUser = (User) inObj.readObject();
				receiverUser = (User) inObj.readObject();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			if (!validator.validateBlankLength(receiverUsernameTF)) toast.showMsg("Warning", "Username can't be blank and min 6 characters", "warning");
			else if (!validator.validateBlank(amoutnTF) || !validator.validateBalance(amoutnTF)) toast.showMsg("Warning", "Amount can't be blank and must be number", "warning");
			else if (amount == 0) toast.showMsg("Error", "Amount can't be 0", "error");
			else if (amount > transferUser.getBalance()) toast.showMsg("Error", "Insufficient balance", "error");
			else if (!validator.validatePin(pinTF)) toast.showMsg("Warning", "PIN can't be blank and must be 6 number", "warning");
			else if (!validator.validatePin(retypePinTF)) toast.showMsg("Warning", "PIN retype can't be blank and must be 6 number", "warning");
			else if (!pin.equals(retypePinTF.getText().trim())) toast.showMsg("Warning", "PIN not match", "warning");
			else if (!pin.equals(Main.getMainViewInstance().user.getPin())) toast.showMsg("Error", "Wrong PIN", "error");
			else if (receiverUser == null) toast.showMsg("Error", "Receiver user not found", "error");
			else {
				try (Socket con = new Socket("localhost", Main.PORT);
					ObjectInputStream inObj = new ObjectInputStream(con.getInputStream());
					DataOutputStream outStr = new DataOutputStream(con.getOutputStream());
				) {
					Double currentTransferBalance = transferUser.getBalance();
					Double currentReceiverBalance = receiverUser.getBalance();
					String request = "transfer|" +
									transferUsername + "|" +
									currentTransferBalance + "|" +
									receiverUsername + "|" +
									currentReceiverBalance + "|" +
									amount;
					outStr.writeUTF(request);
					outStr.flush();
					int rowTransferUpdate = inObj.readInt();
					int rowReceiverUpdate = inObj.readInt();
					int rowTransferThUpdate = inObj.readInt();
					int rowReceiverThUpdate = inObj.readInt();
					boolean isError = rowTransferUpdate != 1 || rowReceiverUpdate != 1 || rowTransferThUpdate != 1 || rowReceiverThUpdate != 1;
					if (isError) toast.showMsg("Error", "Can't transfer, have an error", "error");
					popup.dispose();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
	}
	
	public void historyHandle() {
		JPanel pnMain = new JPanel(), pnSub = new JPanel();
		pnMain.setLayout(new BorderLayout(0, 12));
		pnSub.setBackground(Color.decode(variables.whiteBg));
		JLabel filterLB = new JLabel("Filter  ");
		filterLB.setIcon(new ImageIcon(Main.class.getResource("/assets/filter.png")));
		String filterFields[] = { "No filter", "Code", "Type", "Sender", "Amount", "Time" };
		JComboBox<String> filterCBB = new JComboBox<String>(filterFields);
		filterCBB.setFocusable(false);
		JTextField searchTF = new JTextField(15);
		searchTF.setMargin(new Insets(4, 8, 4, 8));
		exportBT = new JButton(" Export to Excel");
		exportBT.setIcon(new ImageIcon(Main.class.getResource("/assets/spreadsheet.png")));
		exportBT.setFocusable(false);
		exportBT.setMargin(new Insets(4, 8, 4, 8));
		exportBT.setBackground(Color.decode(variables.whiteBg));
		pnSub.add(filterLB);
		pnSub.add(filterCBB);
		pnSub.add(searchTF);
		pnSub.add(exportBT);
		String tableLabels[] = {
			"<html><p style='margin-left: 8px;'>Code</p></html>",
			"<html><p style='margin-left: 8px;'>Type</p></html>",
			"<html><p style='margin-left: 8px;'>Sender</p></html>",
			"<html><p style='margin-left: 8px;'>Amount</p></html>",
			"<html><p style='margin-left: 8px;'>Time</p></html>"
		};
		DefaultTableModel dtm = new DefaultTableModel();
		JTable table = new JTable(dtm) {
			public boolean editCellAt(int row, int column, EventObject e) {
	            return false;
	         }
		};
		ArrayList<TransactionHistory> listTh = getNewHistory();
		filteredListForExport = listTh;
		for (String tableLabelText : tableLabels) { dtm.addColumn(tableLabelText); }
		table.setBackground(Color.decode(variables.whiteBg));
		table.setGridColor(Color.decode(variables.lightGray));
	    table.getTableHeader().setOpaque(false);
	    table.getTableHeader().setBackground(Color.decode(variables.primaryColorLight));
	    table.setRowSelectionAllowed(false);
	    table.setRowHeight(28);
	    table.setShowVerticalLines(false);
	    table.setFocusable(false);
	    table.getColumnModel().getColumn(0).setMinWidth(60);
	    table.getColumnModel().getColumn(0).setMaxWidth(60);
	    table.getColumnModel().getColumn(3).setMinWidth(90);
	    table.getColumnModel().getColumn(3).setMaxWidth(90);
	    JScrollPane scpn = new JScrollPane(table);
	    scpn.setBackground(Color.decode(variables.primaryColorLight));
	    scpn.setBorder(BorderFactory.createLineBorder(Color.decode("#f0f0f0"), 10));
	    pnMain.add(pnSub, BorderLayout.NORTH);
	    pnMain.add(scpn, BorderLayout.CENTER);
		JFrame popup = showPopupJFrame("Transaction history", 700, 500, pnMain);
		openPopups.add(popup);
	    renderData(dtm, table, listTh);
		searchTF.getDocument().addDocumentListener(new DocumentListener() {
	        public void insertUpdate(DocumentEvent e) { filterHistory(); }
	        public void removeUpdate(DocumentEvent e) { filterHistory(); }
	        public void changedUpdate(DocumentEvent e) { filterHistory(); }
	        private void filterHistory() {
	            int searchFilter = filterCBB.getSelectedIndex();
	            String searchData = searchTF.getText().trim();
	            if (searchData.equals("")) {
	            	renderData(dtm, table, listTh);
	            	filteredListForExport = listTh;
	            	return;
	            }
	            ArrayList<TransactionHistory> filteredList = new ArrayList<>();
	            for (TransactionHistory th : listTh) {
	            	if (searchFilter != 0) {
	                    if (searchFilter == 1 && (th.getId()+"").contains(searchData) ||
	                        searchFilter == 2 && th.getType().toString().toLowerCase().contains(searchData) ||
	                        searchFilter == 3 && th.getFrom().toLowerCase().contains(searchData) ||
	                        searchFilter == 4 && (th.getAmount()+"").contains(searchData) ||
	                        searchFilter == 5 && th.getTime().toLowerCase().contains(searchData)
	                    ) { filteredList.add(th); }
	                } else {
	                    if ((th.getId()+"").contains(searchData) ||
	                        th.getType().toString().toLowerCase().contains(searchData) ||
	                        th.getFrom().toLowerCase().contains(searchData) ||
	                        (th.getAmount()+"").contains(searchData) ||
	                        th.getTime().toLowerCase().contains(searchData)
	                    ) { filteredList.add(th); }
	                }
	            }
	            renderData(dtm, table, filteredList);
	            filteredListForExport = filteredList;
	        }
	    });
		exportBT.addActionListener(e -> {
			int searchFilterE = filterCBB.getSelectedIndex();
			String searchDataE = searchTF.getText().trim();
			boolean isSuccess = exportToExcel.export(searchFilterE, searchDataE, filteredListForExport);
			if (isSuccess) popup.dispose();
		});
	}
	
	public void renderData(DefaultTableModel dtm, JTable table, ArrayList<TransactionHistory> list) {
		dtm.setRowCount(0);
		int count = 0;
		if (list != null) count = list.size();
	    if (count == 0) {
	    	dtm.addRow(new String[] { "", "", "<html><p style='margin-left: 74px;'>--- No result ---</p></html>" });
	    	exportBT.setEnabled(false);
	    } else exportBT.setEnabled(true);
	    for (int i = 0; i < count; ++i) {
	    	String notConvertType = list.get(i).getType().toString();
	    	String firstUpperType = notConvertType.substring(0, 1).toUpperCase();
	    	String typeConverted = firstUpperType + notConvertType.substring(1);
	    	dtm.addRow(new String[] {
	    		"<html><p style='margin-left: 10px;'>" + list.get(i).getId() + "</p></html>",
	    		"<html><p style='margin-left: 10px;'>" + typeConverted + "</p></html>",
	    		"<html><p style='margin-left: 10px;'>" + list.get(i).getFrom() + "</p></html>",
	    		"<html><p style='margin-left: 10px;'>" + list.get(i).getAmount() + " $</p></html>",
	    		"<html><p style='margin-left: 10px;'>" + list.get(i).getTime() + "</p></html>"
	    	});
	    }
	    if (table != null) resizeTableColumnWidth.rsz(table);
	}

	public ArrayList<TransactionHistory> getNewHistory() {
		ArrayList<TransactionHistory> histories = new ArrayList<TransactionHistory>();
		try (Socket con = new Socket("localhost", Main.PORT);
			ObjectInputStream inObj = new ObjectInputStream(con.getInputStream());
			DataOutputStream outStr = new DataOutputStream(con.getOutputStream());
		) {
			String request = "renderHistoryData|" + Main.getMainViewInstance().user.getUsername();
			outStr.writeUTF(request);
			outStr.flush();
			histories = (ArrayList<TransactionHistory>) inObj.readObject();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return histories;
	}
	
	public void changePinHandle() {
		JPanel pnMain = new JPanel();
		pnMain.setLayout(new GridLayout(7, 1, 0, 16));
		JLabel oldPinLB = new JLabel("Old PIN"),
			   newPinLB = new JLabel("New PIN"),
			   retypeNewPinLB = new JLabel("Retype new PIN");
		JTextField oldPinTF = new JTextField(),
				   newPinTF = new JTextField(),
				   retypeNewPinTF = new JTextField();
		JButton changePinBT = new JButton("CHANGE PIN");
		retypeNewPinTF.setMargin(new Insets(4, 8, 4, 8));
		retypeNewPinTF.setMargin(new Insets(4, 8, 4, 8));
		retypeNewPinTF.setMargin(new Insets(4, 8, 4, 8));
		changePinBT.setBackground(Color.decode(variables.primaryColorLight));
		changePinBT.setOpaque(true);
		changePinBT.setBorderPainted(false);
		changePinBT.setMargin(new Insets(4, 8, 4, 8));
		pnMain.add(oldPinLB);
		pnMain.add(oldPinTF);
		pnMain.add(newPinLB);
		pnMain.add(newPinTF);
		pnMain.add(retypeNewPinLB);
		pnMain.add(retypeNewPinTF);
		pnMain.add(changePinBT);
		JFrame popup = showPopupJFrame("Change PIN", 400, 400, pnMain);
		openPopups.add(popup);
		changePinBT.addActionListener(e -> {
			String oldPin = oldPinTF.getText().trim();
			boolean isPinMatch = newPinTF.getText().trim().equals(retypeNewPinTF.getText().trim());
			if (!validator.validatePin(oldPinTF)) toast.showMsg("Warning", "Old PIN can't be blank and must be 6 number", "warning");
			else if (!validator.validatePin(newPinTF)) toast.showMsg("Warning", "New PIN can't be blank and must be 6 number", "warning");
			else if (!validator.validatePin(retypeNewPinTF)) toast.showMsg("Warning", "Retype new PIN can't be blank and must be 6 number", "warning");
			else if (!isPinMatch) toast.showMsg("Warning", "PIN not match", "warning");
			else if (isPinMatch && newPinTF.getText().trim().equals(Main.getMainViewInstance().user.getPin()))
			toast.showMsg("Warning", "Can't use this PIN", "warning");
			else if (!oldPin.equals(Main.getMainViewInstance().user.getPin())) toast.showMsg("Error", "Wrong PIN", "error");
			else {
				try (Socket con = new Socket("localhost", Main.PORT);
					ObjectInputStream inObj = new ObjectInputStream(con.getInputStream());
					DataOutputStream outStr = new DataOutputStream(con.getOutputStream());
				) {
					String username = Main.getMainViewInstance().user.getUsername();
					String newPin = newPinTF.getText().trim();
					String request = "changePin|" + username + "|" + newPin;
					outStr.writeUTF(request);
					outStr.flush();
					int rowUserUpdate = inObj.readInt();
					if (rowUserUpdate != 1) toast.showMsg("Error", "Can't change PIN, have an error", "error");
					else toast.showMsg("Success", "Change PIN successfully", "success");
					popup.dispose();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
	}
	
	public void statisticalHandle() {
		JPanel pnMain = new JPanel(),
			   pnInfo = new JPanel(),
			   pnDashboard = new JPanel(),
			   pnChart = new JPanel();
		pnMain.setLayout(new BorderLayout());
		pnInfo.setLayout(new BorderLayout());
		pnDashboard.setLayout(new GridLayout(1, 4));
		JButton exportBT = new JButton(" Export PDF");
		exportBT.setIcon(new ImageIcon(Main.class.getResource("/assets/export.png")));
		exportBT.setFocusable(false);
		exportBT.setMargin(new Insets(4, 8, 4, 8));
		exportBT.setBackground(Color.decode(variables.whiteBg));
		JPanel temp = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		temp.setBackground(Color.decode(variables.whiteBg));
		temp.add(exportBT);
		pnInfo.add(temp, BorderLayout.NORTH);
		JPanel pnDSHB1 = new JPanel(),
				pnDSHB2 = new JPanel(),
				pnDSHB3 = new JPanel(),
				pnDSHB4 = new JPanel();
		pnDSHB1.setBorder(BorderFactory.createLineBorder(Color.white, 10));
		pnDSHB1.setBackground(Color.decode(variables.primaryColorLight));
		pnDSHB2.setBorder(BorderFactory.createLineBorder(Color.white, 10));
		pnDSHB2.setBackground(Color.decode(variables.primaryColorLight));
		pnDSHB3.setBorder(BorderFactory.createLineBorder(Color.white, 10));
		pnDSHB3.setBackground(Color.decode(variables.primaryColorLight));
		pnDSHB4.setBorder(BorderFactory.createLineBorder(Color.white, 10));
		pnDSHB4.setBackground(Color.decode(variables.primaryColorLight));
		JLabel DSHBLB1  = new JLabel(),
			   DSHBLB2  = new JLabel(),
			   DSHBLB3  = new JLabel(),
			   DSHBLB4  = new JLabel();
		pnDSHB1.add(DSHBLB1);
		pnDSHB2.add(DSHBLB2);
		pnDSHB3.add(DSHBLB3);
		pnDSHB4.add(DSHBLB4);
		pnDashboard.add(pnDSHB1);
		pnDashboard.add(pnDSHB2);
		pnDashboard.add(pnDSHB3);
		pnDashboard.add(pnDSHB4);
		pnInfo.add(pnDashboard, BorderLayout.CENTER);
		pnMain.add(pnInfo, BorderLayout.NORTH);
		generateColumnChart chart = new generateColumnChart();
		pnChart.setLayout(new GridLayout(1, 1));
		pnChart.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		pnChart.setBackground(Color.white);
		pnMain.add(pnChart, BorderLayout.CENTER);
		JFrame popup = showPopupJFrame("Statistical", 900, 500, pnMain);
		openPopups.add(popup);
		new Thread(() -> {
			try (Socket con = new Socket("localhost", Main.PORT);
				ObjectInputStream inObj = new ObjectInputStream(con.getInputStream());
				DataOutputStream outStr = new DataOutputStream(con.getOutputStream());
			) {
				String request = "statistical|" + Main.getMainViewInstance().user.getUsername();
				outStr.writeUTF(request);
				outStr.flush();
				while (Main.getMainViewInstance().user != null) {
					ArrayList<double[]> dataDSHB = (ArrayList<double[]>) inObj.readObject();
					dataDSHBForExport = dataDSHB;
					ArrayList<String[]> dataChart = (ArrayList<String[]>) inObj.readObject();
					SwingUtilities.invokeLater(() -> {
						double totalTransaction = 0;
						for (double[] arr : dataDSHB) { totalTransaction += arr[0]; }
						DSHBLB1.setText("<html><p style='text-align: center; font-size: 16px; margin-top: 4px'>Total transactions<br>"
								+ "<b style='color: "+variables.primaryColor+"'>"+
								(totalTransaction+"").substring(0, (totalTransaction+"").indexOf("."))
								+"</b><p></html>");
						DSHBLB2.setText("<html><p style='text-align: center; font-size: 16px; margin-top: 4px'>Total recharge<br>"
								+ "<b style='color: "+variables.primaryColor+"'>"+dataDSHB.get(1)[1]+" $</b><p></html>");
						DSHBLB3.setText("<html><p style='text-align: center; font-size: 16px; margin-top: 4px'>Total transfer<br>"
								+ "<b style='color: "+variables.primaryColor+"'>"+dataDSHB.get(2)[1]+" $</b><p></html>");
						DSHBLB4.setText("<html><p style='text-align: center; font-size: 16px; margin-top: 4px'>Total receive<br>"
								+ "<b style='color: "+variables.primaryColor+"'>"+dataDSHB.get(0)[1]+" $</b><p></html>");
						chart.gnrt("Daily cash flow chart", dataChart);
						pnChart.removeAll();
						pnChart.add(chart.chartPanel);
					});
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}).start();
		exportBT.addActionListener(e -> {
			generateColumnChart.exportToPng(chart.chart);
			boolean isSuccess = exportPDF.export(dataDSHBForExport);
			if (isSuccess) popup.dispose();
		});
	}
}