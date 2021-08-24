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

import Entity.ChangeUserDetails;
import Entity.PaymentAccount;
import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert.AlertType;
/**
 * 
 * @author jeries
 *
 */
public class ApprovPaymentController implements Initializable {

	@FXML
	ComboBox<String> id_combo;
	@FXML
	ComboBox<String> subscription_combo;
	@FXML
	ComboBox<String> status_payment_combo;
	@FXML
	Button back;
	@FXML
	Button save;
	private int selectedIndexInListOfUsers;
	private int selectedIndexInListOfPayAccStatus;
	private int selectedIndexInListOfSub;
	private ArrayList<PaymentAccount> acclist = new ArrayList<PaymentAccount>();

	private HashMap<String, String> serverMsg = new HashMap<String, String>();
	private ObservableList<String> list;
/**
 * back to Menu
 * @param event
 */
	public void BackToMenu(ActionEvent event) {
		try {
			Main.OpenFxmlFile(event, "/FXML/BranchManagerMenu.fxml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
/**
 * initialize get accounts are still not Approved by branch manager 
 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		subscription_combo.setDisable(true);
		status_payment_combo.setDisable(true);
		getUserBranchID();
		serverMsg.put("msgType", "select");
		serverMsg.put("query",
				"SELECT UsersAccountID,BranchID,Subscription,PaymentAccountStatus from paymentaccount where "
						+ "BranchID='" + Main.user.getBranch() + "' and PaymentAccountStatus='" + 0 + "';");
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
		ArrayList<String> id = new ArrayList<String>();
		for (int i = 0; i < answer.size(); i += 4) {
			acclist.add(new PaymentAccount(Main.user.getUserID(), Integer.parseInt(answer.get(i + 1)), answer.get(i + 2),
					Integer.parseInt(answer.get(i + 3))));
			id.add(answer.get(i));

		}
	list = FXCollections.observableArrayList(id);
	id_combo.setItems(list);

	ArrayList<String> Pastatus = new ArrayList<String>();
	Pastatus.add("Not Approved");
	Pastatus.add("Approved");
	list = FXCollections.observableArrayList(Pastatus);
	status_payment_combo.setItems(list);

	ArrayList<String> subscription = new ArrayList<String>();
	subscription.add("Per Order");
	subscription.add("Month");
	subscription.add("Year");
	list = FXCollections.observableArrayList(subscription);
	subscription_combo.setItems(list);	
	if(acclist.isEmpty())
	Main.showPopUp("INFORMATION","Infromation dialog",null,"There no payment account that pending for approvement!");
	}
	/**
	 * Set informations for selected User in ComboxBox
	 * @param event
	 */
	public void check_SelecetdID(ActionEvent event) {
		selectedIndexInListOfUsers = id_combo.getSelectionModel().getSelectedIndex();

		status_payment_combo.setValue("Not Approved");
		status_payment_combo.setDisable(false);
		if (acclist.get(selectedIndexInListOfUsers).getSubscriptionType().equals("0"))
			subscription_combo.setValue("Per Order");
		else if (acclist.get(selectedIndexInListOfUsers).getSubscriptionType().equals("1"))
			subscription_combo.setValue("Month");
		else
			subscription_combo.setValue("year");
		subscription_combo.setDisable(false);
	}
/**
 * set Payment account status
 * @param event
 */
	public void check_selectedPayAccStatus(ActionEvent event) {
		selectedIndexInListOfPayAccStatus = status_payment_combo.getSelectionModel().getSelectedIndex();
	}
/**
 * set Subscription Type
 * @param event
 */
	public void check_selectedSubscription(ActionEvent event) {
		selectedIndexInListOfSub = subscription_combo.getSelectionModel().getSelectedIndex();
	}
/**
 * get user BranchID to show the Accounts are requested for his Branch 
 */
	public void getUserBranchID() {
		serverMsg.put("msgType", "select");
		serverMsg.put("query", "SELECT BranchID from users where userID='" + Main.user.getUserID() + "';");
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
	}
/**
 * Save payment Account to DataBase 
 * @param event
 */
	public void SaveDetails(ActionEvent event) {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String data = df.format(new Date());
		serverMsg.put("msgType", "update");
		serverMsg.put("query",
				"update zrle.paymentaccount set Paymentaccountstatus='" + selectedIndexInListOfPayAccStatus
						+ "',Subscription='" + selectedIndexInListOfSub + "' , DateApproved='"+data+"' where  UsersAccountID='"
						+ acclist.get(selectedIndexInListOfPayAccStatus-1).getUserID() + "' and BranchID='" + Main.user.getBranch() + "';");
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
			Main.showPopUp("CONFIRMATION", "Confirm", "User Was Updated Succesully", "");
		else
			Main.showPopUp("Error", "Proplem !", "There Was Prolem With Updating User", " Try Againe Later");
	
	}

}
