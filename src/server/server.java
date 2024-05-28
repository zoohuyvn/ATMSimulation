/**
 * 
 */
package server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import ATMSimulation.Main;

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