package Controller;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import Entity.BranchCatalog;
import Entity.Order;
import Entity.Product;
import Entity.ShopCatalog;
import application.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
/**
 * this class is the controller of the the catalogOrder 
 * @author user
 *
 */
public class CatalogOrderController implements Initializable {
	/** Message to server */
	private HashMap<String, Object> msgServer = new HashMap<String, Object>();
	/** HashMap answer from the server */
	private HashMap<String, String> answer = null;
	/** if the answer is integer */
	private Integer Intanswer;
	/** instance of order */
	/** if the answer is ArrayList of byteArraty */
	ArrayList<byte[]> result;
	/** if the result is ArrayList of strings */
	ArrayList<String> answerMsg;
	/** if the answer is arrayList of hashMaps<String,String> */
	ArrayList<HashMap<String, String>> answerFromServer;
	ArrayList<HashMap<Integer, Integer>> answer1;
	public Order order = null;
	@FXML
	private AnchorPane pane;
	@FXML
	private Button ShppingCartBtn;
	@FXML
	private FlowPane flow;
	@FXML
	private ScrollPane scroll;
	@FXML
	private TextField label;
	@FXML
	private ImageView StoreImage;
	@FXML
	private ImageView BackgroundImage;

	public void OpenShoppingCart(ActionEvent event) throws IOException {
		Main.OpenFxmlFile(event, "/FXML/ShoppingCart.fxml");
	}
	@FXML
    void GoBackToMainMenu(ActionEvent event) throws IOException {
		Main.OpenFxmlFile(event, "/FXML/CustomerMainMenu.fxml");
    }



	/*
	 * we use these method to load the shopCatalog proudcts deatils and pictures
	 */
	public void loadCatalog() throws IOException {
		ImageView[] images = new ImageView[Main.catalog.getShopCat().getSize()];
		int i=0;
		ArrayList<Product> tempdeatils = new ArrayList<Product>(Main.catalog.getShopCat().getSize());
		for (i = 0; i < Main.catalog.getShopCat().getSize(); i++) {
			tempdeatils.add(Main.catalog.getShopCat().getCatlaogProducts().get(i).clone());
		}
		// System.out.println(tempdeatils.toString());
		i = 0;
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
		Main.catalog.getShopCat().getCatlaogProducts().get(0).getProductName();
		pane.getChildren().add(scroll);
		while (i < images.length) {
			File imagefile = new File("C:\\Zrlefiles\\ProudctsImages\\image"
					+ Main.catalog.getShopCat().getCatlaogProducts().get(i).getProductID() + ".jpg");
			Image image = new Image(imagefile.toURI().toString());
			images[i] = new ImageView(image);
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
			String string = new String(
					"Proudct name:" + Main.catalog.getShopCat().getCatlaogProducts().get(i).getProductName()
							+ "\nProudct type:" + Main.catalog.getShopCat().getCatlaogProducts().get(i).getType()
							+ "\nProudct color:" + Main.catalog.getShopCat().getCatlaogProducts().get(i).getColor()
							+ "\nProudct price:" + Main.catalog.getShopCat().getCatlaogProducts().get(i).getPrice());
			int BranchCatPrice = Integer.parseInt(Main.catalog.getShopCat().getCatlaogProducts().get(i).getPrice());
			Label text = new Label();
			int ShopCatPrice = Integer.parseInt(Main.tempShopCatalog.getCatlaogProducts().get(i).getPrice());
			if (BranchCatPrice != ShopCatPrice) {
				text.setTextFill(Color.BLUE);
				string += "\n*****Sale*****";

			}
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
			String btnID = Main.catalog.getShopCat().getCatlaogProducts().get(i).getProductID();
			btn.setId(btnID);
			btn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					addProudctToOrder(btn.getId(), Main.user.getOrder().getOrderID());
				}
			});
			i++;
		}
	}

	private String getOrderID() {

		msgServer.put("msgType", "select");
		msgServer.put("query", "SELECT  MAX(OrderID) FROM zrle.`order` ;");
		try {
			Main.client.sendMessageToServer(msgServer);
			synchronized (Main.client) {
				Main.client.wait();
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		answerMsg = (ArrayList<String>) Main.client.getMessage();
		if (answerMsg.size() == 0) {
			System.out.println("Error:didn't found the required data in the database");
			return null;
		}
		return answerMsg.get(0);

	}

	private int checkOrders() {
		msgServer.put("msgType", "select");
		msgServer.put("query", "SELECT  OrderStatus FROM zrle.`order` where UserID='" + Main.user.getUserID()
				+ "' and OrderStatus='0'");
		try {
			Main.client.sendMessageToServer(msgServer);
			synchronized (Main.client) {
				Main.client.wait();
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		answerMsg = (ArrayList<String>) Main.client.getMessage();
		return answerMsg.size();

	}

	private Order createOrder(String orderID) {
		int newOrderid = Integer.parseInt(orderID) + 1;
		String OrderID = Integer.toString(newOrderid);
		String BranchID = "1";
		String UserID = Main.user.getUserID();
		Order order = new Order(OrderID, BranchID, UserID);
		msgServer.put("msgType", "insert");
		msgServer.put("query",
				"INSERT INTO zrle.`order` (OrderID, UserID, BracnchID,OrderStatus,DeliveryID,OrderDate,PaymentID)VALUES('"
						+ OrderID + "','" + UserID + "','" + BranchID + "'  ,'0',null,null,null)");
		System.out.println(
				"INSERT INTO zrle.`order` (OrderID, UserID, BracnchID,OrderStatus,DeliveryID,OrderDate,PaymentID)VALUES('"
						+ OrderID + "','" + UserID + "','" + BranchID + "'  ,'0',null,null,null)");
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
			System.out.println("Error creating new order!");
		}
		return order;

	}

	public void addProudctToOrder(String btnID, String orderID) {
		int BtnID = Integer.parseInt(btnID);
		int quan;
		Product proudct = Main.catalog.getShopCat().getCatlaogProducts().get(BtnID - 1);
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
				Main.showPopUp("INFORMATION", "Information Dialog", null,
						"Some error occurerd while updating the proudct information in the database!");
			} else {
				Main.showPopUp("INFORMATION", "Infromation Dialog", null,
						"The  quantity of " + proudct.getProductName() + " have updated to " + quan + "!");
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
				Main.showPopUp("ERROR", "Error", null, "Some error occured!");
			} else {
				Main.showPopUp("INFORMATION", "Information Dialog", null,
						"The proudct  " + proudct.getProductName() + "have been added sucssesfully to the order!");
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {

		loadPictures();
			loadCatalog();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	protected void loadPictures() {
		Image Backgroundimg = new Image(getClass().getResource("/images/Background.jpg").toString());
		BackgroundImage.setImage(Backgroundimg);
		Image Storeimg = new Image(getClass().getResource("/images/Store.png").toString());
		StoreImage.setImage(Storeimg);
		
	}

}
