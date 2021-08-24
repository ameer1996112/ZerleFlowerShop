package Entity;

import java.io.File;
import java.io.Serializable;
import java.sql.Date;
import java.time.Year;
import javafx.scene.image.Image;

public class report implements Serializable{
	/**
	 * the variables below is a fields in report class
	 */
	String ReportID ;
	String ReportDate  ; 
	String ReportType ;
	String BranchID ;
	String ReportQuarter ;
    private String ReportFile;
    String ManagerID;
    private String ReportYear;
    String status;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setReportDate(String reportDate) {
		ReportDate = reportDate;
	}
	/**
	 * how to display the report data
	 * @return
	 */
    public String getManagerID() {
		return ManagerID;
	}
	public void setManagerID(String ID) {
		ManagerID = ID;
	}
	public String getReportType() {
		return ReportType;
	}
	public void setReportType(String reportType) {
		this.ReportType = reportType;
	}
	public String getReportID() {
		return ReportID;
	}
	public void setReportID(String ID) {
		this.ReportID = ID;
	}
	public String getReportDate() {
		return ReportDate;
	}
	public void setReportYear(String reportYear) {
		this.ReportDate = reportYear;
	}
	public String getReportQuarter() {
		return ReportQuarter;
	}
	
	public void setReportQuarter(String reportQuarter) {
		this.ReportQuarter = reportQuarter;
	}
	
	public String getBranchID() {
		return BranchID;
	}
	public void setBranchID(String branchID) {
		this.BranchID = branchID;
	}
	public String getDescription() {
		return this.getReportFile();
	}
	public void setReportDescription(String cat) {
		this.setReportFile(cat);
	}
	public String getReportFile() {
		return ReportFile;
	}
	public void setReportFile(String reportFile) {
		this.ReportFile = reportFile;
	}
	public String getReportYear() {
		return ReportYear;
	}

	
	@Override
	public String toString() {
		return "Reports [ReportType=" + ReportType + ", ReportDate=" + ReportDate + ", ReportQuarter=" + ReportQuarter
				+ " BranchID = " + BranchID + "";
	}
	public report(String Type, String Date, String reportQuarter, String branchID,String File,String id,String reportYear) {
		super();
		ReportType = Type;
		ReportDate= Date;
		ReportQuarter = reportQuarter;
	    ReportFile=File;
		BranchID = branchID;
		ReportID=id;
		ReportYear=reportYear;
	}
	


	
	
}


