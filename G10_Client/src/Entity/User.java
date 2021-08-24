package Entity;

import java.util.ArrayList;
import java.util.HashMap;

import application.Main;

public class User {
	/* UserID-User ID */
	private String UserID;

	/* Address-User Address */
	private Address Address;

	/* Phone-User phone */
	private String Phone;

	/* Email-User Email */
	private String Email;

	/* Type - UserType */
	private String Type;

	/* Branch-BranchID */
	private String Branch;

	private Order order;
	
	private ArrayList<PaymentAccount> paymentAccounts;

	/* Deafult constructor */
	public User() {

	}

	public User(String userID) {
		UserID = userID;

	}
	public User(String userID, String email, String phone, String type, String BranchID) {
		this.UserID = userID;
		this.Email = email;
		this.Phone = phone;
		this.Type = type;
		this.Branch = BranchID;
	}
	public User(String userID, String email,Address address ,String phone, String type, String BranchID) {
		this.UserID = userID;
		this.Address = address;
		this.Email = email;
		this.Phone = phone;
		this.Type = type;
		this.Branch = BranchID;
		this.paymentAccounts= new ArrayList<PaymentAccount>();
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	/* getUserInfo ()-getting user info */
	public static User getUserInfo(String userID) {
		User usr;
		HashMap<String, String> msgServer = new HashMap<>();
		msgServer.put("msgType", "select");
		msgServer.put("query",
				"select UserID,Email,Phone,UserType ,users.AddressID,address.City,address.Street,address.ZipCode , BranchIDfrom users join address on users.AddressID=address.AddressID where users.UserID='"
						+ userID + "'");

		ArrayList<String> result = sendMsg2(msgServer);
		Address address = new Address(result.get(4), result.get(5), result.get(6), result.get(7));
		usr = new User(result.get(0), result.get(1), address, result.get(2), result.get(3), result.get(8));

		return usr;

	}

	/** sendMsg -Send message to the server */
	@SuppressWarnings("unchecked")
	public static ArrayList<String> sendMsg2(HashMap<String, String> msgServer) {
		try {
			Main.client.sendMessageToServer(msgServer);
		} catch (Exception exp) {
			System.out.println("Server fatal error!");
		}
		synchronized (Main.client) {
			try {
				Main.client.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		ArrayList<String> courseResult = (ArrayList<String>) Main.client.getMessage();
		if (courseResult == null)
			return null;
		return courseResult;
	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public Address getAddress() {
		return Address;
	}

	public void setAddress(Address address) {
		Address = address;
	}

	public String getPhone() {
		return Phone;
	}

	public void setPhone(String phone) {
		Phone = phone;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getBranch() {
		return Branch;
	}

	public void setBranch(String branch) {
		Branch = branch;
	}
	public ArrayList<PaymentAccount> getPaymentAccounts() {
		return paymentAccounts;
	}

	public void setPaymentAccounts(ArrayList<PaymentAccount> paymentAccounts) {
		this.paymentAccounts = paymentAccounts;
	}
	public void setPayment(PaymentAccount payment){
		this.paymentAccounts.add(payment);
	}

	public static ArrayList<String> getUserIdByType(String type) {
		HashMap<String, String> msgServer = new HashMap<>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select UserID From users WHERE userType='" + type + "';");
		try {
			Main.client.sendMessageToServer(msgServer);
		} catch (Exception e) {
			System.out.println("Server fatal error!");

		}
		synchronized (Main.client) {
			try {
				Main.client.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		ArrayList<String> result = (ArrayList<String>) Main.client.getMessage();

		if (result.size() > 0)
			return result;
		return null;

	}

	public ArrayList<String> getBranchByID() {
		HashMap<String,String> msgServer= new HashMap<String,String>();
	msgServer.put("msgType","select");
	msgServer.put("query","select * from branch");
	msgServer.put("msgType", "select");
	msgServer.put("query", "SELECT  ProductID from product ;");
	try {
		Main.client.sendMessageToServer(msgServer);
		synchronized (Main.client) {
			Main.client.wait();
		}
	} catch (InterruptedException e1) {
		e1.printStackTrace();
	}
	ArrayList<String> answerMsg = (ArrayList<String>) Main.client.getMessage();
	if (answerMsg.size() == 0) {
		System.out.println("Error:didn't found the required data in the database");
	}
	return answerMsg;
		
	}
}
