package Controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * this controller for the main menu gui in this class we define all the methods
 * of all buttons on the main menu
 * 
 * @author Ameer amer
 *
 */
public class MainController implements Initializable {
	@FXML
	private Pane pane;// param injected by fxml
	@FXML
	private ImageView image;// this for the background image

	/**
	 * we call this method when we click on view catalog button
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	public void viewCatalog(ActionEvent event) throws IOException {
		Main.OpenFxmlFile(event, "/FXML/ViewCatalog.fxml");
	}

	/**
	 * we call this method when we click on catalog order button
	 * 
	 * @param event
	 * @throws IOException
	 */
	public void openCatalogOrder(ActionEvent event) throws IOException {
		if (Main.user.getPaymentAccounts().size() != 0)
			Main.OpenFxmlFile(event, "/FXML/CatalogOrder.fxml");
		else
			Main.showPopUp("ERROR", "Error dialog", null, "You must have an approved payment account!");

	}

	/**
	 * this method for openinning a new payment account
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void fillPaymentAccDeatils(ActionEvent event) throws IOException {
		if (Main.user.getPaymentAccounts().size() == 0) {
			Main.OpenFxmlFile(event, "/FXML/FillPayment.fxml");
		} else {
			Main.showPopUp("ERROR", "Error dialog", null, "you already have one!");
		}
	}

	/**
	 * we call this method when we click on the custom order button
	 * 
	 * @param event
	 * @throws IOException
	 */
	public void openCustomOrder(ActionEvent event) throws IOException {
		if (Main.user.getPaymentAccounts().size() != 0)
			Main.OpenFxmlFile(event, "/FXML/CustomOrder.fxml");
		else
			Main.showPopUp("ERROR", "Error dialog", null, "You must have an approved payment account!");

	}

	/**
	 * this method perform logout
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	public void Logout(ActionEvent event) throws IOException {
		HashMap<String, String> msg = new HashMap<String, String>();
		msg.put("msgType", "update");
		msg.put("query", "UPDATE zrle.users SET LoginStatus='0' WHERE UserID='" + Main.user.getUserID() + "';");
		System.out.println(msg.toString());

		try {
			Main.client.sendMessageToServer(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}

		synchronized (Main.client) {
			try {
				Main.client.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		int answer = (int) Main.client.getMessage();
		Main.client = null;
		Main.OpenFxmlFile(event, "/FXML/Login.fxml");

	}

	/**
	 * this method open the cancel order window
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void openCanelOrder(ActionEvent event) throws IOException {
		Main.OpenFxmlFile(event, "/FXML/CancelOrder.fxml");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Image image1 = new Image(getClass().getResource("/images/bg2.jpg").toString());
		image.setImage(image1);
		Text text = new Text("Welcome " + Main.user.getUserID() + " to zrleMarket!");
		text.setWrappingWidth(300);
		text.setFont(Font.font("Josefin Sans", FontWeight.BOLD, 19));
		text.setLayoutX(22);
		text.setLayoutY(17);
		text.setFill(Color.WHITESMOKE);
		pane.getChildren().add(text);
		pane.setLayoutX(205);

	}
}
