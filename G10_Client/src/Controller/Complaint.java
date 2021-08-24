package Controller;

import java.io.Serializable;
import java.util.Date;

public class Complaint implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int ComplaintID;
	private String UserName;
	private String ComplaintText;
	private String OrderID;
	private String ComplaintTopic;
	private Date CompDate;
	public Complaint(int complaintID, String userName, String complaintText, String orderID, String complaintTopic,
			Date compDate) {
		super();
		ComplaintID = complaintID;
		UserName = userName;
		ComplaintText = complaintText;
		OrderID = orderID;
		ComplaintTopic = complaintTopic;
		CompDate = compDate;
	}
	
	public Complaint() {
		
	}

	public int getComplaintID() {
		return ComplaintID;
	}

	public void setComplaintID(int complaintID) {
		ComplaintID = complaintID;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getComplaintText() {
		return ComplaintText;
	}

	public void setComplaintText(String complaintText) {
		ComplaintText = complaintText;
	}

	public String getOrderID() {
		return OrderID;
	}

	public void setOrderID(String orderID) {
		OrderID = orderID;
	}

	public String getComplaintTopic() {
		return ComplaintTopic;
	}

	public void setComplaintTopic(String complaintTopic) {
		ComplaintTopic = complaintTopic;
	}

	public Date getCompDate() {
		return CompDate;
	}

	public void setCompDate(Date compDate) {
		CompDate = compDate;
	}

	@Override
	public String toString() {
		return "Complaint [ComplaintID=" + ComplaintID + ", UserName=" + UserName + ", ComplaintText=" + ComplaintText
				+ ", OrderID=" + OrderID + ", ComplaintTopic=" + ComplaintTopic + ", CompDate=" + CompDate + "]";
	}
	
	

}
