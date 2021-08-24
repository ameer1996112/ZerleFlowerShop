package Controller;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

import application.Main;

/**
 * 
 * @author jeries
 *
 */
public class BranchManagerMenuController {
	@FXML 
	private Button btnViewCata;
	@FXML 
	private Button btnApprov;
	
	public void ViewCatalog(ActionEvent Event) {
		System.out.println("View Catalog");
	}
/**
 * Open Menu Manager to Approve Accounts 
 * @param event
 */
	public void ApprovAccounts(ActionEvent event) {
		try {

			((Node) event.getSource()).getScene().getWindow().hide();
			Stage primaryStage = Main.getPrimaryStage();
			Parent root = FXMLLoader.load(getClass().getResource("/FXML/ApprovePaymentAccount.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Approv Payment Accounts");
			primaryStage.setResizable(false);
			primaryStage.show();
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {

					try {
						Main.logOut();

					} catch (Exception e) {
						System.exit(0);
					}
				}
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

}
