package Entity;

public class PaymentAccount {
	private String creditCardNumber;
	private String paymentType;
	private String SubscriptionType;
	private Double Balance;
	private int PaymentAccountStatus;
	private String DateApproved;
	private int BranchID;
	private Double subTotal;
	String UserID;

	public PaymentAccount() {

	}

	public PaymentAccount(String creditCardNumber, String paymentType, String subscriptionType, Double balance,
			int paymentAccountStatus, int branchID, Double SubTotal) {
		super();
		this.creditCardNumber = creditCardNumber;
		this.paymentType = paymentType;
		SubscriptionType = subscriptionType;
		Balance = balance;
		PaymentAccountStatus = paymentAccountStatus;
		BranchID = branchID;
		subTotal = SubTotal;
	}

	public PaymentAccount(String userID, int branchID, String sub,int paymentAccountStatus) {
		this.UserID = userID;
		this.BranchID = branchID;
		this.SubscriptionType = sub;
		this.PaymentAccountStatus = PaymentAccountStatus;

	}

	public String getCreditCardNumber() {
		return creditCardNumber;
	}

	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getSubscriptionType() {
		return SubscriptionType;
	}

	public void setSubscriptionType(String subscriptionType) {
		SubscriptionType = subscriptionType;
	}

	public Double getBalance() {
		return Balance;
	}

	public void setBalance(Double balance) {
		Balance = balance;
	}

	public int getPaymentAccountStatus() {
		return PaymentAccountStatus;
	}

	public void setPaymentAccountStatus(int paymentAccountStatus) {
		PaymentAccountStatus = paymentAccountStatus;
	}

	public int getBranchID() {
		return BranchID;
	}

	public void setBranchID(int branchID) {
		BranchID = branchID;
	}

	public Double getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(Double subTotal) {
		this.subTotal = subTotal;
	}

	public String getDateApproved() {
		return DateApproved;
	}

	public void setDateApproved(String dateApproved) {
		DateApproved = dateApproved;
	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

}
