package Controller;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * 
 * @author jerie
 *
 */
public class SurveyUpdateController  {

	public Button btnSurvey, back;
	public TextField q1, q2, q3, q4, q5, q6;
	public Label invalid_detailsL_q1;
	public Label invalid_detailsL_q2;
	public Label invalid_detailsL_q3;
	public Label invalid_detailsL_q4;
	public Label invalid_detailsL_q5;
	public Label invalid_detailsL_q6;
	public Boolean IsActive;
	public String date;
	public HashMap<String, String> serverMsg = new HashMap<String, String>();

	public void back(ActionEvent event) {
		try {
			Main.openMain(Main.user.getType(), event);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
/**
 * insert New Survey To DataBase
 * The Worker Have to Fill 6 Quesitons
 * @param event
 */
	public void SaveSurveyToDB(ActionEvent event)

	{

		String qq1;
		String qq2;
		String qq3;
		String qq4;
		String qq5;
		String qq6;
			/* get the details from input fields */
			try {
				qq1 = q1.getText();
			} catch (Exception e) {
				qq1 = "";
			}
			try {
				qq2 = q2.getText();
			} catch (Exception e) {
				qq2 = "";
			}
			try {
				qq3 = q3.getText();
			} catch (Exception e) {
				qq3 = "";
			}
			try {
				qq4 = q4.getText();
			} catch (Exception e) {
				qq4 = "";
			}
			try {
				qq5 = q5.getText();
			} catch (Exception e) {
				qq5 = "";
			}
			try {
				qq6 = q6.getText();
			} catch (Exception e) {
				qq6 = "";
			}
			/* initialize the error label to be no visible */
			invalid_detailsL_q1.setVisible(false);
			invalid_detailsL_q2.setVisible(false);
			invalid_detailsL_q3.setVisible(false);
			invalid_detailsL_q4.setVisible(false);
			invalid_detailsL_q5.setVisible(false);
			invalid_detailsL_q6.setVisible(false);

			/* check the inputs */
			if (qq1.isEmpty()) {
				invalid_detailsL_q1.setVisible(true);
			}
			if (qq2.isEmpty()) {
				invalid_detailsL_q2.setVisible(true);
			}
			if (qq3.isEmpty()) {
				invalid_detailsL_q3.setVisible(true);
			}
			if (qq4.isEmpty()) {
				invalid_detailsL_q4.setVisible(true);
			}
			if (qq5.isEmpty()) {
				invalid_detailsL_q5.setVisible(true);
			}
			if (qq6.isEmpty()) {
				invalid_detailsL_q6.setVisible(true);
			}
			
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			String data = df.format(new Date());
			
			serverMsg.put("msgType", "insert");
			serverMsg.put("query", "INSERT INTO survey ( Date, Q1, Q2, Q3, Q4, Q5, Q6,Status, Num_Of_Participant) VALUES ('"+data+"', '"+qq1+"', '"+qq2+"', '"+qq3+"', '"+qq4+"', '"+qq5+"','"+qq6+"', 'Active', '0');");
			try {
				Main.client.sendMessageToServer(serverMsg);
			} catch (Exception exp) {
				exp.printStackTrace();
				return;
			}
			 
		      Optional<ButtonType> result = Main.showPopUp("INFORMATION", "Survey", "The Survey Is Added ", "");

		      if (result.get() == ButtonType.OK)
				try {
					Main.openMain(Main.user.getType(), event);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
	}


}
