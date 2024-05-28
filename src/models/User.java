/**
 * 
 */
package models;
import java.io.Serializable;

/**
 * @author zoohuy
 * 18 thg 5, 2024
 */
public class User implements Serializable {
	private String username, password, fullname, secretCode, pin;
	private double balance;
	
	public User() {}
	public User(String username, String password, String fullname, String secretCode, String pin, double balance) {
		this.username = username;
		this.password = password;
		this.fullname = fullname;
		this.secretCode = secretCode;
		this.pin = pin;
		this.balance = balance;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getSecretCode() {
		return secretCode;
	}

	public void setSecretCode(String secretCode) {
		this.secretCode = secretCode;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String toString() {
		return "User [username=" + username + ", password=" + password + ", fullname=" + fullname + ", secretCode=" + secretCode + ", pin=" + pin + ", balance=" + balance + "]";
	}
}