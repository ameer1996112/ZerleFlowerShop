package Controller;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import application.Main;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

/**
 * 
 * @author jeries
 *
 */
public class CompanyServiceExpertMenu implements Initializable {
	public HashMap<String, String> serverMsg = new HashMap<String, String>();
	public String surveyID;
	public TextArea ConcText;
	public Boolean ActiveSurvey;
	public Button btnAddConclusion, btnNewSurvey,btnlogout;
	public String conclusion;

	/**
	 * To Add Conclusion to DataBase 
	 * In Case Conclusion Added The Survey Close AutoMaticlly
	 * @param event
	 */
	public void Add_Conclusion(ActionEvent event) {

		if (!ActiveSurvey)
			Main.showPopUp("INFORMATION", "Message", "There is No Active Survey", "Thank you!");
		else {
			if (ConcText.getText().isEmpty())
				Main.showPopUp("INFORMATION", "Conclusion", "In Order To Add Concluion You Have to Enter Text",
						"Enter Text In TextArea");
			else {
				conclusion = ConcText.getText();
				serverMsg.put("msgType", "update");
				serverMsg.put("query", "Update zrle.survey set Conclusion='" + conclusion
						+ "',Status='No Active' where ID='" + surveyID + "';");
				try {
					Main.client.sendMessageToServer(serverMsg);
				} catch (Exception exp) {
					exp.printStackTrace();
					return;
				}

				synchronized (Main.client) {
					try {
						Main.client.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				int answer = (int) Main.client.getMessage();
				if (answer == 1)
					Main.showPopUp("CONFIRMATION", "Confirm", "Conclusion Was Added Succesfully", "");
				else
					Main.showPopUp("Error", "Proplem !", "There Was Prolem With Updating Conclusion ",
							" Try Againe Later");
			}
			ActiveSurvey=true;
		}
	}

	/**
	 * Open New Survey In Case There Is no Active Survey in Database
	 * In Case there is Active One Shows Message
	 * @param event
	 * @throws IOException
	 */
	public void Open_New_Survey(ActionEvent event) throws IOException {
		if (!ActiveSurvey)
			Main.OpenFxmlFile(event, new String("/FXML/SurveyUpdate.fxml"));
		else
			Main.showPopUp("ERROR", "Error", "There Is Open Survey", "Add Conclusion And Close The Current Survey");
	}
	/**
	 * LogOut User from DataBase 
	 * @param event
	 */
	public void logOut(ActionEvent event)
	{
		Main.logOutBackToMenu(event);
	}
	
	@Override
	/**
	 * get Survey ID 
	 */
	public void initialize(URL arg0, ResourceBundle arg1) {

		// TODO Auto-generated method stub
		serverMsg.put("msgType", "select");
		serverMsg.put("query", "select ID from zrle.survey where Status='Active';");
		try {
			Main.client.sendMessageToServer(serverMsg);
		} catch (Exception exp) {
			exp.printStackTrace();
			return;
		}

		synchronized (Main.client) {
			try {
				Main.client.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		ArrayList<String> result = new ArrayList<String>();
		result = (ArrayList<String>) Main.client.getMessage();
		if (result.size() == 0) {
			ActiveSurvey = false;
		} else {
			ActiveSurvey = true;
			surveyID = result.get(0);
		}

	}

}
