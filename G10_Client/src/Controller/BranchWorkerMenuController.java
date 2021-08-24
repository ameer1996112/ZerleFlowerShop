package Controller;

import java.io.IOException;

import application.Main;
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
/**
 * 
 * @author jeries
 *
 */
public class BranchWorkerMenuController {
	@FXML 
	Button btnFillSurvey;
	/**
	 * open Menu to Fill Surverys
	 * @param event
	 */
	public void FillSurvey(ActionEvent event)
	{
		try {

			((Node) event.getSource()).getScene().getWindow().hide();
			Stage primaryStage = Main.getPrimaryStage();
			Parent root = FXMLLoader.load(getClass().getResource("/FXML/FillSurveyDetails.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Fill Survey");
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
