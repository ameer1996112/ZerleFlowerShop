package Entity;

public class Payment {
private int PaymentID;
private int OrderID;
private Double totalPrice;
public Payment(int paymentID, int orderID, Double totalPrice) {
	super();
	PaymentID = paymentID;
	OrderID = orderID;
	this.totalPrice = totalPrice;
}
public int getPaymentID() {
	return PaymentID;
}
public void setPaymentID(int paymentID) {
	PaymentID = paymentID;
}
public int getOrderID() {
	return OrderID;
}
public void setOrderID(int orderID) {
	OrderID = orderID;
}
public Double getTotalPrice() {
	return totalPrice;
}
public void setTotalPrice(Double totalPrice) {
	this.totalPrice = totalPrice;
}

}
