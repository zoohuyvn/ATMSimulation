/**
 * 
 */
package server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.attribute.AclEntry;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.java_websocket.WebSocket;

import ATMSimulation.Main;
import daos.UserDao;
import models.TransactionHistory;
import models.User;
import views.mainView;

/**
 * @author zoohuy
 * 20 thg 5, 2024
 */
public class server {
	public void start() throws IOException {
		ServerSocket serverSocket = new ServerSocket(Main.PORT);
		System.out.println("[Server] -> Ready...");
        while (true) {
        	Socket clientSocket = serverSocket.accept();
	        System.out.println("[Server] -> New client connected (#" + clientSocket.getPort() + ")");
            new Thread(new requestHandler(clientSocket)).start();
        }
    }
	
	public static void main(String[] args) {
		try { new server().start(); }
		catch (IOException e) { e.printStackTrace(); }
	}
}