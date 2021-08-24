package Controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

import application.Main;
/**
 * 
 * @author jeries
 *
 */
public class AdminController {
	@FXML 
	private Button btnViewCata;
	@FXML 
	private Button btnChUsDe;
	
	public void ViewCatalog(ActionEvent Event) {
	}
/**
 * Open User Details Menu To Change 
 * @param event
 */
	public void ChangeUserDetails(ActionEvent event) {
		try {

			((Node) event.getSource()).getScene().getWindow().hide();
			Stage primaryStage = Main.getPrimaryStage();
			Parent root = FXMLLoader.load(getClass().getResource("/FXML/UserDetails.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Change User Detials");
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
