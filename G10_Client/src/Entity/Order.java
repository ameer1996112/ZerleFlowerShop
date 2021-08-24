package Entity;

import java.sql.Date;
import java.util.ArrayList;

public class Order {
	private String OrderID;
	private String UserID;
	private String BranchID;
	private String DeliveryID;
	private String Date;
	private String PaymentID;
	private ArrayList<Product> OrderProudcts;
	private int[] quantity;
	private int size;
	private double totalPrice;
	private int orderStatus;
	private Delivery delivery;
	private String greeting;
	private Payment payment;
	private String OrderType;

	public Order(String userID) {
		UserID = userID;
	}

	// new Order(OrderID,BranchID,UserID );
	public Order(String orderID, String branchID, String userID) {
		OrderID = orderID;
		BranchID = branchID;
		UserID = userID;
		this.size = 0;
		this.orderStatus = 0;
	}

	public Order(String orderID, double totalCost, String Date) {
		this.OrderID = orderID;
		this.totalPrice = totalCost;
		this.Date = Date;
	}

	public Order(String orderID, String userID, String branchID, String date) {
		OrderID = orderID;
		UserID = userID;
		BranchID = branchID;
		Date = date;
		this.size = 0;
		this.orderStatus = 0;
	}

	public Order(String orderID, String userID, String branchID, String deliveryID, String date, String paymentID,
			ArrayList<Product> orderProudcts) {
		super();
		OrderID = orderID;
		UserID = userID;
		BranchID = branchID;
		DeliveryID = deliveryID;
		this.Date = date;
		PaymentID = paymentID;
		this.size = 0;
		this.orderStatus = 0;
	}

	public ArrayList<Product> getOrderProudcts() {
		if (this.OrderProudcts == null)
			return null;
		return OrderProudcts;
	}

	public void addProudctTOorder(int index, Product proudct) {
		OrderProudcts.add(index, proudct);
	}

	public void setOrderProudcts(ArrayList<Product> orderProudcts) {
		OrderProudcts = orderProudcts;
	}

	public void setProudct(Product proudct) {
		this.OrderProudcts.add(proudct);
	}

	public String getOrderID() {
		return OrderID;
	}

	public void setOrderID(String orderID) {
		OrderID = orderID;
	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public String getBranchID() {
		return BranchID;
	}

	public void setBranchID(String branchID) {
		BranchID = branchID;
	}

	public String getDeliveryID() {
		return DeliveryID;
	}

	public void setDeliveryID(String deliveryID) {
		DeliveryID = deliveryID;
	}

	public String getDate() {
		return Date;
	}

	public void setDate(String date) {
		this.Date = date;
	}

	public String getPaymentID() {
		return PaymentID;
	}

	public void setPaymentID(String paymentID) {
		PaymentID = paymentID;
	}

	public int[] getQuantity() {
		return quantity;
	}

	public void setQuan(int index, int quan) {
		this.quantity[index] = quan;
	}

	public void setQuantity(int[] quantity) {
		this.quantity = quantity;
	}

	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Delivery getDelivery() {
		return delivery;
	}

	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
	}

	public String getGreeting() {
		return greeting;
	}

	public void setGreeting(String greeting) {
		this.greeting = greeting;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public String getOrderType() {
		return OrderType;
	}

	public void setOrderType(String orderType) {
		OrderType = orderType;
	}

	public boolean contain(Product proudct) {
		for (int i = 0; i < OrderProudcts.size(); i++) {
			String proudctID = OrderProudcts.get(i).toString();
			// System.out.println(proudctID.indexOf(","));
			proudctID = proudctID.substring(19, proudctID.indexOf(","));
			proudctID = proudctID.replaceAll("[^\\d.]", "");
			if (proudctID.equals(proudct.getProductID())) {
				return true;
			}

		}
		return false;
	}
}
