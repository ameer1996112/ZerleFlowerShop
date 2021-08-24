package Controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import Entity.Product;
import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * this controller for the customer order
 * 
 * @author Ameer amer
 *
 */
public class CustomOrderController extends CatalogOrderController implements Initializable {
	@FXML
	private AnchorPane pane;// this paramter injected by FXML

	@FXML
	private ComboBox<String> ItemTypeCombo;// this combo show the item types of
											// the products

	@FXML
	private TextField ColorTxt;// in this text field we type the color

	@FXML
	private TextField MinTxt;// minimum price text field

	@FXML
	private TextField MaxTxt;// max price text field

	@FXML
	private Button GoBtn;// we click on this button after filling the required
							// data
	@FXML
	private ImageView BackgroundImage;// backgroound image

	@FXML
	private ImageView StoreImage;// logo image

	private ArrayList<String> answerMsg;// array list for answer from server
	private HashMap<String, Object> msgServer = new HashMap<String, Object>();
	private final ObservableList<String> options = FXCollections.observableArrayList("Bouquet", "flower", "chocolate");// types
																														// of
																														// products

	private Integer Intanswer;// for answer from server

	private ArrayList<Product> list = new ArrayList<Product>();// list that
																// contain all
																// productss

	/**
	 * this method open the custom order
	 * 
	 * @param event
	 */
	@FXML
	void OpenCustomOrder(ActionEvent event) {
		// load the proudcts
		if (ItemTypeCombo.getSelectionModel().isEmpty() == false || ColorTxt.getText().isEmpty() == false
				|| MinTxt.getText().isEmpty() == false || MaxTxt.getText().isEmpty() == false) {
			msgServer.put("msgType", "loadDeatils");
			msgServer.put("query",
					"SELECT * FROM product WHERE Price BETWEEN " + MinTxt.getText() + " AND " + MaxTxt.getText()
							+ " and Color='" + ColorTxt.getText() + "' and Type='"
							+ ItemTypeCombo.getSelectionModel().getSelectedItem() + "'; ");
			System.out.println("SELECT * FROM product WHERE Price BETWEEN " + MinTxt.getText() + " AND "
					+ MaxTxt.getText() + " and Color='" + ColorTxt.getText() + "' and Type='"
					+ ItemTypeCombo.getSelectionModel().getSelectedItem() + "'; ");
			try {
				Main.client.sendMessageToServer(msgServer);
				synchronized (Main.client) {
					Main.client.wait();
				}
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			ArrayList<HashMap<String, String>> Deatils = (ArrayList<HashMap<String, String>>) Main.client.getMessage();
			// System.out.println(Deatils.size());
			if (Deatils.size() == 0) {
				ShowAlret("Sorry,we don't have products with these deatils!");
				// ItemTypeCombo.getSelectionModel().clearSelection();
				// ColorTxt.clear();
				// MinTxt.clear();
				// MaxTxt.clear();
				return;

			}
			Product product;
			for (int i = 0; i < Deatils.size(); i++) {
				product = new Product(Deatils.get(i).get("ID"), Deatils.get(i).get("Name"), Deatils.get(i).get("Type"),
						Deatils.get(i).get("Color"), Deatils.get(i).get("Price"));
				// System.out.println(product.toString());
				list.add(product);
			}
			// Main.proudctList = list;
			int i = 0;
			FlowPane show = new FlowPane();
			// show.setStyle("-fx-background-color: GREY");
			show.setPrefWidth(999);
			show.setPrefHeight(369);
			show.setPadding(new Insets(20, 0, 20, 0));
			show.setVgap(30);
			show.setHgap(15);
			show.setAlignment(Pos.CENTER);
			// show.setPrefWrapLength(250); // preferred width allows for two
			ScrollPane scroll = new ScrollPane();
			scroll.setPrefSize(1000, 500);
			Pane ShowPane = new Pane();
			// ShowPane.setPrefSize(747, 351);
			ShowPane.getChildren().add(show);
			show.setLayoutX(10);
			show.setLayoutY(10);
			scroll.setContent(ShowPane);
			scroll.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
			scroll.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
			scroll.getStylesheets().add("/FXML/custom.css");
			scroll.getStyleClass().add("mylistview");
			pane.getChildren().add(scroll);
			ImageView[] images = new ImageView[list.size()];
			while (i < images.length) {
				File imagefile = new File("C:\\Zrlefiles\\ProudctsImages\\image" + list.get(i).getProductID() + ".jpg");
				images[i] = new ImageView(new Image(imagefile.toURI().toString()));

				Pane ImagePane = new Pane();
				// ImagePane.setPrefSize(231, 180);
				images[i].setFitHeight(150);
				images[i].setFitWidth(221);
				images[i].setLayoutX(5);
				images[i].setLayoutY(10);
				ImagePane.getChildren().add(images[i]);
				VBox box = new VBox();
				AnchorPane pane2 = new AnchorPane();
				box.setPrefSize(236, 350);
				String string = new String("Proudct name:" + list.get(i).getProductName() + "\nProudct type:"
						+ list.get(i).getType() + "\nProudct color:" + list.get(i).getColor() + "\nProudct price:"
						+ list.get(i).getPrice());
				Label text = new Label();
				text.setPrefSize(205, 120);
				Pane textPane = new Pane();
				textPane.setPrefSize(205, 140);
				textPane.getChildren().add(text);
				text.setText(string);
				text.setFont(Font.font("Josefin Sans", FontWeight.BOLD, 16));
				text.setWrapText(true);
				Button btn = new Button("Add to cart");
				btn.getStylesheets().add("/FXML/DarkTheme.css");
				btn.getStyleClass().add("button");
				btn.setLayoutX(79);
				btn.setLayoutY(0);
				Pane BtnPane = new Pane();
				BtnPane.setPrefWidth(236);
				BtnPane.getChildren().add(btn);
				box.getChildren().addAll(ImagePane, textPane, BtnPane);
				pane2.getChildren().add(box);
				pane.getStylesheets().add("/FXML/style.css");
				show.getChildren().addAll(pane2);
				String btnID = Integer.toString(i);
				btn.setId(btnID);
				btn.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						String orderID = Main.user.getOrder().getOrderID();
						addProudctToOrder(btn.getId(), orderID);
					}
				});
				i++;
			}

		} else {
			ShowAlret("Error:please fill all required fields!");
		}
	}

	public void ShowAlret(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);
		alert.setContentText(message);

		alert.showAndWait();
	}

	public void addProudctToOrder(String btnID, String orderID) {
		int BtnID = Integer.parseInt(btnID);
		int quan;
		Product proudct = list.get(BtnID);
		if (Main.user.getOrder().getQuantity().length == 0
				|| Main.user.getOrder().getQuantity()[Integer.parseInt(proudct.getProductID())] == 0) {
			quan = 1;
		} else {
			quan = Main.user.getOrder().getQuantity()[Integer.parseInt(proudct.getProductID())] + 1;
		}
		System.out.println(Main.user.getOrder().getOrderProudcts().toString());
		System.out.println(proudct);
		// System.out.println));
		if (Main.user.getOrder().contain(proudct)) {
			msgServer.put("msgType", "update");
			msgServer.put("query", "UPDATE zrle.order_product SET Quantity='" + quan + "' Where OrderID='" + orderID
					+ "' and ProductID='" + proudct.getProductID() + "'");
			Main.user.getOrder().setQuan(Integer.parseInt(proudct.getProductID()), quan);

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
				ShowAlret("Some error occurerd while updating the proudct information in the database!");
			} else {
				ShowAlret("The  quantity of " + proudct.getProductName() + " have updated to " + quan + "!");
			}

		} else {
			Main.user.getOrder().setProudct(proudct);
			Main.user.getOrder().setQuan(Integer.parseInt(proudct.getProductID()), quan);
			int size = Main.user.getOrder().getSize() + 1;
			Main.user.getOrder().setSize(size);
			msgServer.put("msgType", "insert");
			msgServer.put("query", "INSERT INTO zrle.`order_product` (OrderID,ProductID,Quantity)VALUES('" + orderID
					+ "','" + proudct.getProductID() + "'," + quan + ")");
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
				ShowAlret("Some error occured !");
			} else {
				ShowAlret("The proudct  " + proudct.getProductName() + "have been added sucssesfully to the order!");
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			Image Backgroundimg = new Image(getClass().getResource("/images/Background.jpg").toString());
			BackgroundImage.setImage(Backgroundimg);
			Image Storeimg = new Image(getClass().getResource("/images/Store.png").toString());
			StoreImage.setImage(Storeimg);
			if (pane.getChildren().isEmpty() == true) {
				AnchorPane newLoadedPane = FXMLLoader.load(getClass().getResource("/FXML/2.fxml"));
				pane.getChildren().add(newLoadedPane);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ItemTypeCombo.setItems(options);

	}
}
