package Controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class CompanyComplaintController implements Initializable {

	    @FXML
	    private TextField userName;

	    @FXML
	    private TextField WorkerName;

	    @FXML
	    private TextField orderId;

	    @FXML
	    private ComboBox<String> comboBox;

	    @FXML
	    private TextArea response;
	    
	    ObservableList <String> comp=FXCollections.observableArrayList();
	    
	    public void FillInfo() {
	    	//fill from the database
	    }
	    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		comp.add("10%");
		comp.add("20%");
		comp.add("25%");
		comp.add("30%");
		comp.add("50%");
		comp.add("75%");
		comp.add("100%");
		comboBox.setItems(comp);
		
	}
	
	public void SendBtn(MouseEvent event) {
		
	}
	
	public void BackBtn(MouseEvent event) {
		
	}
	
	public boolean CheckOrderandUserName() {
		return true;
	}
	

}
