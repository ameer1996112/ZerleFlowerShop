package Controller;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import Entity.Complaint;
import Entity.PaymentAccount;
import Entity.Refound;
import application.Main;

public class CompanyServiceWorkerComplaintController implements Initializable {

	public TextField txtCustomerID, txtRefoundAmount;
	public TextArea txtAreaCustomerComplaine, txtAreaWorkerResponse;
	public Label lblRefund, lblResponse;
	public ComboBox<String> comboComplaine, comboReturnType;
	private HashMap<String, String> serverMsg = new HashMap<String, String>();
	private ObservableList<String> list;
	private int selectedIndexInListOfComplaine;
	private int selectedIndexInListOfRefoundType;
	private String RefoundType;
	private Boolean flag = false;
	public ArrayList<Complaint> ComplaineList = new ArrayList<Complaint>();

	public void FillInfo(ActionEvent event) {
		selectedIndexInListOfComplaine = comboComplaine.getSelectionModel().getSelectedIndex();
		txtCustomerID.setText(ComplaineList.get(selectedIndexInListOfComplaine).getUserID());
		txtAreaCustomerComplaine.setText(ComplaineList.get(selectedIndexInListOfComplaine).getComplaintText());
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeDetials();
		String CompToAnswerNow = " Complaines ID  Who Passed 24 Hours : ";
		for (Complaint c : ComplaineList) {
			serverMsg.put("msgType", "CheckComplaines");
			serverMsg.put("Time", c.getCompTime());
			serverMsg.put("Date", c.getCompDate());
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
			Boolean answer = (Boolean) Main.client.getMessage();
			if (answer) {
				CompToAnswerNow += ", " + c.getComplaintID();
			}

		}
		Main.showPopUp("INFORMATION", "Complaines Have To Answer Now", "", CompToAnswerNow);
		serverMsg.clear();
		ArrayList<String> ReturnType = new ArrayList<String>();
		ReturnType.add("Divelopment Delay");
		ReturnType.add("Bad Quality of Product");
		ReturnType.add("Maltreatment");
		ReturnType.add("UnderEstimated");
		ReturnType.add("Wrong Delivry");
		ReturnType.add("Missed Products");
		list = FXCollections.observableArrayList(ReturnType);
		comboReturnType.setItems(list);

	}

	public void Send(ActionEvent event) {
		if (txtAreaWorkerResponse.getText().isEmpty()) {
			lblResponse.setTextFill(Color.RED);
			Main.showPopUp("INFORMATION", "Fill Response", "Response", "");
			//
		} else if (txtRefoundAmount.getText().isEmpty()) {
			if (!flag) {
				serverMsg.put("msgType", "insert");
				serverMsg.put("query",
						" update zrle.complaine set ComplaineStatus='1' ,ComplaineAnswer='"
								+ txtAreaWorkerResponse.getText() + "' where ComplaineID="
								+ ComplaineList.get(selectedIndexInListOfComplaine).getComplaintID() + ";");
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
				int answer = (int) Main.client.getMessage();
				if (answer == 1) {
					Main.showPopUp("INFORMATION", "Complaine Submited", "Complaine Succsefully Updated", "");
				}
			}
		} else {
			serverMsg.clear();
			String str = new DecimalFormat("##.#").format(Double.parseDouble(txtRefoundAmount.getText()));
			serverMsg.put("msgType", "insertRefound");
			serverMsg.put("query",
					"insert into zrle.refound (UserID,Description,ReturnType,BalanceReturned) " + "Values('"
							+ ComplaineList.get(selectedIndexInListOfComplaine).getUserID() + "','" + RefoundType
							+ "','Complaine','" + str + "')");

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
			String refoundID = (String) Main.client.getMessage();
			
			serverMsg.clear();
			serverMsg.put("msgType", "insert");
			serverMsg.put("query",
					" update zrle.complaine set ComplaineStatus='1' ,ComplaineAnswer='"
							+ txtAreaWorkerResponse.getText() + "', RefoundID='"+refoundID+"' where ComplaineID="
							+ ComplaineList.get(selectedIndexInListOfComplaine).getComplaintID() + ";");
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
			int answer2 = (int) Main.client.getMessage();
			if (answer2 == 1) {
				Main.showPopUp("INFORMATION", "Complaine Submited", "Complaine Succsefully Updated", "");
			}
		}

	}

	public void logout(ActionEvent event) throws IOException {
		Main.logOutBackToMenu(event);
	}

	public void initializeDetials() {
		serverMsg.put("msgType", "select");
		serverMsg.put("query",
				" SELECT ComplaineID,ComplaineText,UserID,Date,Time,ComplaineTopic FROM zrle.complaine where ComplaineStatus='0';");
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
		ArrayList<String> answer = new ArrayList<String>();
		answer = (ArrayList<String>) Main.client.getMessage();
		ArrayList<String> complaintid = new ArrayList<String>();
		for (int i = 0; i < answer.size(); i += 6) {
			ComplaineList.add(new Complaint(Integer.parseInt(answer.get(i)), answer.get(i + 1), answer.get(i + 2),
					answer.get(i + 3), answer.get(i + 4), answer.get(i + 5)));
			complaintid.add(answer.get(i));
		}
		list = FXCollections.observableArrayList(complaintid);
		comboComplaine.setItems(list);
		txtCustomerID.setEditable(false);
		txtAreaCustomerComplaine.setEditable(false);
		txtRefoundAmount.setEditable(true);
		txtAreaWorkerResponse.setEditable(true);

	}

	public void SetValueForRefoundType(ActionEvent event) {
		// fill type for refoud
		selectedIndexInListOfRefoundType = comboComplaine.getSelectionModel().getSelectedIndex();
		RefoundType = comboReturnType.getValue();
		System.out.println(RefoundType);
		flag = true;
	}

}
