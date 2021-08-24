package Entity;

public class Refound {
	private String refoundID;
	private String UserID;
	private String Description;
	private String ReturnType;
	private String BalanceReturned;
	
	public Refound(String refoundID) {
		super();
		this.refoundID = refoundID;
	}
	
	public Refound(String refoundID, String userID, String description, String returnType, String balanceReturned) {
		super();
		this.refoundID = refoundID;
		UserID = userID;
		Description = description;
		ReturnType = returnType;
		BalanceReturned = balanceReturned;
	}
	public Refound(String refoundID, String userID) {
		super();
		this.refoundID = refoundID;
		UserID = userID;
	}
	public String getRefoundID() {
		return refoundID;
	}
	public void setRefoundID(String refoundID) {
		this.refoundID = refoundID;
	}
	public String getUserID() {
		return UserID;
	}
	public void setUserID(String userID) {
		UserID = userID;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getReturnType() {
		return ReturnType;
	}
	public void setReturnType(String returnType) {
		ReturnType = returnType;
	}
	public String getBalanceReturned() {
		return BalanceReturned;
	}
	public void setBalanceReturned(String balanceReturned) {
		BalanceReturned = balanceReturned;
	}
	
	
	

}
