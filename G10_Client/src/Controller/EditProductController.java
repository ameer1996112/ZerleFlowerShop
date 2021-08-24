package Controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Entity.Product;
import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class EditProductController implements Initializable {// cheack image
																// chooser!!

	@FXML
	private TextField pPrice;

	@FXML
	private TextField pColor;

	@FXML
	private TextField pType;

	@FXML
	private TextField pSale;

	@FXML
	private TextField imgChooser;

	private Product p = new Product();
	CatalogController cat = new CatalogController();
	private HashMap<String, Object> msgServer = new HashMap<String, Object>();

	private Integer Intanswer;

	// try to order the dialogs by adding msg to the function!! &opacity of the
	// buttons with chicking validations
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) { // fill info by loop
		// TODO Auto-generated method stub
		int id = cat.getFocusedItemID(); // get id of the selected item;
		int index = 0;
		ObservableList<Product> list = FXCollections.observableArrayList(cat.getList());

		for (index = 0; index < list.size(); index++) {

			if (list.get(index).getProductID().equals(Integer.toString(id))) {

				pType.setText(list.get(index).getType());
				pType.setEditable(true);
				pColor.setText(list.get(index).getColor());
				pColor.setEditable(true);
				pPrice.setText(String.valueOf(list.get(index).getPrice()));
				pPrice.setEditable(true);
				pSale.setText(String.valueOf(list.get(index).getSale()));
				pSale.setEditable(true);
				break;
			}
		}
	}

	public void chooseBtn(MouseEvent event) {

		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Images(*.*)", "*.*"),
				new FileChooser.ExtensionFilter("JPG(*.jpg)", "*.jpg"),
				new FileChooser.ExtensionFilter("PNG(*.png)", "*.png"));
		File selectedFile = fc.showOpenDialog(null);
		if (selectedFile != null) {

			imgChooser.setText(selectedFile.getAbsolutePath());
			Image img = new Image(selectedFile.toURI().toString(), 250, 200, true, true);
			p.setImg(img);

		} else {

			WarrningWin();
		}
	}

	public void savebtn(MouseEvent event) {

		if (FieldValidation() && CheckNumericFields()) {
			boolean flag = confirmationWin("");
			int index = cat.getSelectedItem(); // get the num of the selected
												// item
			if (flag) {

				double sale;
				if (pSale.getText().isEmpty()) {
					sale = 0;
				} else {
					sale = Double.parseDouble(pSale.getText());
				}

				p.setPrice(pPrice.getText());
				cat.getList().get(index).setPrice(pPrice.getText());
				// p.setSale(sale);
				p.setColor(pColor.getText());
				p.setProductID(cat.getList().get(cat.getSelectedItem()).getProductID());
				cat.getList().get(index).setColor(p.getColor());
				p.setType(pType.getText());
				p.setSale(Double.parseDouble(pSale.getText()));
				cat.getList().get(index).setType(p.getType());

				if (!imgChooser.getText().isEmpty()) {
					cat.getList().get(index).setImg(p.getImg());
				}
				msgServer.put("msgType", "update");
				msgServer.put("query", "UPDATE zrle.product SET Type='" + p.getType() + "',Color='" + p.getColor()
						+ "',Price='" + p.getPrice() + "',SalePrice='"+p.getSale()+"' Where ProductID='" + p.getProductID()+"'");
				System.out.println("UPDATE zrle.product SET Type='" + p.getType() + "',Color='" + p.getColor()
						+ "',Price='" + p.getPrice() + "' Where ProductID='" + p.getProductID()+"'");
				try {
					Main.client.sendMessageToServer(msgServer);
					synchronized (Main.client) {
						Main.client.wait();
					}
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}

				Intanswer = (Integer) Main.client.getMessage();
				if (Intanswer == null) {
				}
				for( int i=0;i<Main.proudctList.size();i++)
				{
					if(p.getProductID().equals(Main.proudctList.get(i).getProductID())){
						if(p.getColor()!=null)
						Main.proudctList.get(i).setColor(p.getColor());
						else if(p.getType()!=null)
						Main.proudctList.get(i).setType(p.getType());
						else if(p.getProductName()!=null)
						Main.proudctList.get(i).setProductName(p.getProductName());
						else if(p.getPrice()!=null)
						Main.proudctList.get(i).setPrice(p.getPrice());
						else if (p.getSale()!=null)
							Main.proudctList.get(i).setSale((p.getSale()));
					}
				}
			}

		}
	}

	public void backbtn(MouseEvent event) throws IOException {
		if (CheckIfPressSave()) {
			try {
				Parent root1;
				root1 = FXMLLoader.load(getClass().getResource("/FXML/Catalog.fxml"));
				// Parent root1=(Parent) fx.load();
				Scene scene = new Scene(root1);
				scene.getStylesheets().add(getClass().getResource("/FXML/application.css").toExternalForm());
				Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				stage.setTitle("Zr-Le Update Product");
				stage.setScene(scene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Can not back to the previouse page !!");
				ErrorWin("you can not return to the prev page");
			}
		} else {
			if (confirmationWin("you forget to save the changes!!")) {
				try {
					Parent root1;
					root1 = FXMLLoader.load(getClass().getResource("/FXML/Catalog.fxml"));
					// Parent root1=(Parent) fx.load();
					Scene scene = new Scene(root1);
					scene.getStylesheets().add(getClass().getResource("/FXML/application.css").toExternalForm());
					Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
					stage.setTitle("Zr-Le Update Product");
					stage.setScene(scene);
					stage.show();
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("Can not back to the previouse page !!");
					ErrorWin("you can not return to the prev page");
				}
			}
		}

	}

	public void ErrorWin(String msg) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error Dialog");
		alert.setHeaderText("Look, an Error Dialog");
		alert.setContentText("Ooops, there was an error!, " + msg + " !.");
		alert.showAndWait();
	}

	public Boolean confirmationWin(String msg) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog To Save Changes");
		alert.setHeaderText("Confirmation To Save Changes Dialog");
		alert.setContentText(msg + " Are you sure with this?");
		// alert.showAndWait();
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			// ... user chose OK
			return true;
		} else {
			// ... user chose CANCEL or closed the dialog
			return false;
		}
	}

	public void WarrningWin() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Warning None Photo Dialog");
		alert.setHeaderText("Look, a Warning Dialog");
		alert.setContentText("Careful!, you didn't upload a picture!.");
		alert.showAndWait();
	}

	public boolean NumberValidation(String str) {
		Pattern p = Pattern.compile("[0.0-999.99]+");
		Matcher m = p.matcher(str);
		if (m.find() && m.group().equals(str)) {
			return true;
		} else {

			return false;
		}
	}

	public boolean FieldValidation() {
		if (pType.getText().isEmpty() || pColor.getText().isEmpty()) {
			ErrorWin("please fill all of the required fields!!\n ***(who marked with ' * ' )***");
			return false;
		} else {
			return true;
		}
	}

	public boolean SaleValidation(double sale) {
		if (sale < 0.0 || sale > 96.0) {
			ErrorWin("please enter a Valid Sale less than 96%");
			return false;
		} else {
			return true;
		}
	}

	public boolean PriceValidation() {
		if (Double.parseDouble(pPrice.getText()) <= 0.0) {
			ErrorWin("please enter a Valid Price");
			return false;
		} else {
			return true;
		}
	}

	public boolean CheckNumericFields() {
		boolean flag1, flag2;
		double sale;
		flag1 = NumberValidation(pPrice.getText());
		if (pSale.getText().isEmpty()) {
			flag2 = false;
			sale = 0.0;
		} else {
			flag2 = NumberValidation(pSale.getText());

		}
		if (flag2) {
			sale = Double.parseDouble(pSale.getText());
			flag2 = SaleValidation(sale);
		}
		if (flag1) {
			flag1 = PriceValidation();
		}
		if (flag1 && flag2) {
			return true;
		} else {
			ErrorWin("Please Enter a Valid Number For Price or Sale");
			return false;
		}
	}

	public boolean CheckIfPressSave() {
		// function that know if the user changed or edit info but forget to
		// press save
		double sale = 0, price = 0;
		String type, color;
		Image img;
		Product p = cat.getList().get(cat.getSelectedItem());
		if (pSale.getText().isEmpty()) {
			sale = 0.0;
		} else {
			if (NumberValidation(pSale.getText())) {
				sale = Double.parseDouble(pSale.getText());
			}
		}
		if (NumberValidation(pPrice.getText())) {
			price = Double.parseDouble(pPrice.getText());
		}
		type = pType.getText();
		color = pColor.getText();

		if (p.getType().equals(type) && p.getColor().equals(color) && // do not
																		// forget
																		// set
																		// new
																		// image
																		// in
																		// save
																		// and
																		// compare
																		// new
																		// image
																		// too
				price == Double.parseDouble(p.getPrice()) && sale == p.getSale()) {
			return true;
		} else {
			return false;
		}

	}

}
