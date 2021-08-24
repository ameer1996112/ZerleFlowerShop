package Controller;

import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;

public class CustomerComplaintController implements Initializable {

	    @FXML
	    private ComboBox<String> comboBox;

	    @FXML
	    private TextField orderId;

	    @FXML
	    private TextField companyMail;

	    @FXML
	    private TextField userName;

	    @FXML
	    private TextArea complaint;

	    
	    private static ObservableList <Complaint>list=FXCollections.observableArrayList();
	    private static ObservableList <String> comp=FXCollections.observableArrayList();
	    
	    //private static final DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	   // private static final DateFormat sdf1 = new SimpleDateFormat("dd?MM/yyyy");
	    
	public void FillInfoInList() {
		//fill the info from the database;
	}

	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		comp.add("Divelopment Delay");
		comp.add("Bad Quality of Product");
		comp.add("Maltreatment");
		comp.add("UnderEstimated");
		comp.add("Wrong Delivry");
		comp.add("Missed Products");
		comboBox.setItems(comp);
		//Date date = new Date();
		//timeN.setText(sdf.format(date).toString());
		//dateN.setText(sdf1.format(date).toString());
		companyMail.setText("ZrLeeG10@gmail.com");
		userName.setText("Johny Salem");//set the current user name from db
	}
	
	public void SendMessage(MouseEvent event) {
		if(!CheckComplaint()) {
		final String username="ZrLeeG10@gmail.com";//from
		final String password="g10zrlee";
		
		Properties prop=new Properties();
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "587");
		
		Session session=Session.getInstance(prop,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username,password);
			}
		
		});
		try {
			Message message=new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO,InternetAddress.parse("johnysalem12@gmail.com"));//to
		    message.setSubject(comboBox.getSelectionModel().getSelectedItem()+" Complaint ! Zr-Lee");
		    message.setText("Dear Johny,\n Hello i am "+userName.getText()+" and the order num is: "+
		                     orderId.getText()+".\n"+complaint.getText()+" !!!\n thank you a lot.");
		    Transport.send(message);
		    System.out.println("DONE!!!");
		    MessageInfoWin("your Complaint had been sent successfully!!");
		}catch(MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
	
	public void MessageInfoWin(String msg) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Sending Dialog");
		alert.setHeaderText("Look, a Information Dialog");
		alert.setContentText("Done!!, "+msg+".");
		alert.showAndWait();
	}
	
	public void ErrorMessage(String msg) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error Sending Dialog");
		alert.setHeaderText("Look, a Error Dialog");
		alert.setContentText("Sorry!!,there is an error\n"+msg+".");
		alert.showAndWait();
	}
	
	public boolean OrderCheck(String id) {
		return true;
	}
	
	public boolean CheckComplaint() {
		if(complaint.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Sending Dialog");
			alert.setHeaderText("Look, a Error Dialog");
			alert.setContentText("Sorry!!,your complaint text is empty!!\nplease enter a complaint to send.");
			alert.showAndWait();
			return true;
		}
		else if(complaint.getText().length()>400) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Sending Dialog");
			alert.setHeaderText("Look, a Error Dialog");
			alert.setContentText("Too Long!!,please enter complaint less than 400 char please!.");
			alert.showAndWait();
			return true;
		}else {
			return false;
		}
	}
	
	public void BackBtn(MouseEvent event) {
		
	}
	
	public void GetUserName(String orderid) {
		String query="select OrderID,UsersAccountID from zrle.order where OrderID="+orderid;
	}

}
