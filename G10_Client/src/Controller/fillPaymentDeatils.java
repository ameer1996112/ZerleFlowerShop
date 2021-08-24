package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class fillPaymentDeatils extends MainController implements Initializable {
	@FXML
	private ComboBox<String> subscription_combo;
	@FXML
	private TextField credit;
	private HashMap<String, String> msgServer = new HashMap<String, String>();
	private Integer Intanswer;
	private final ObservableList<String> options = FXCollections.observableArrayList("Monthly", "Yearly");

	@FXML
	void SaveDetails(ActionEvent event) {
		int sub = subscription_combo.getSelectionModel().getSelectedIndex();
		String creditnum = credit.getText();
		msgServer.put("msgServer", "insert");
		msgServer.put("query",
				"INSERT INTO paymentaccount (UsersAccountID,BranchID,Subscription,Subscription,PaymentAccountStatus,BuyingInSub)VALUES('"
						+ Main.user.getUserID() + "','" + Main.user.getBranch() + "'," + sub + "',0,0)");
		try {
			Main.client.sendMessageToServer(msgServer);
			synchronized (Main.client) {
				Main.client.wait();
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		Intanswer = (Integer) Main.client.getMessage();
		if (Intanswer == 0) {
			Main.showPopUp("ERROR", "Erorr dialog", null,
					"some error ocuured while adding payment account in database");
		} else {
			Main.showPopUp("ERROR", "Erorr dialog", null, "Your payment account added in the system!");
		}

	}

	/**
	 * we call this method when we click on view catalog button
	 * 
	 * @param event
	 * @throws IOException
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		subscription_combo.setItems(options);
	}

}
