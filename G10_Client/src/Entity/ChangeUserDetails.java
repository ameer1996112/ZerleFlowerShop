package Entity;

public class ChangeUserDetails {
	private String UserID;

	private String UserType;

	private String UserStatus;

	private int Branch;
	
	private int PaymentAccountStatus;
	
	private int Subscription;
	

	public ChangeUserDetails(String userID, String userType, String userStatus, int branch,
			int paymentAccountStatus, int subscription) {
		super();
		UserID = userID;
		UserType = userType;
		UserStatus = userStatus;
		Branch = branch;
		PaymentAccountStatus = paymentAccountStatus;
		Subscription = subscription;
	}

	public ChangeUserDetails(String userID, String userType, String userStatus) {
		super();
		UserID = userID;
		UserType = userType;
		UserStatus = userStatus;
	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public String getUserType() {
		return UserType;
	}

	public void setUserType(String userType) {
		UserType = userType;
	}

	public String getUserStatus() {
		return UserStatus;
	}

	public void setUserStatus(String userStatus) {
		UserStatus = userStatus;
	}

	public int getBranch() {
		return Branch;
	}

	public void setBranch(int branch) {
		Branch = branch;
	}

	public int getPaymentAccountStatus() {
		return PaymentAccountStatus;
	}

	public void setPaymentAccountStatus(int paymentAccountStatus) {
		PaymentAccountStatus = paymentAccountStatus;
	}

	public int getSubscription() {
		return Subscription;
	}

	public void setSubscription(int subscription) {
		Subscription = subscription;
	}

}
