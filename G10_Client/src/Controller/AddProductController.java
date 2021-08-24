package Controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Entity.Product;
import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AddProductController implements Initializable {

	private CatalogController cat = new CatalogController();

	@FXML
	private TextField pId;

	@FXML
	private TextField pName;

	@FXML
	private TextField pType;

	@FXML
	private TextField pPrice;

	@FXML
	private TextField pColor;

	@FXML
	private TextField pSale;

	@FXML
	private Label imglabel;

	@FXML
	private ImageView img;

	private Integer Intanswer;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		img.setImage(new Image("/images/Photo_Add-RoundedBlack-512.png"));
	}

	public void UploadPhoto(MouseEvent event) {

		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Images(*.*)", "*.*"),
				new FileChooser.ExtensionFilter("JPG(*.jpg)", "*.jpg"),
				new FileChooser.ExtensionFilter("PNG(*.png)", "*.png"));
		File selectedFile = fc.showOpenDialog(null);
		if (selectedFile != null) {

			imglabel.setText(selectedFile.getAbsolutePath());
			Image img1 = new Image(selectedFile.toURI().toString(), 250, 200, true, true);
			cat.getList().get(cat.getSelectedItem()).setImg(img1);
			img.setImage(img1);

		} else {

			WarrningWin();
		}
	}

	public void AddBtn(MouseEvent event) throws InterruptedException, IOException {
		if (FieldValidation() && IDValidation() && CheckNumericFields()) {
			if (CheckExistingProduct(Integer.parseInt(pId.getText()), pName.getText())) {
				HashMap<String, Object> msgServer = new HashMap<String, Object>();
				Product product = new Product();
				product.setProductID(pId.getText());
				product.setProductName(pName.getText());
				product.setType(pType.getText());
				product.setColor(pColor.getText());
				product.setPrice(pPrice.getText().toString());
				// check if the image null to put the defualt one
				product.setImg(img.getImage());
				cat.getList().add(product);
				msgServer.put("msgType", "insert");
				msgServer.put("query", "INSERT INTO zrle.`product` (ProductID, Name,Type,Color,Price,SalePrice)VALUES('"
						+ product.getProductID() + "','" + product.getProductName() + "','" + product.getType()
						+ "'  ,'" + product.getColor() + "','" + product.getPrice() + "','" + product.getSale() + "')");
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
					System.out.println("Error adding new product!");
					return;
				}
				byte[] buffer = new byte[20 * 1024];
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				File file = new File(imglabel.getText());
				FileInputStream fis = new FileInputStream(imglabel.getText());
				int read;
				while ((read = fis.read(buffer)) != -1) {
					os.write(buffer, 0, read);
				}
				fis.close();
				os.close();
				System.out.println(buffer);
				msgServer.put("msgType", "saveImage");
				msgServer.put("path", "C:\\Zrlefiles\\ProudctsImages\\image" + product.getProductID() + ".jpg");
				//msgServer.put("ProductID", product.getProductID());
				msgServer.put("byteArr", buffer);
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
					ErrorWin("error in saving the image in the server folders!");
					System.out.println("Error saving pictrue!");
					return;
				}
				else{
					msgServer.put("msgType", "save");
					msgServer.put("path", "C:\\Zrlefiles\\ProudctsImages\\image" + product.getProductID() + ".jpg");
					msgServer.put("ProductID",  product.getProductID());
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
						ErrorWin("error in saving the image in the database!");
						System.out.println("Error saving pictrue!");
					}
					else{
						MessageWin("The product have been added sucssesfully!");
					}
				}
			}
		}
	}

	// byte[] buffer = new byte[20 * 1024];
	// ByteArrayOutputStream os = new ByteArrayOutputStream();
	// FileInputStream fis = new FileInputStream(imglabel.getText());
	// int read;while((read=fis.read(buffer))!=-1)
	// {
	/// os.write(buffer, 0, read);
	// }fis.close();os.close();
	// }

	@FXML
	public void BackBtn(MouseEvent event) {
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

	public void ErrorWin(String msg) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error Dialog");
		alert.setHeaderText("Look, an Error Dialog");
		alert.setContentText("Ooops, there was an error!, " + msg + " !.");
		alert.showAndWait();
	}

	public void WarrningWin() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Warning None Photo Dialog");
		alert.setHeaderText("Look, a Warning Dialog");
		alert.setContentText("Careful!, you didn't upload a picture!.");
		alert.showAndWait();
	}

	public void MessageWin(String msg) throws InterruptedException {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Successfully Dialog");
		alert.setHeaderText("Successfully Adding Product Dialog");
		alert.setContentText(msg + " !.");
		alert.showAndWait();
		Thread.sleep(1);
		alert.close();
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
		if (pType.getText().isEmpty() || pColor.getText().isEmpty() || pId.getText().isEmpty()
				|| pName.getText().isEmpty()) {
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

	public boolean IDValidation() {

		Pattern p = Pattern.compile("[0-9999]+");
		Matcher m = p.matcher(pId.getText());
		if (m.find() && m.group().equals(pId.getText())) {
			return true;
		} else {

			return false;
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

	public boolean CheckExistingProduct(int id, String name) {
		boolean flag = true; // thats mean the product does not exist yet!
		for (int i = 0; i < cat.getList().size(); i++) {
			if (id == Integer.parseInt(cat.getList().get(i).getProductID())
					|| name.equals(cat.getList().get(i).getProductName())) {
				flag = false;// the product is exist
			}
		}
		return flag;
	}

}
