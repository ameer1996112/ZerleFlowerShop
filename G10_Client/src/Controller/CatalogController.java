package Controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.text.View;

import Entity.Product;
import application.Main;
import Entity.ProductTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class CatalogController implements Initializable {// add function to add
															// and delete and
															// back buttons

	@FXML
	private ImageView image;

	@FXML
	private ListView<String> listView;

	@FXML
	private TableView<ProductTable> tableView = new TableView<ProductTable>();

	@FXML
	private TableColumn<ProductTable, String> ProductName = new TableColumn<ProductTable, String>("Product");

	@FXML
	private TableColumn<ProductTable, String> Desc = new TableColumn<ProductTable, String>("Description");

	@FXML
	private TableColumn<ProductTable, String> Price = new TableColumn<ProductTable, String>("Price");

	@FXML
	private TextField textField;

	@FXML
	private TextArea details;

	@FXML
	private Button editbtn;

	private static ObservableList<Product> list = FXCollections.observableArrayList();

	private static int FocusedItemID;

	private static int SelectedItem;
	private static int listSize;
	private ArrayList<byte[]> result;
	private HashMap<String, Object> msgServer = new HashMap<String, Object>();

	private Integer Intanswer;

	public void FillInfoFromDB() throws IOException { // loop to fill the info
														// the list
		HashMap<String, String> msgServer = new HashMap<String, String>();
		// ArrayList<Product> list = new ArrayList<Product>();
		if(list.isEmpty()==true){
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select ProductID, Name, Type, Color, Price,SalePrice from product");
		Main.client.sendMessageToServer(msgServer);
		synchronized (Main.client) {
			try {
				Main.client.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ArrayList<String> Deatils = (ArrayList<String>) Main.client.getMessage();
		Product proudct;
		int j=0;
		for (int i = 0; i < (Deatils.size()); i += 6) {
			proudct = new Product(Deatils.get(i), Deatils.get(i + 1), Deatils.get(i + 2), Deatils.get(i + 3),
					Deatils.get(i + 4));
			// System.out.println(product.toString());
			proudct.setSale(Double.parseDouble(Deatils.get(i + 5)));
			File imagefile = new File("C:\\Zrlefiles\\ProudctsImages\\image" +proudct.getProductID() + ".jpg");
			Image image = new Image(imagefile.toURI().toString());
			proudct.setImg(image);
			list.add(proudct);
			//list.get(Integer.parseInt(proudct.getProductID())).setImg(image);
			//j++;
		}
	}
		/*
		for (int i = 0; i < Main.proudctList.size(); i++) {
			int proudctID = Integer.parseInt(Main.proudctList.get(i).getProductID());
			//if (i == proudctID) {
				File imagefile = new File("C:\\Zrlefiles\\ProudctsImages\\image" + i + ".jpg");
				Image image = new Image(imagefile.toURI().toString());
				list.get(i).setImg(image);
			//}
			// System.out.println(product.toString());
		}
		*/
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		Image img2 = new Image("/images/84-512.png");
		image.setImage(img2);
		int index = 0;
		try {
			FillInfoFromDB();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// fill the list info from the data base by loop!
		ObservableList<Product> list1 = FXCollections.observableArrayList(list);
		ObservableList<String> ProductsName = FXCollections.observableArrayList();
		for (index = 0; index < list1.size(); index++) {
			ProductsName.add(list1.get(index).getProductName());
		}
		// make loop to fill the products names
		listView.setItems(ProductsName);
		ProductName.setCellValueFactory(new PropertyValueFactory<ProductTable, String>("ProductName"));
		Desc.setCellValueFactory(new PropertyValueFactory<ProductTable, String>("Desc"));
		Price.setCellValueFactory(new PropertyValueFactory<ProductTable, String>("Price"));
		ProductName.setEditable(true);
		Desc.setEditable(true);
		Price.setEditable(true);
		tableView.getColumns().setAll(ProductName, Desc, Price);
		tableView.setEditable(true);

	}

	@FXML
	public void EditButton(MouseEvent event) throws IOException {
		if (details.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Look, an Error Dialog");
			alert.setContentText("Ooops, there was an error! please press on item to Edit");

			alert.showAndWait();
		} else {
			textField.setEditable(true);
			details.setEditable(true);
			ProductName.setEditable(true);
			Desc.setEditable(true);
			Price.setEditable(true);
			try {
				Parent root1;
				root1 = FXMLLoader.load(getClass().getResource("/FXML/EditProduct.fxml"));
				// Parent root1=(Parent) fx.load();
				Scene scene = new Scene(root1);
				scene.getStylesheets().add(getClass().getResource("/FXML/application.css").toExternalForm());
				Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

				stage.setTitle("Zr-Le Edit Product");
				stage.setScene(scene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Can not open the edit product window please try again !!");
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Dialog");
				alert.setHeaderText("Look, an Error Dialog");
				alert.setContentText("Ooops, there was an error!, you can not open the page!.");
				alert.showAndWait();
			}
		}
	}

	public void buttonAction(MouseEvent event) { // when we select an
													// item/product from the
													// list view in the left
													// side!
		ObservableList<String> names;
		names = listView.getSelectionModel().getSelectedItems();
		int num = listView.getSelectionModel().getSelectedIndex();
		setFocusedItemID(Integer.parseInt(list.get(num).getProductID()));
		setSelectedItem(num);
		double sale = list.get(num).getSale();
		// get image from database!!
		// for(String elem :names) {
		image.setImage(list.get(num).getImg());
		if(image==null){
			image.setImage(new Image("/images/84-512.png"));
		}
		details.setText("Beautifull " + list.get(num).getColor() + "  " + list.get(num).getType()
				+ " .this product had sale of :" + sale + "% .");
		textField.setText(list.get(num).getProductName());
		textField.setEditable(false);
		details.setEditable(false);
		ObservableList<ProductTable> tableList = FXCollections
				.observableArrayList(new ProductTable(list.get(num).getType(),
						list.get(num).getColor() + " " + list.get(num).getType(), list.get(num).getPrice() + " $"));
		tableView.setItems(tableList);
		// }
	}

	@FXML
	public void AddProduct(MouseEvent event) throws IOException {
		try {
			Parent root2;
			root2 = FXMLLoader.load(getClass().getResource("/FXML/AddProduct.fxml"));
			// Parent root1=(Parent) fx.load();
			Scene scene = new Scene(root2);
			scene.getStylesheets().add(getClass().getResource("/FXML/application.css").toExternalForm());
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

			stage.setTitle("Zr-Le Add Product");
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Can not open the add product window please try again !!");
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Look, an Error Dialog");
			alert.setContentText("Ooops, there was an error!, you can not open the page!.");
			alert.showAndWait();
		}
	}

	public void DeleteBtn(MouseEvent event) {
		if (details.getText().isEmpty()) {
			Main.showPopUp("ERORR", "Error Dialog", "Look, an Error Dialog",
					"Ooops, there was an error!, you can not open the page!.");
		} else {
			String name = textField.getText();
			String type = ProductName.getCellData(0);
			double price = Double.parseDouble(Price.getCellData(0).replace("$", ""));
			int index = 0;
			for (int i = 0; i < list.size(); i++) {
				if (name.equals(list.get(i).getProductName()) && type.equals(list.get(i).getType())
						&& price == Double.parseDouble(list.get(i).getPrice())) {
					index = i;
					break;
				}
			}
			Main.proudctList.remove(listView.getSelectionModel().getSelectedIndex());
			msgServer.put("msgType", "delete");
			msgServer.put("query", "DELETE FROM product WHERE ProductID='" + list.get(index).getProductID() + "'");
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
			list.remove(index);
			MessageWin("The selected item had been deleted successfully");
			listView.getItems().remove(SelectedItem);
			listView.refresh();
			SetDefault();

		}
	}

	public void BackBtn(MouseEvent event) {
		// to be sure that all the chanches had saved befor to back to the
		// previouse page
	}

	public void SaveBtn(MouseEvent event) {
		// save all the changes in the data base by sql query!
		// by checking if the count of the products in the database is equal to
		// size of the static list!!
	}

	public void ImageView(ActionEvent event) {
		// function to display and save image
	}

	public void SetDefault() {
		Image img2 = new Image("/images/84-512.png");
		image.setImage(img2);
		details.setText("");
		textField.setText("");
		tableView.getItems().clear();
	}

	public void MessageWin(String Msg) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText("Look, an Info");
		alert.setContentText(Msg + " !.");
		alert.showAndWait();
	}

	public ObservableList<Product> getList() {
		return list;
	}

	public void setList(ObservableList<Product> list) {
		this.list = list;
	}

	public int getFocusedItemID() {
		return FocusedItemID;
	}

	public void setFocusedItemID(int focusedItemID) {
		FocusedItemID = focusedItemID;
	}

	public static int getSelectedItem() {
		return SelectedItem;
	}

	public static void setSelectedItem(int selectedItem) {
		SelectedItem = selectedItem;
	}

}
