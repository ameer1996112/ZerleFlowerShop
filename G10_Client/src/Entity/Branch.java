package Entity;

public class Branch {
	private String BranchID;
	private String BranchName;
	private String BranchManager;
	public Branch(String branchID, String branchName, String branchManager) {
		super();
		BranchID = branchID;
		BranchName = branchName;
		BranchManager = branchManager;
	}
	public String getBranchID() {
		return BranchID;
	}
	public void setBranchID(String branchID) {
		BranchID = branchID;
	}
	public String getBranchName() {
		return BranchName;
	}
	public void setBranchName(String branchName) {
		BranchName = branchName;
	}
	public String getBranchManager() {
		return BranchManager;
	}
	public void setBranchManager(String branchManager) {
		BranchManager = branchManager;
	}
	
}
