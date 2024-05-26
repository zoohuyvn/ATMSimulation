/**
 * 
 */
package models;

import java.io.Serializable;

/**
 * @author zoohuy
 * 18 thg 5, 2024
 */
public class TransactionHistory implements Serializable {
	private int id;
	private String username, type, from, time;
	double amount;
	
	public TransactionHistory() {}
	public TransactionHistory(int id, String username, String type, String from, double amount, String time) {
		this.id = id;
		this.username = username;
		this.type = type;
		this.from = from;
		this.amount = amount;
		this.time = time;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getFrom() {
		return from;
	}
	
	public void setFrom(String from) {
		this.from = from;
	}
	
	public String getTime() {
		return time;
	}
	
	public void setTime(String time) {
		this.time = time;
	}
	
	public double getAmount() {
		return amount;
	}
	
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public String toString() {
		return "transactionHistory [id=" + id + ", username=" + username + ", type=" + type + ", from=" + from + ", time=" + time + ", amount=" + amount + "]";
	}
}