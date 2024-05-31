/**
 * 
 */
package server;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import org.mindrot.jbcrypt.BCrypt;

import connection.dbConnector;
import daos.StatisticalDao;
import daos.TransactionHistoryDao;
import daos.UserDao;
import models.TransactionHistory;
import models.User;
import utils.timestamp;

/**
 * @author zoohuy
 * 23 thg 5, 2024
 */
public class requestHandler implements Runnable {
	private Socket clientSocket;

    public requestHandler(Socket socket) {
        this.clientSocket = socket;
    }
    
    public void run() {
        try (DataInputStream inStr = new DataInputStream(clientSocket.getInputStream());
             ObjectOutputStream outObj = new ObjectOutputStream(clientSocket.getOutputStream());
        ) {
        	String request = inStr.readUTF();
        	String[] parts = request.split("\\|");
        	String command = parts[0];

        	switch (command) {
        		case "login":
        			handleLogin(parts, outObj);
        			break;
        		case "register":
        			handleRegister(parts, outObj);
        			break;
        		case "getTimeAndBalance":
        			handleGetTimeAndBalance(parts, outObj);
        			break;
        		case "recharge":
        			handleRecharge(parts, outObj);
        			break;
        		case "transferCheckUser":
        			handleTransferCheckUser(parts, outObj);
        			break;
        		case "transfer":
        			handleTransfer(parts, outObj);
        			break;
        		case "renderHistoryData":
        			handleRenderHistoryData(parts, outObj);
        			break;
        		case "changePin":
        			handleChangePin(parts, outObj);
        			break;
        		case "statistical":
        			handleStatistical(parts, outObj);
        			break;
        		default:
        			break;
        	}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Login
    public static boolean validate2FACode(String pin, String secretCode) {
		boolean result = false;
		try {
            URL url = new URL("https://www.authenticatorapi.com/Validate.aspx?Pin=" + pin + "&SecretCode=" + secretCode);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                result = Boolean.parseBoolean(response.toString());
            } else {
                System.out.println("Error when call API: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		return result;
	}
    
    private void handleLogin(String[] parts, ObjectOutputStream outObj) throws Exception {
    	String username = parts[1];
        String password = parts[2];
        String twoFA = parts[3];
        User userFound = UserDao.getInstance().read(new User(username, "", "", "", "", 0));
        boolean validatePassword = false;
        boolean validate2FACode = false;
        if (userFound != null) {
        	validatePassword = BCrypt.checkpw(password, userFound.getPassword());
        	validate2FACode = validate2FACode(twoFA, userFound.getSecretCode());
        }
        outObj.writeObject(userFound);
        outObj.flush();
        outObj.writeBoolean(validatePassword);
        outObj.flush();
        outObj.writeBoolean(validate2FACode);
        outObj.flush();
    }
    
    // Register
    public static String generateRdmSecretCode() {
		String result = "ZBANK";
		for (int i = 0; i < 5; ++i) {
			result += Math.round(Math.random() * 5);
		}
		return result;
	}
	
	public static String getQRCodeUrl(String appInfo, String secretCode) {
		String result = "";
		try {
            URL url = new URL("https://www.authenticatorapi.com/pair.aspx?AppName=ZBank&AppInfo=" + appInfo + "&SecretCode=" + secretCode);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                result = response.substring(response.indexOf("src") + 5, response.indexOf("border") - 2);
            } else {
                System.out.println("Error when call API: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		return result;
	}
	
	private void handleRegister(String[] parts, ObjectOutputStream outObj) throws Exception {
		String username = parts[1];
		String password = parts[2];
		String fullname = parts[3];
		String pin = parts[4];
		String rdmSecretCode = generateRdmSecretCode();
		int rowUpdate = UserDao.getInstance().create(new User(username, password, fullname, rdmSecretCode, pin, 0));
		String imgSrc = getQRCodeUrl(username, rdmSecretCode);
		outObj.writeInt(rowUpdate);
		outObj.flush();
		outObj.writeUTF(imgSrc);
		outObj.flush();
	}
    
    // pnTop
	private void handleGetTimeAndBalance(String[] parts, ObjectOutputStream outObj) throws Exception {
	    while (true) {
	    	String username = parts[1];
	    	User user = UserDao.getInstance().read(new User(username, "", "", "", "", 0));
	    	double balance = user.getBalance();
	    	String timeEng = timestamp.getTimeEnglish();
	    	String output = timeEng + "|" + balance;
	    	outObj.writeUTF(output);
	    	outObj.flush();
	    	Thread.sleep(1000);
	    }
    }

    // pnBottom
    private void handleRecharge(String[] parts, ObjectOutputStream outObj) throws Exception {
    	String username = parts[1];
    	Double amount = Double.parseDouble(parts[2]);
    	User user = UserDao.getInstance().read(new User(username, "", "", "", "", 0));
    	Double currentBalance = user.getBalance();
    	Double newBalance = currentBalance + amount;
		User updateUser = new User(username, "", "", "", "", newBalance);
		TransactionHistory th = new TransactionHistory(0, username, parts[0], username, amount, timestamp.getTime());
    	int rowUserUpdate = UserDao.getInstance().update(updateUser, parts[0]);
		int rowThUpdate = TransactionHistoryDao.getInstance().create(th);
		outObj.writeInt(rowUserUpdate);
		outObj.flush();
		outObj.writeInt(rowThUpdate);
		outObj.flush();
    }
    
    private void handleTransferCheckUser(String[] parts, ObjectOutputStream outObj) throws Exception {
    	String transferUsername = parts[1];
		String receiverUsername = parts[2];
		User transferUser = UserDao.getInstance().read(new User(transferUsername, "", "", "", "", 0));
		User receiverUser = UserDao.getInstance().read(new User(receiverUsername, "", "", "", "", 0));
		outObj.writeObject(transferUser);
		outObj.flush();
		outObj.writeObject(receiverUser);
		outObj.flush();
    }
    
    private void handleTransfer(String[] parts, ObjectOutputStream outObj) throws Exception {
    	Connection con = null;
    	try {
    		con = dbConnector.getConnection();
    		con.setAutoCommit(false);
    		String transferUsername = parts[1];
    		Double currentTransferBalance = Double.parseDouble(parts[2]);
    		String receiverUsername = parts[3];
    		Double currentReceiverBalance = Double.parseDouble(parts[4]);
    		Double amount = Double.parseDouble(parts[5]);
    		Double newTransferBalance = currentTransferBalance - amount;
    		Double newReceiverBalance = currentReceiverBalance + amount;
    		User updateTransferUser = new User(transferUsername, "", "", "", "", newTransferBalance);
    		User updateReceiverUser = new User(receiverUsername, "", "", "", "", newReceiverBalance);
    		TransactionHistory transferTh = new TransactionHistory(0, transferUsername, parts[0], transferUsername, amount, timestamp.getTime());
    		TransactionHistory receiverTh = new TransactionHistory(0, receiverUsername, "receive", transferUsername, amount, timestamp.getTime());
    		int rowTransferUpdate = UserDao.getInstance().update(updateTransferUser, parts[0], con);
    		int rowReceiverUpdate = UserDao.getInstance().update(updateReceiverUser, "receive", con);
    		int rowTransferThUpdate = TransactionHistoryDao.getInstance().create(transferTh, con);
    		int rowReceiverThUpdate = TransactionHistoryDao.getInstance().create(receiverTh, con);
    		if (rowTransferUpdate > 0 && rowReceiverUpdate > 0 && rowTransferThUpdate > 0 && rowReceiverThUpdate > 0) {
    			con.commit();
    			outObj.writeInt(1);
    		} else {
    			con.rollback();
    			outObj.writeInt(-1);
    		}
    	} catch (Exception e) {
            if (con != null) {
                con.rollback();
            }
            outObj.writeInt(-1);
            throw e;
        } finally {
            if (con != null) {
                con.setAutoCommit(true);
                dbConnector.closeConnection(con);
            }
            outObj.flush();
        }
    }
    
    private void handleRenderHistoryData(String[] parts, ObjectOutputStream outObj) throws Exception {
    	String username = parts[1];
    	ArrayList<TransactionHistory> listTh = TransactionHistoryDao.getInstance().readByUsername(username);
    	outObj.writeObject(listTh);
    }
    
    private void handleChangePin(String[] parts, ObjectOutputStream outObj) throws Exception {
    	String username = parts[1];
    	String newPin = parts[2];
    	User updateUser = new User(username, "", "", "", newPin, 0);
		int rowUserUpdate = UserDao.getInstance().update(updateUser, "changepin");
		outObj.writeInt(rowUserUpdate);
    }
    
    private void handleStatistical(String[] parts, ObjectOutputStream outObj) throws Exception {
    	while (true) {
	    	String username = parts[1];
	    	ArrayList<double[]> dataDSHB = StatisticalDao.getInstance().readCount(username);
			ArrayList<String[]> dataChart = StatisticalDao.getInstance().readChartData(username);
	    	outObj.writeObject(dataDSHB);
	    	outObj.flush();
	    	outObj.writeObject(dataChart);
	    	outObj.flush();
	    	Thread.sleep(1000);
	    }
    }
}