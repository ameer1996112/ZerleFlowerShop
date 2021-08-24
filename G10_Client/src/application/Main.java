package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import Entity.BranchCatalog;
import Entity.Order;
import Entity.Product;
import Entity.ShopCatalog;
import Entity.User;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class Main extends Application {
	/** Label to display messages to user */
	@FXML
	private static Label guiMsg;

	// Creating a static client to pass to the controllers
	public static ClientConnection client;

	public static User user;

	public Order order;

	public  static ArrayList<Product> proudctList;
	public static BranchCatalog catalog;
	public static ShopCatalog tempShopCatalog;
	public static ArrayList<HashMap<String, String>> salelist = new ArrayList<HashMap<String, String>>();

	private static Stage primaryStage = new Stage();

	public static Stage getPrimaryStage() {
		return primaryStage;
	}
private static Stage window;
	// Creating a static root to pass to the controllers
	private static BorderPane root = new BorderPane();

	/**
	 * Just a root getter for the controllers to use
	 */
	public static BorderPane getRoot() {
		return root;
	}

	@Override
	public void start(Stage stage) throws Exception { // Show login screen on
														// start.

		Parent login = FXMLLoader.load(getClass().getResource("/FXML/Login.fxml"));
		Scene scene = new Scene(login);
		stage.setTitle("Login");
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * openMain()- Main function of the program
	 * 
	 * @param type
	 *            - indicate the role of the user.
	 * @throws IOException
	 */
	public static void openMain(String type,ActionEvent event) throws IOException {

		String fxml_url = "/FXML/";

		switch (type) {

		case "0":
			fxml_url += "AdministratorMenu.fxml";
			break;
		case "1":
			fxml_url += "GuestMainMenu.fxml";
			break;
		case "2":
			fxml_url += "CustomerMainMenu.fxml";
			break;
		case "3":
			fxml_url += "CompanyManagerMainMenu.fxml";
			break;
		case "4":
			fxml_url += "BranchManagerMainMenu.fxml";
			break;
		case "5":
			fxml_url += "BranchWorkerMainMenu.fxml";
			break;
		case "6":
			fxml_url += "CompanyWorkerMenu.fxml";
			break;
		case "7":
			fxml_url += "CompanyServiceExpertMenu.fxml";
			break;
		case "8":
			fxml_url += "CompanyServiceWorkerMenu.fxml";
			break;
			
		}
		OpenFxmlFile(event,fxml_url);
		
	}
	public static void OpenFxmlFile(ActionEvent event ,String  fxml_url) throws IOException
	{

		((Node) event.getSource()).getScene().getWindow().hide();
		Parent main = FXMLLoader.load(Main.class.getResource(fxml_url));
		main.getStylesheets().add("/FXML/application.css");
		Stage primaryStage = getPrimaryStage();
		Scene scene = new Scene(main);
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {

				try {
					logOut();
				} catch (Exception e) {
					System.exit(0);
				}
			}
		});
	}
	/**
	 * Back To Login Page
	 * @param event
	 */
	public static void logOutBackToMenu(ActionEvent event) {
		logOut();
		try {
			Parent login = FXMLLoader.load(Main.class.getResource("/FXML/Login.fxml"));
			Scene scene = new Scene(login);
			Stage primaryStage = getPrimaryStage();
			primaryStage.setTitle("Login");
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {

					try {
						System.exit(0);
					} catch (Exception e) {
						System.exit(0);
					}
				}
				});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * logOut() - log out function by the user. close the main scene and replace it
	 * with the login screen. Also sending log out message for updating the DB.
	 * 
	 * @return - 1: operation succeeded. 0: operation failed.
	 */
	public static void  logOut() {
		HashMap<String, String> msg = new HashMap<String,String>();
		msg.put("msgType", "update");
		msg.put("query", "UPDATE zrle.users SET LoginStatus='0' WHERE UserID='"+user.getUserID()+"';");
		System.out.println(msg.toString());
		
		try {
			client.sendMessageToServer(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}

		synchronized (client) {
			try {
				client.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		int answer = (int) client.getMessage();
		client = null;
	}
	public static Optional<ButtonType> showPopUp(String typeOfPopUpString, String title, String header, String content)
	{
		Alert alert = null;
		
		
		switch(typeOfPopUpString)
		{
		case "CONFIRMATION":
			alert = new Alert(AlertType.CONFIRMATION);
			break;
		case "INFORMATION":
			alert = new Alert(AlertType.INFORMATION);
			break;
		case "ERROR":
			alert = new Alert(AlertType.ERROR);
			break;
		}
		
		if(title!=null)
			alert.setTitle(title);
		if(header!=null)
			alert.setHeaderText(header);
		if(content!=null)
			alert.setContentText(content);
		
		Optional<ButtonType> result = alert.showAndWait();
		
		return result;
	}

	public static void main(String[] args) {
		launch(args);
	}
}