package Controller;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import Entity.Survey;
import application.Main;

/**
 * 
 * @author jeries
 *
 */
public class AnswerSurveyController implements Initializable {
	public RadioButton R1_1, R1_2, R1_3, R1_4, R1_5, R1_6, R1_7, R1_8, R1_9, R1_10;
	public RadioButton R2_1, R2_2, R2_3, R2_4, R2_5, R2_6, R2_7, R2_8, R2_9, R2_10;
	public RadioButton R3_1, R3_2, R3_3, R3_4, R3_5, R3_6, R3_7, R3_8, R3_9, R3_10;
	public RadioButton R4_1, R4_2, R4_3, R4_4, R4_5, R4_6, R4_7, R4_8, R4_9, R4_10;
	public RadioButton R5_1, R5_2, R5_3, R5_4, R5_5, R5_6, R5_7, R5_8, R5_9, R5_10;
	public RadioButton R6_1, R6_2, R6_3, R6_4, R6_5, R6_6, R6_7, R6_8, R6_9, R6_10;
	public Button Back_to_main, next_customer, submit_survey;
	public ToggleGroup a1, a2, a3, a4, a5, a6;
	public Label q1, q2, q3, q4, q5, q6;
	public Label survey_id;
	public TextArea user_comments;
	public ComboBox<String> users_id;
	public static Survey current_survey;
	public ArrayList<Survey>Surveys;
	public int surveyCounter;
	public Double avgq1, avgq2, avgq3, avgq4, avgq5, avgq6;
	public String str1,str2,str3,str4,str5,str6;
	ObservableList<String> list;
	ArrayList<String> answer = new ArrayList<String>();
	HashMap<String, String> serverMsg = new HashMap<String, String>();

	/**
	 * Save Current Survey to ArrayList and Get Another Answers from Other Client
	 * @param event
	 * @throws IOException
	 */
	public void NextCustomer(ActionEvent event) throws IOException {
		if (check_form()) {
			get_answers();
			String temp = Integer.toString(surveyCounter + 1);
			Main.showPopUp("INFORMATION", "Message", "Your answers for customer number  " + temp + " were submitted ",
					"Thank you!");
			Surveys.add(current_survey);
			resetTextColor();
			ResetButtons();
			surveyCounter++;
		} else {

			Main.showPopUp("INFORMATION", "Message", "In order to complete the survey to will have to answers all 6 Q",
					"Invalid fields!");

		}

		current_survey = new Survey();

	}
/**
 * Back To Main Menu
 * @param event
 * @throws IOException
 */
	public void back_to_main(ActionEvent event) throws IOException {


		try {
			Main.openMain("5", event);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * get average Of answers for Questions 
	 * @param event
	 * @throws IOException
	 */
	public void form_submit(ActionEvent event) throws IOException {
		for (Survey s : Surveys) {
			avgq1 += s.getA1();
			avgq2 += s.getA2();
			avgq3 += s.getA3();
			avgq4 += s.getA4();
			avgq5 += s.getA5();
			avgq6 += s.getA6();
		}
		avgq1 /= surveyCounter;
		avgq2 /= surveyCounter;
		avgq3 /= surveyCounter;
		avgq4 /= surveyCounter;
		avgq5 /= surveyCounter;
		avgq6 /= surveyCounter;
		 str1=new DecimalFormat("##.#").format(avgq1);
		 str2=new DecimalFormat("##.#").format(avgq2);
		 str3=new DecimalFormat("##.#").format(avgq3);
		 str4=new DecimalFormat("##.#").format(avgq4);
		 str5=new DecimalFormat("##.#").format(avgq5);
		 str6=new DecimalFormat("##.#").format(avgq6);
		SaveSurveyAnswers();
	}
/**
 * save Survey to DataBase
 */
	public void SaveSurveyAnswers() {
		HashMap<String, String> serverMsg = new HashMap<String, String>();
		serverMsg.put("msgType", "update");
		serverMsg.put("query", "update zrle.survey set A1='" + str1 + "', A2='" + str2 + "',A3='" + str3 + "',A4='"
				+ str4 + "',A5='"+str5+"',A6='"+str6+"',Num_Of_Participant='"+surveyCounter+"' where ID='"+Surveys.get(0).getID()+"';");
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		int result = (int) Main.client.getMessage();
		if (result == 1)
			Main.showPopUp("CONFIRMATION", "Confirm", "Survey Was updated", "");
		else
			Main.showPopUp("ERROR", "Proplem !", "There Was Prolem With Updating Survey", " Try Againe Later");
	
	}
/**
 * Reset Colors in Case The Worker Didnot Fill All Questions 
 */
	public void resetTextColor() {
		q1.setTextFill(Color.BLACK);
		q2.setTextFill(Color.BLACK);
		q3.setTextFill(Color.BLACK);
		q4.setTextFill(Color.BLACK);
		q5.setTextFill(Color.BLACK);
		q6.setTextFill(Color.BLACK);
	}

/**
 * Check if the Worker answered all of the Questions
 * @return
 */
	private boolean check_form() {

		boolean answer = true;
		if (a1.getSelectedToggle() == null) {
			q1.setTextFill(Color.web("#ed0b31"));
			answer = false;
		}
		if (a2.getSelectedToggle() == null) {
			q2.setTextFill(Color.web("#ed0b31"));
			answer = false;
		}
		if (a3.getSelectedToggle() == null) {
			q3.setTextFill(Color.web("#ed0b31"));
			answer = false;
		}
		if (a4.getSelectedToggle() == null) {
			q4.setTextFill(Color.web("#ed0b31"));
			answer = false;
		}
		if (a5.getSelectedToggle() == null) {
			q5.setTextFill(Color.web("#ed0b31"));
			answer = false;
		}
		if (a6.getSelectedToggle() == null) {
			q6.setTextFill(Color.web("#ed0b31"));
			answer = false;
		}
		return answer;
	}

	/**
	 * Set Answers for Current Survery
	 */
	public void get_answers() {

		if (a1.getSelectedToggle() == R1_1) {
			current_survey.setA1(1.0);
		}
		if (a1.getSelectedToggle() == R1_2) {
			current_survey.setA1(2.0);
		}
		if (a1.getSelectedToggle() == R1_3) {
			current_survey.setA1(3.0);
		}
		if (a1.getSelectedToggle() == R1_4) {
			current_survey.setA1(4.0);
		}
		if (a1.getSelectedToggle() == R1_5) {
			current_survey.setA1(5.0);
		}
		if (a1.getSelectedToggle() == R1_6) {
			current_survey.setA1(6.0);
		}

		if (a1.getSelectedToggle() == R1_7) {
			current_survey.setA1(7.0);
		}
		if (a1.getSelectedToggle() == R1_8) {
			current_survey.setA1(8.0);
		}
		if (a1.getSelectedToggle() == R1_9) {
			current_survey.setA1(9.0);
		}
		if (a1.getSelectedToggle() == R1_10) {
			current_survey.setA1(10.0);
		}

		if (a2.getSelectedToggle() == R2_1) {
			current_survey.setA2(1.0);
		}
		if (a2.getSelectedToggle() == R2_2) {
			current_survey.setA2(2.0);
		}
		if (a2.getSelectedToggle() == R2_3) {
			current_survey.setA2(3.0);
		}
		if (a2.getSelectedToggle() == R2_4) {
			current_survey.setA2(4.0);
		}
		if (a2.getSelectedToggle() == R2_5) {
			current_survey.setA2(5.0);
		}
		if (a2.getSelectedToggle() == R2_6) {
			current_survey.setA2(6.0);
		}

		if (a2.getSelectedToggle() == R2_7) {
			current_survey.setA2(7.0);
		}
		if (a2.getSelectedToggle() == R2_8) {
			current_survey.setA2(8.0);
		}
		if (a2.getSelectedToggle() == R2_9) {
			current_survey.setA2(9.0);
		}
		if (a2.getSelectedToggle() == R2_10) {
			current_survey.setA2(10.0);
		}

		if (a3.getSelectedToggle() == R3_1) {
			current_survey.setA3(1.0);
		}
		if (a3.getSelectedToggle() == R3_2) {
			current_survey.setA3(2.0);
		}
		if (a3.getSelectedToggle() == R3_3) {
			current_survey.setA3(3.0);
		}
		if (a3.getSelectedToggle() == R3_4) {
			current_survey.setA3(4.0);
		}
		if (a3.getSelectedToggle() == R3_5) {
			current_survey.setA3(5.0);
		}
		if (a3.getSelectedToggle() == R3_6) {
			current_survey.setA3(6.0);
		}

		if (a3.getSelectedToggle() == R3_7) {
			current_survey.setA3(7.0);
		}
		if (a3.getSelectedToggle() == R3_8) {
			current_survey.setA3(8.0);
		}
		if (a3.getSelectedToggle() == R3_9) {
			current_survey.setA3(9.0);
		}
		if (a3.getSelectedToggle() == R3_10) {
			current_survey.setA3(10.0);
		}

		if (a4.getSelectedToggle() == R4_1) {
			current_survey.setA4(1.0);
		}
		if (a4.getSelectedToggle() == R4_2) {
			current_survey.setA4(2.0);
		}
		if (a4.getSelectedToggle() == R4_3) {
			current_survey.setA4(3.0);
		}
		if (a4.getSelectedToggle() == R4_4) {
			current_survey.setA4(4.0);
		}
		if (a4.getSelectedToggle() == R4_5) {
			current_survey.setA4(5.0);
		}
		if (a4.getSelectedToggle() == R4_6) {
			current_survey.setA4(6.0);
		}

		if (a4.getSelectedToggle() == R4_7) {
			current_survey.setA4(7.0);
		}
		if (a4.getSelectedToggle() == R4_8) {
			current_survey.setA4(8.0);
		}
		if (a4.getSelectedToggle() == R4_9) {
			current_survey.setA4(9.0);
		}
		if (a4.getSelectedToggle() == R4_10) {
			current_survey.setA4(10.0);
		}

		if (a5.getSelectedToggle() == R5_1) {
			current_survey.setA5(1.0);
		}
		if (a5.getSelectedToggle() == R5_2) {
			current_survey.setA5(2.0);
		}
		if (a5.getSelectedToggle() == R5_3) {
			current_survey.setA5(3.0);
		}
		if (a5.getSelectedToggle() == R5_4) {
			current_survey.setA5(4.0);
		}
		if (a5.getSelectedToggle() == R5_5) {
			current_survey.setA5(5.0);
		}
		if (a5.getSelectedToggle() == R5_6) {
			current_survey.setA5(6.0);
		}

		if (a5.getSelectedToggle() == R5_7) {
			current_survey.setA5(7.0);
		}
		if (a5.getSelectedToggle() == R5_8) {
			current_survey.setA5(8.0);
		}
		if (a5.getSelectedToggle() == R5_9) {
			current_survey.setA5(9.0);
		}
		if (a5.getSelectedToggle() == R5_10) {
			current_survey.setA5(10.0);
		}

		if (a6.getSelectedToggle() == R6_1) {
			current_survey.setA6(1.0);
		}
		if (a6.getSelectedToggle() == R6_2) {
			current_survey.setA6(2.0);
		}
		if (a6.getSelectedToggle() == R6_3) {
			current_survey.setA6(3.0);
		}
		if (a6.getSelectedToggle() == R6_4) {
			current_survey.setA6(4.0);
		}
		if (a6.getSelectedToggle() == R6_5) {
			current_survey.setA6(5.0);
		}
		if (a6.getSelectedToggle() == R6_6) {
			current_survey.setA6(6.0);
		}

		if (a6.getSelectedToggle() == R6_7) {
			current_survey.setA6(7.0);
		}
		if (a6.getSelectedToggle() == R6_8) {
			current_survey.setA6(8.0);
		}
		if (a6.getSelectedToggle() == R6_9) {
			current_survey.setA6(9.0);
		}
		if (a6.getSelectedToggle() == R6_10) {
			current_survey.setA6(10.0);
		}

	}


/**
 * Query to get the Active Survey
 * in case there is no Active Survey it shows to the user  and back to his menu
 * @param event
 */
	public void get_survey_qustion(ActionEvent event) {
		HashMap<String, String> serverMsg = new HashMap<String, String>();
		// current_survey
		serverMsg.put("msgType", "select");
		serverMsg.put("query", "SELECT * From zrle.survey where Status='Active';");
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		answer = (ArrayList<String>) Main.client.getMessage();
		if (answer.size()!=0)
		{
		current_survey = new Survey();
		current_survey.setID(answer.get(0));
		current_survey.setDate(answer.get(1));
		current_survey.setQ1(answer.get(2));
		current_survey.setQ2(answer.get(3));
		current_survey.setQ3(answer.get(4));
		current_survey.setQ4(answer.get(5));
		current_survey.setQ5(answer.get(6));
		current_survey.setQ6(answer.get(7));
		current_survey.setA1(Double.parseDouble(answer.get(8)));
		current_survey.setA2(Double.parseDouble(answer.get(9)));
		current_survey.setA3(Double.parseDouble(answer.get(10)));
		current_survey.setA4(Double.parseDouble(answer.get(11)));
		current_survey.setA5(Double.parseDouble(answer.get(12)));
		current_survey.setA6(Double.parseDouble(answer.get(13)));
		current_survey.setNumOfParticipant(answer.get(16));

		q1.setText(current_survey.getQ1());
		q2.setText(current_survey.getQ2());
		q3.setText(current_survey.getQ3());
		q4.setText(current_survey.getQ4());
		q5.setText(current_survey.getQ5());
		q6.setText(current_survey.getQ6());

		/* set the radio buttons by groups */
		setRadioB();
		}
		else
		{
			 Optional<ButtonType> result = Main.showPopUp("INFORMATION", "Message", "There is No Active Survey", "Thank you!");

		      if (result.get() == ButtonType.OK)
		      {
		    	 try {
					Main.openMain("7", event);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		      	 
		      }
		}
	}
/**
 * set Toggle Groups
 */
	public void setRadioB() {
		a1 = new ToggleGroup();
		RadioButton button1 = R1_1;
		button1.setToggleGroup(a1);
		RadioButton button2 = R1_2;
		button2.setToggleGroup(a1);
		RadioButton button3 = R1_3;
		button3.setToggleGroup(a1);
		RadioButton button4 = R1_4;
		button4.setToggleGroup(a1);
		RadioButton button5 = R1_5;
		button5.setToggleGroup(a1);
		RadioButton button6 = R1_6;
		button6.setToggleGroup(a1);
		RadioButton button7 = R1_7;
		button7.setToggleGroup(a1);
		RadioButton button8 = R1_8;
		button8.setToggleGroup(a1);
		RadioButton button9 = R1_9;
		button9.setToggleGroup(a1);
		RadioButton button10 = R1_10;
		button10.setToggleGroup(a1);

		a2 = new ToggleGroup();
		RadioButton button2_1 = R2_1;
		button2_1.setToggleGroup(a2);
		RadioButton button2_2 = R2_2;
		button2_2.setToggleGroup(a2);
		RadioButton button2_3 = R2_3;
		button2_3.setToggleGroup(a2);
		RadioButton button2_4 = R2_4;
		button2_4.setToggleGroup(a2);
		RadioButton button2_5 = R2_5;
		button2_5.setToggleGroup(a2);
		RadioButton button2_6 = R2_6;
		button2_6.setToggleGroup(a2);
		RadioButton button2_7 = R2_7;
		button2_7.setToggleGroup(a2);
		RadioButton button2_8 = R2_8;
		button2_8.setToggleGroup(a2);
		RadioButton button2_9 = R2_9;
		button2_9.setToggleGroup(a2);
		RadioButton button2_10 = R2_10;
		button2_10.setToggleGroup(a2);

		a3 = new ToggleGroup();
		RadioButton button3_1 = R3_1;
		button3_1.setToggleGroup(a3);
		RadioButton button3_2 = R3_2;
		button3_2.setToggleGroup(a3);
		RadioButton button3_3 = R3_3;
		button3_3.setToggleGroup(a3);
		RadioButton button3_4 = R3_4;
		button3_4.setToggleGroup(a3);
		RadioButton button3_5 = R3_5;
		button3_5.setToggleGroup(a3);
		RadioButton button3_6 = R3_6;
		button3_6.setToggleGroup(a3);
		RadioButton button3_7 = R3_7;
		button3_7.setToggleGroup(a3);
		RadioButton button3_8 = R3_8;
		button3_8.setToggleGroup(a3);
		RadioButton button3_9 = R3_9;
		button3_9.setToggleGroup(a3);
		RadioButton button3_10 = R3_10;
		button3_10.setToggleGroup(a3);

		a4 = new ToggleGroup();
		RadioButton button4_1 = R4_1;
		button4_1.setToggleGroup(a4);
		RadioButton button4_2 = R4_2;
		button4_2.setToggleGroup(a4);
		RadioButton button4_3 = R4_3;
		button4_3.setToggleGroup(a4);
		RadioButton button4_4 = R4_4;
		button4_4.setToggleGroup(a4);
		RadioButton button4_5 = R4_5;
		button4_5.setToggleGroup(a4);
		RadioButton button4_6 = R4_6;
		button4_6.setToggleGroup(a4);
		RadioButton button4_7 = R4_7;
		button4_7.setToggleGroup(a4);
		RadioButton button4_8 = R4_8;
		button4_8.setToggleGroup(a4);
		RadioButton button4_9 = R4_9;
		button4_9.setToggleGroup(a4);
		RadioButton button4_10 = R4_10;
		button4_10.setToggleGroup(a4);

		a5 = new ToggleGroup();
		RadioButton button5_1 = R5_1;
		button5_1.setToggleGroup(a5);
		RadioButton button5_2 = R5_2;
		button5_2.setToggleGroup(a5);
		RadioButton button5_3 = R5_3;
		button5_3.setToggleGroup(a5);
		RadioButton button5_4 = R5_4;
		button5_4.setToggleGroup(a5);
		RadioButton button5_5 = R5_5;
		button5_5.setToggleGroup(a5);
		RadioButton button5_6 = R5_6;
		button5_6.setToggleGroup(a5);
		RadioButton button5_7 = R5_7;
		button5_7.setToggleGroup(a5);
		RadioButton button5_8 = R5_8;
		button5_8.setToggleGroup(a5);
		RadioButton button5_9 = R5_9;
		button5_9.setToggleGroup(a5);
		RadioButton button5_10 = R5_10;
		button5_10.setToggleGroup(a5);

		a6 = new ToggleGroup();
		RadioButton button6_1 = R6_1;
		button6_1.setToggleGroup(a6);
		RadioButton button6_2 = R6_2;
		button6_2.setToggleGroup(a6);
		RadioButton button6_3 = R6_3;
		button6_3.setToggleGroup(a6);
		RadioButton button6_4 = R6_4;
		button6_4.setToggleGroup(a6);
		RadioButton button6_5 = R6_5;
		button6_5.setToggleGroup(a6);
		RadioButton button6_6 = R6_6;
		button6_6.setToggleGroup(a6);
		RadioButton button6_7 = R6_7;
		button6_7.setToggleGroup(a6);
		RadioButton button6_8 = R6_8;
		button6_8.setToggleGroup(a6);
		RadioButton button6_9 = R6_9;
		button6_9.setToggleGroup(a6);
		RadioButton button6_10 = R6_10;
		button6_10.setToggleGroup(a6);

	}
/**
 * Reset Button After current survey saved
 */
	public void ResetButtons() {
		R1_1.setSelected(false);
		R1_2.setSelected(false);
		R1_3.setSelected(false);
		R1_4.setSelected(false);
		R1_5.setSelected(false);
		R1_6.setSelected(false);
		R1_7.setSelected(false);
		R1_8.setSelected(false);
		R1_9.setSelected(false);
		R1_10.setSelected(false);

		R2_1.setSelected(false);
		R2_2.setSelected(false);
		R2_3.setSelected(false);
		R2_4.setSelected(false);
		R2_5.setSelected(false);
		R2_6.setSelected(false);
		R2_7.setSelected(false);
		R2_8.setSelected(false);
		R2_9.setSelected(false);
		R2_10.setSelected(false);

		R3_1.setSelected(false);
		R3_2.setSelected(false);
		R3_3.setSelected(false);
		R3_4.setSelected(false);
		R3_5.setSelected(false);
		R3_6.setSelected(false);
		R3_7.setSelected(false);
		R3_8.setSelected(false);
		R3_9.setSelected(false);
		R3_9.setSelected(false);
		R3_10.setSelected(false);

		R4_1.setSelected(false);
		R4_2.setSelected(false);
		R4_3.setSelected(false);
		R4_4.setSelected(false);
		R4_5.setSelected(false);
		R4_6.setSelected(false);
		R4_7.setSelected(false);
		R4_8.setSelected(false);
		R4_9.setSelected(false);
		R4_10.setSelected(false);

		R5_1.setSelected(false);
		R5_2.setSelected(false);
		R5_3.setSelected(false);
		R5_4.setSelected(false);
		R5_5.setSelected(false);
		R5_6.setSelected(false);
		R5_7.setSelected(false);
		R5_8.setSelected(false);
		R5_9.setSelected(false);
		R5_10.setSelected(false);

		R6_1.setSelected(false);
		R6_2.setSelected(false);
		R6_3.setSelected(false);
		R6_4.setSelected(false);
		R6_5.setSelected(false);
		R6_6.setSelected(false);
		R6_7.setSelected(false);
		R6_8.setSelected(false);
		R6_9.setSelected(false);
		R6_10.setSelected(false);

	}
/**
 * initialize and get current questions
 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		get_survey_qustion(new ActionEvent());
		surveyCounter = Integer.parseInt(current_survey.getNumOfParticipant());
		avgq1 = current_survey.getA1();
		avgq2 = current_survey.getA2();
		avgq3 = current_survey.getA3();
		avgq4 = current_survey.getA4();
		avgq5 = current_survey.getA5();
		avgq6 = current_survey.getA6();
		Surveys = new ArrayList<Survey>();

	}

}
