package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.TreeMap;

import Entity.Branch;
import Entity.ChangeUserDetails;
//import Entity.UserDetails;
//import action.Msg;
import application.Main;
import javafx.application.Platform;
//import gui.Login_win;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * 
 * @author jeries
 *
 */
public class UserDetailsController implements Initializable {
	ArrayList<String> answer = new ArrayList<String>();
	HashMap<String, String> serverMsg = new HashMap<String, String>();

	@FXML
	ComboBox<String> type_combo;
	@FXML
	ComboBox<String> status_combo;
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
	@FXML
	Label invalid_detailsL_ID;

	ObservableList<String> list;

	public int selectedIndexInListOfTypes;
	public int selectedIndexInListOfStatus;
	public int selectedIndexInListOfBranch;
	public int selectedIndexInListOfUsers;
	public int selectedIndexInListOfPayAccStatus;
	public int selectedIndexInListOfSub;
	private static ArrayList<ChangeUserDetails> userlist = new ArrayList<ChangeUserDetails>();
	private static ArrayList<Branch> branchlist = new ArrayList<Branch>();

/**
 * Initialize ComboBox's and Gets Data From DataBase
 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		/* turn off the error label */
		invalid_detailsL_ID.setVisible(false);
		/* set combboxe to disable except id */
		type_combo.setDisable(true);
		status_combo.setDisable(true);
		subscription_combo.setDisable(true);
		status_payment_combo.setDisable(true);
		
		serverMsg.put("msgType", "select");
		serverMsg.put("query", "SELECT UserID,UserType,UserStatus,BranchID from users");
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
		ArrayList<String> id = new ArrayList<String>();

		int jump = 0;
		for (int i = 0; i < answer.size(); i += 4) {
			userlist.add(new ChangeUserDetails(answer.get(i), answer.get(i + 1), answer.get(i + 2)));
		}

		for (ChangeUserDetails s : userlist) {
			id.add(s.getUserID());

		}

		list = FXCollections.observableArrayList(id);
		id_combo.setItems(list);

		ArrayList<String> usertype = new ArrayList<String>();
		usertype.add("Adminstrator - 0");
		usertype.add("Guest - 1");
		usertype.add("Customer - 2");
		usertype.add("CompanyManager - 3");
		usertype.add("BranchManager - 4");
		usertype.add("BranchWorker - 5");
		usertype.add("CompanyWorker - 6");
		usertype.add("CompanyServiceExpert - 7");
		usertype.add("CompanyServiceWorker - 8");
		list = FXCollections.observableArrayList(usertype);
		type_combo.setItems(list);

		ArrayList<String> status = new ArrayList<String>();
		status.add("Active");
		status.add("Block");
		list = FXCollections.observableArrayList(status);
		status_combo.setItems(list);

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
		serverMsg.put("msgType", "select");
		serverMsg.put("query", "SELECT  BranchID,BName,ManagerID from branch");
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
		for (int i = 0; i < answer.size(); i += 3) {
			branchlist.add(new Branch(answer.get(i), answer.get(i + 1), answer.get(i + 2)));
		}
		ArrayList<String> store = new ArrayList<String>();
		for (Branch s : branchlist) {
			store.add(s.getBranchName());
		}

		list = FXCollections.observableArrayList(store);
		//branch_combo.setItems(list);

	}

	public void SubmitChange(ActionEvent event) {
		System.out.println("make query to change");

	}

	public void BackToMenu(ActionEvent event) {

		try {
			Main.openMain("0", event);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	public void check_selectedUserType(ActionEvent event) {

		selectedIndexInListOfTypes = type_combo.getSelectionModel().getSelectedIndex();
		System.out.println(selectedIndexInListOfTypes);
	}

	public void check_selectedUserStatus(ActionEvent event) {
		selectedIndexInListOfStatus = status_combo.getSelectionModel().getSelectedIndex();
		// System.out.println(selectedIndexInListOfStatus);
	}

	public void check_selectedPayAccStatus(ActionEvent event) {
		selectedIndexInListOfPayAccStatus = status_payment_combo.getSelectionModel().getSelectedIndex();
		System.out.println(selectedIndexInListOfPayAccStatus);
	}

	public void check_selectedSubscription(ActionEvent event) {
		selectedIndexInListOfSub = subscription_combo.getSelectionModel().getSelectedIndex();
		System.out.println(selectedIndexInListOfSub);
	}

	public void check_SelecetdID(ActionEvent event) {
		selectedIndexInListOfUsers = id_combo.getSelectionModel().getSelectedIndex();
		status_payment_combo.setDisable(true);
		subscription_combo.setDisable(true);


		type_combo.setDisable(false);
		type_combo.setValue(userlist.get(selectedIndexInListOfUsers).getUserType());

		status_combo.setDisable(false);
		if (Integer.parseInt(userlist.get(selectedIndexInListOfUsers).getUserStatus()) == 0) {
			if (Integer.parseInt(userlist.get(selectedIndexInListOfUsers).getUserType()) == 2) {
				getpayaccountdetails();

			}
			status_combo.setValue("Active");
		}

		else {
			status_combo.setValue("Blocked");
			status_payment_combo.setDisable(true);
			subscription_combo.setDisable(true);

			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					Main.showPopUp("ERROR", "Client Error", "The User Is Blocked", "");
					return;
				}
			});

		}

	}

	public void UpdateDetails(ActionEvent event) {

		int flag1, flag2 = 1;
		serverMsg.put("msgType", "update");
		serverMsg.put("query",
				" UPDATE zrle.users SET UserType='" + selectedIndexInListOfTypes + "',UserStatus='"
						+ selectedIndexInListOfStatus + "',BranchID='" + selectedIndexInListOfBranch + "' WHERE UserID='"
						+ userlist.get(selectedIndexInListOfUsers).getUserID() + "';");
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

			flag1 = (int) Main.client.getMessage();
			if (Integer.parseInt(userlist.get(selectedIndexInListOfUsers).getUserType()) == 2)
				flag2 = updatedetailstoCustomer();
			if (flag1 == 1 & flag2 == 1)
				Main.showPopUp("CONFIRMATION", "Updated", "The Details Was Updated Succ", "");
			else
				Main.showPopUp("Error", "Error", "There Was Prolem With Updating User", " Try Againe Later");

		}

	}

	public int updatedetailstoCustomer() {

		serverMsg.put("msgType", "update");
		serverMsg.put("query",
				" UPDATE zrle.paymentaccount SET Subscription='" + selectedIndexInListOfSub + " ',PaymentAccountStatus='"
						+ selectedIndexInListOfPayAccStatus + " ' WHERE UsersAccountID='"
						+ userlist.get(selectedIndexInListOfUsers).getUserID() + "' ;");
		try {
			Main.client.sendMessageToServer(serverMsg);
		} catch (Exception exp) {
			exp.printStackTrace();
			return 0;
		}
		synchronized (Main.client) {
			try {
				Main.client.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int flag2 = (int) Main.client.getMessage();
			return flag2;
		}
	}

	public void getpayaccountdetails() {
		serverMsg.put("msgType", "select");
		serverMsg.put("query", "SELECT  UsersAccountID,PaymentAccountStatus,Subscription from paymentaccount");
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

		for (int i = 0; i < answer.size(); i += 3) {
			for (int j = 0; j < userlist.size(); j++) {
				if (answer.get(i).equals(userlist.get(j).getUserID())) {
					userlist.get(j).setPaymentAccountStatus(Integer.parseInt(answer.get(i + 1)));
					userlist.get(j).setSubscription(Integer.parseInt(answer.get(i + 2)));
					break;
				}
			}

		}

		System.out.println(userlist.get(selectedIndexInListOfUsers).getPaymentAccountStatus());
		System.out.println(userlist.get(selectedIndexInListOfUsers).getSubscription());
		if (userlist.get(selectedIndexInListOfUsers).getPaymentAccountStatus() == 1)
			status_payment_combo.setValue("Approved");
		else
			status_payment_combo.setValue("Not Approved");

		status_payment_combo.setDisable(false);

		if (userlist.get(selectedIndexInListOfUsers).getSubscription() == 0)
			subscription_combo.setValue("Per Order");
		else if (userlist.get(selectedIndexInListOfUsers).getSubscription() == 1)
			subscription_combo.setValue("Month");
		else
			subscription_combo.setValue("year");

		subscription_combo.setDisable(false);

	}

}
