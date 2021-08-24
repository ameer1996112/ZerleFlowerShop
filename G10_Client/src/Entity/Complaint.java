package Entity;


public class Complaint {
	
	private int ComplaintID;
	private String ComplaintText;
	private String UserID;
	private String CompDate;
	private String CompTime;
	private String Topic;
	private String Status;
	private String Answer;
	private Refound refound;
	public int getComplaintID() {
		return ComplaintID;
	}
	public void setComplaintID(int complaintID) {
		ComplaintID = complaintID;
	}
	public String getComplaintText() {
		return ComplaintText;
	}
	public void setComplaintText(String complaintText) {
		ComplaintText = complaintText;
	}
	public String getUserID() {
		return UserID;
	}
	public void setUserID(String userID) {
		UserID = userID;
	}
	public String getCompDate() {
		return CompDate;
	}
	public void setCompDate(String compDate) {
		CompDate = compDate;
	}
	public String getCompTime() {
		return CompTime;
	}
	public void setCompTime(String compTime) {
		CompTime = compTime;
	}
	public String getTopic() {
		return Topic;
	}
	public void setTopic(String topic) {
		Topic = topic;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public String getAnswer() {
		return Answer;
	}
	public void setAnswer(String answer) {
		Answer = answer;
	}
	public Refound getRefound() {
		return refound;
	}
	public void setRefound(Refound refound) {
		this.refound = refound;
	}
	public Complaint(int complaintID, String complaintText, String userID, String compDate, String compTime,
			String topic, String status, String answer, Refound refound) {
		super();
		ComplaintID = complaintID;
		ComplaintText = complaintText;
		UserID = userID;
		CompDate = compDate;
		CompTime = compTime;
		Topic = topic;
		Status = status;
		Answer = answer;
		this.refound = refound;
	}
	public Complaint(int complaintID, String complaintText, String userID, String compDate, String compTime,
			String topic) {
		super();
		ComplaintID = complaintID;
		ComplaintText = complaintText;
		UserID = userID;
		CompDate = compDate;
		CompTime = compTime;
		Topic = topic;
	}
	
	

}


