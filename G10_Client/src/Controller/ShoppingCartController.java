package Controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTextField;

import Entity.Order;
import application.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
/**
 * this class for the shopping cart
 * @author Ameer amer
 *
 */
public class ShoppingCartController implements Initializable {
	@FXML
	private Pane pane;
	@FXML
	TextField quanField;
	Pane pane1 = new Pane();
	VBox box = new VBox();
	Text PriceText = new Text();
	Text TotalPriceText = new Text();
	AnchorPane pane3 = new AnchorPane();
	ScrollPane scroll = new ScrollPane();
	private int ButtonID = 0;
	private int[] index = new int[100];
	private int totalPrice = 0;
	private int[] QuanField = new int[20];
	private int finalPrice = 0;
	private int ProudctPrice = 0;
	private Integer Intanswer;
	private HashMap<String, Object> msgServer = new HashMap<String, Object>();

@FXML
	public void openPaymentPage(ActionEvent event) throws IOException {
	Main.OpenFxmlFile(event, "/FXML/PaymentPage.fxml");
	}

	public void deleteProudct(String id, int index[]) {
		msgServer.put("msgType", "delete");
		msgServer.put("query",
				"DELETE FROM order_product WHERE OrderID='" + Main.user.getOrder().getOrderID() + "' and ProductID='"
						+ Main.user.getOrder().getOrderProudcts().get(Integer.parseInt(id)).getProductID() + "';");
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
			Main.showPopUp("ERROR", "Error dialog", null, "Some error occured while deleting the proudct from the sopping cart please try again!");
		} else {
Main.showPopUp("INFORMATION", "Information dialog", null, "The proudct deleted sucssefully from the shopping cart!");
			// Main.user.getOrder().getOrderProudcts().clear();
			int ID = Integer.parseInt(id);
			int proudctPrice  =Integer.parseInt(Main.user.getOrder().getOrderProudcts().get(Integer.parseInt(id)).getPrice());
			int quan = Main.user.getOrder().getQuantity()[Integer.parseInt(Main.user.getOrder().getOrderProudcts().get(ID).getProductID())];
			totalPrice = totalPrice-(proudctPrice*quan);
			TotalPriceText.setText("Total:" + Integer.toString(totalPrice) + " NIS");
			Main.user.getOrder().setTotalPrice(totalPrice);
			box.getChildren().remove(ID);
			Main.user.getOrder()
					.setQuan(Integer.parseInt(Main.user.getOrder().getOrderProudcts().get(ID).getProductID()), 0);
			Main.user.getOrder().getOrderProudcts().remove(ID);

		}
		// System.out.println(Main.user.getOrder().getOrderProudcts().size());
		if (Main.user.getOrder().getOrderProudcts().size() == 0) {
			pane.getChildren().clear();
			// pane.getChildren().clear();
			Text text = new Text("Your shopping cart is empty, but it doesn't have to be.");
			text.setFont(Font.font("Helvetica", FontWeight.BOLD, 16));
			text.setLayoutX(194);
			text.setLayoutY(190);
			Text text1 = new Text("There are lots of great deals and one-of-a-kind items just waiting for you.");
			text1.setLayoutX(194);
			text1.setLayoutY(222);
			Text text2 = new Text(
					"Start shopping, and look for the 'Add to cart' button. You can add several items to your cart from catalog or custom order and pay for them all at once.");
			text2.setWrappingWidth(596);
			text2.setLayoutX(194);
			text2.setLayoutY(239);
			pane.getChildren().addAll(text, text1, text2);
		}
	}

	public void updateQuan(String id, int quan1, int quan2, int ProudctPrice) {
		msgServer.put("msgType", "update");
		msgServer.put("query", "UPDATE zrle.order_product SET Quantity='" + quan1 + "' Where OrderID='"
				+ Main.user.getOrder().getOrderID() + "' and ProductID='" + id + "'");
		try {
			Main.client.sendMessageToServer(msgServer);
			synchronized (Main.client) {
				Main.client.wait();
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		Intanswer = (Integer) Main.client.getMessage();
		if (Intanswer == 1) {
			if(Main.user.getOrder().getOrderType().equals("Catalog")){
Main.showPopUp("INFORMATION", "Information dialog", null,"The quantity of " + Main.catalog.getShopCat().getCatlaogProducts().get(Integer.parseInt(id)).getProductName()
		+ " updated to " + quan1 + "!" );
			}
			else{
				Main.showPopUp("INFORMATION", "Information dialog", null,"The quantity of " + Main.proudctList.get(Integer.parseInt(id)).getProductName()
						+ " updated to " + quan1 + "!" );
			}
		} else {
			Main.showPopUp("ERROR", "Error dialog", null,"Some error occured!" );
		}
		int finalPrice = quan1 * ProudctPrice;
		PriceText.setText(Integer.toString(finalPrice) + "NIS");
		totalPrice += (quan1 - quan2) * ProudctPrice;
		TotalPriceText.setText("Total:" + Integer.toString(totalPrice) + " NIS");
		Main.user.getOrder().setTotalPrice(totalPrice);

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		int i = 0;
		if (Main.user.getOrder().getOrderProudcts().size() != 0||Main.user.getOrder()!=null) {
			ImageView[] images = new ImageView[Main.user.getOrder().getOrderProudcts().size()];
			box.setPrefSize(514, 338);
			box.setLayoutX(100);
			box.setLayoutY(62);
			box.setSpacing(20);
			box.setAlignment(Pos.CENTER);
			scroll.setPrefSize(800, 600);
			scroll.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
			scroll.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
			scroll.getStylesheets().add("/FXML/custom.css");
			scroll.getStyleClass().add("mylistview");
			pane1.getChildren().add(box);
			pane1.setLayoutX(50);
			pane1.setLayoutY(70);
			scroll.setContent(pane1);
			pane.getChildren().add(scroll);
			while (i < images.length) {
				File imagefile = new File("C:\\Zrlefiles\\ProudctsImages\\image"
						+ Main.user.getOrder().getOrderProudcts().get(i).getProductID() + ".jpg");
				images[i] = new ImageView(new Image(imagefile.toURI().toString()));
				images[i].setFitHeight(94);
				images[i].setFitWidth(80);
				images[i].setLayoutX(2);
				images[i].setLayoutY(3);

				Pane ImagePane = new Pane();
				ImagePane.setPrefSize(86, 90);
				ImagePane.getChildren().add(images[i]);
				Text text = new Text(Main.user.getOrder().getOrderProudcts().get(i).getProductName());
				text.setWrappingWidth(130);
				text.setFont(Font.font("Helvetica", FontWeight.BOLD, 19));
				Pane textPane = new Pane();
				Text text1 = new Text("Qunatity:");
				text1.setLayoutX(80);
				text1.setLayoutY(40);
				TextField quanField = new TextField();
				quanField.setPrefSize(40, 15);
				if (Main.user.getOrder().getQuantity()[Integer
						.parseInt(Main.user.getOrder().getOrderProudcts().get(i).getProductID())] != 0) {
					int quan = Main.user.getOrder().getQuantity()[Integer
							.parseInt(Main.user.getOrder().getOrderProudcts().get(i).getProductID())];
					quanField.setText(Integer.toString(quan));
					quanField.setLayoutX(136);
					quanField.setLayoutY(23);
					Button button = new Button("Update");
					String buttonID = (Main.user.getOrder().getOrderProudcts().get(i).getProductID());
					int buttonid = Integer.parseInt(buttonID) - 1;

					button.setId(buttonID);
					button.setLayoutX(182);
					button.setLayoutY(23);
					button.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {

							updateQuan(button.getId(), Integer.parseInt(quanField.getText()), quan, ProudctPrice);
						}
					});
					quanField.setAlignment(Pos.CENTER_RIGHT);
					textPane.getChildren().addAll(text1, quanField, button);
					// textPane.setPrefSize(43, 115);
					Pane PricePane = new Pane();
					ProudctPrice = Integer.parseInt(Main.user.getOrder().getOrderProudcts().get(i).getPrice());
					finalPrice = ProudctPrice * quan;
					PriceText = new Text(Integer.toString(finalPrice) + "NIS");
					PriceText.setFont(Font.font("Helvetica", FontWeight.BOLD, 14));
					PriceText.setText(Integer.toString(finalPrice) + "NIS");
					PriceText.setLayoutX(15);
					PriceText.setLayoutY(40);
					// removeBtnPane.getChildren().add(removeBtn);
					Button removeBtn = new Button("Remove");
					removeBtn.setId(Integer.toString(i));
					removeBtn.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							deleteProudct(removeBtn.getId(), index);
						}
					});
					removeBtn.setLayoutX(70);
					removeBtn.setLayoutY(23);
					PricePane.getChildren().addAll(PriceText, removeBtn);
					HBox box1 = new HBox();
					box1.setPrefSize(600, 101);
					box1.getChildren().addAll(ImagePane, text, textPane, PricePane);
					// box1.setAlignment(Pos.CENTER);
					box1.setLayoutY(12);
					box1.setLayoutX(5);
					AnchorPane Pane = new AnchorPane();
					Pane.getChildren().add(box1);
					Pane.getStylesheets().add("/FXML/style.css");
					Pane.setPrefSize(518, 153);
					box.setSpacing(5);
					box.getChildren().addAll(Pane);
				}
				i++;
				totalPrice += finalPrice;
			}
			Button ProceedToCheckout = new Button("Proceed to checkout");
			// ProceedToCheckout.setStyle("-fx-background-color:#0079bc");
			ProceedToCheckout.getStylesheets().add("/FXML/DarkTheme.css");
			ProceedToCheckout.setLayoutX(277);
			ProceedToCheckout.setLayoutY(40);
			ProceedToCheckout.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					try {
						openPaymentPage(event);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			});
			Button ContinueShopping = new Button("Continue shopping");
			ContinueShopping.getStylesheets().add("/FXML/DarkTheme.css");
			ContinueShopping.setLayoutX(85);
			ContinueShopping.setLayoutY(40);
			Main.user.getOrder().setTotalPrice(totalPrice);
			TotalPriceText = new Text("Total:" + totalPrice + " NIS");
			TotalPriceText.setLayoutX(243);
			TotalPriceText.setLayoutY(27);
			TotalPriceText.setFont(Font.font("Helvetica", FontWeight.BOLD, 18));
			pane3.getChildren().addAll(TotalPriceText, ContinueShopping, ProceedToCheckout);
			pane3.getStylesheets().add("/FXML/style.css");
			box.getChildren().add(pane3);

		} else {
			Text text = new Text("Your shopping cart is empty, but it doesn't have to be.");
			text.setFont(Font.font("Helvetica", FontWeight.BOLD, 16));
			text.setLayoutX(194);
			text.setLayoutY(190);
			Text text1 = new Text("There are lots of great deals and one-of-a-kind items just waiting for you.");
			text1.setLayoutX(194);
			text1.setLayoutY(222);
			Text text2 = new Text(
					"Start shopping, and look for the 'Add to cart' button. You can add several items to your cart from different sellers and pay for them all at once.");
			text2.setWrappingWidth(596);
			text2.setLayoutX(194);
			text2.setLayoutY(239);
			pane.getChildren().addAll(text, text1, text2);
		}
	}

}
