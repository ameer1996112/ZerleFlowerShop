package Controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import com.jfoenix.validation.RequiredFieldValidator;

import Entity.Delivery;
import Entity.Payment;
import Entity.Product;
import application.Main;
import application.SendEmail;
import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;

/**
 * this class handling all the operartions that happen on the payment page
 * 
 * @author Ameer amer
 *
 */
public class PaymentPageController extends CatalogOrderController implements Initializable {
	@FXML
	private Pane pane;// variable injected by fxml
	@FXML
	private ScrollPane scroll;// the scroll pane varible
	@FXML
	private AnchorPane f;

	@FXML
	private ImageView Checkoutimage;// logo image

	@FXML
	private JFXTextField firstName;// firstname field

	@FXML
	private JFXTextField lastName;// lastname field

	@FXML
	private JFXTextField Email;// email field

	@FXML
	private JFXTextField Telephone;// telephone field

	@FXML
	private ImageView personlinfoimage;// this image for personalinfo

	@FXML
	private AnchorPane shippingPane;// this pane injected by fxml

	@FXML
	private JFXTextField shippingAddress;// shipping address field

	@FXML
	private JFXTextField shippingTelephone;// shipping telephone field

	@FXML
	private JFXTimePicker Time;// this for picking shipping time

	@FXML
	private JFXDatePicker date;// this for picking shipping date

	@FXML
	private ImageView shippingdeatilsimage;// shipping deatils logo

	@FXML
	private AnchorPane greetingPane;// this pane injected by fxml

	@FXML
	private TextArea greetingText;// this for greeting text

	@FXML
	private ImageView greetingimage;// this for greeting image

	@FXML
	private AnchorPane PaymentPange;// this pane injected by fxml

	@FXML
	private JFXCheckBox creditCardCheckBox;// credit card check box

	@FXML
	private JFXCheckBox cashCheckBox;// cash check box

	@FXML
	private ImageView paymentmethodimage;
	private static final String FX_LABEL_FLOAT_TRUE = "-fx-label-float:true;";
	private static final String EM1 = "1em";
	private static final String ERROR = "error";

	private HashMap<String, Object> msgServer = new HashMap<String, Object>();
	private ArrayList<String> answerMsg;
	private ArrayList<String> answer;
	private String PaymentMethod = null;
	private String DateTime;

	// disable payment method checkbox when some payment checked
	@FXML
	void disablePaymentMethodCheck(ActionEvent event) {
		if (creditCardCheckBox.isSelected()) {
			cashCheckBox.setDisable(true);
			PaymentMethod = "CreditCard";

		} else {
			cashCheckBox.setDisable(false);
		}
		if (cashCheckBox.isSelected()) {
			creditCardCheckBox.setDisable(true);
			PaymentMethod = "Cash";
		} else {
			creditCardCheckBox.setDisable(false);
		}
	}
/**
 * this metohd for paying for order
 */
	@SuppressWarnings("unused")
	public void PayForOrder() {
		if (Main.user.getOrder().getOrderStatus() == 0) {
			System.out.println(firstName.getText().isEmpty());
			System.out.println(lastName.getText().isEmpty());
			System.out.println(Email.getText().isEmpty());
			System.out.println(Telephone.getText().isEmpty());
			if (firstName.getText().isEmpty() == false && lastName.getText().isEmpty() == false
					&& Email.getText().isEmpty() == false && Telephone.getText().isEmpty() == false) {

				String PersonalFirstname = firstName.getText();
				String Personallastname = lastName.getText();
				String Personalemail = Email.getText();
				String Personaltelephone = Telephone.getText();
				if (!shippingPane.isDisable() && Main.user.getOrder().getDelivery() == null) {
					if (shippingAddress.getText().isEmpty() == false && shippingTelephone.getText().isEmpty() == false
							&& date.getValue() != null && Time.getValue() != null) {
						LocalDate Date = date.getValue();
						LocalTime time = Time.getValue();
						String shippingAddres = shippingAddress.getText();
						String shippingTelephones = shippingTelephone.getText();
						Delivery delivery = new Delivery(Main.user.getOrder().getOrderID(), shippingAddres,
								PersonalFirstname, Personallastname, shippingTelephones, Date);
						msgServer.put("msgType", "select");
						msgServer.put("query", "SELECT  MAX(DeliveryID) FROM zrle.`delivery` ;");
						try {
							Main.client.sendMessageToServer(msgServer);
							synchronized (Main.client) {
								Main.client.wait();
							}
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						answer = (ArrayList<String>) Main.client.getMessage();
						msgServer.put("msgType", "insert");
						msgServer.put("query",
								"INSERT INTO delivery (DeliveryID, OrderID, Address,Name,Phone,DeliveryDate)VALUES('"
										+ (Integer.parseInt(answer.get(0)) + 1) + "','"
										+ Main.user.getOrder().getOrderID() + "','" + shippingAddres + "','"
										+ PersonalFirstname + "','" + shippingTelephones + "','" + Date + "')");
						try {
							Main.client.sendMessageToServer(msgServer);
							synchronized (Main.client) {
								Main.client.wait();
							}
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						msgServer.put("msgType", "update");
						msgServer.put("query",
								"UPDATE zrle.order SET DeliveryID='" + (Integer.parseInt(answer.get(0)) + 1)
										+ "' WHERE OrderID='" + Main.user.getOrder().getOrderID() + "' and UserID = '"
										+ Main.user.getUserID() + "';");
						try {
							Main.client.sendMessageToServer(msgServer);
							synchronized (Main.client) {
								Main.client.wait();
							}
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					} else {
						showPopUp("ERROR", "Error dialog", null, "Please fill all the required fieilds!");
					}
				}
				if (!greetingPane.isDisable() && Main.user.getOrder().getGreeting() == null) {
					if (greetingText.getText().isEmpty() == false) {
						String greeting = greetingText.getText();
						Main.user.getOrder().setGreeting(greeting);
						msgServer.put("msgType", "update");
						msgServer.put("query",
								"UPDATE zrle.order SET Greeting='" + greeting + "' WHERE OrderID='"
										+ Main.user.getOrder().getOrderID() + "' and UserID = '" + Main.user.getUserID()
										+ "';");
						try {
							Main.client.sendMessageToServer(msgServer);
							synchronized (Main.client) {
								Main.client.wait();
							}
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					} else {
						showPopUp("ERROR", "Error dialog", null, "Please fill all the required fieilds!");
					}
				}
			} else {
				showPopUp("ERROR", "Error dialog", null, "Please fill all the required fieilds!");
			}
			msgServer.put("msgType", "select");
			msgServer.put("query", "SELECT  MAX(PaymentID) FROM zrle.`payment` ;");
			try {
				Main.client.sendMessageToServer(msgServer);
				synchronized (Main.client) {
					Main.client.wait();
				}
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			answer = (ArrayList<String>) Main.client.getMessage();
			if (Main.user.getPaymentAccounts().get(0).getSubscriptionType().equals("0")
					|| Main.user.getPaymentAccounts().get(0).getSubscriptionType().equals("1")
							&& Main.user.getOrder().getPayment() == null) {
				Double newbalance = Main.user.getPaymentAccounts().get(0).getBalance()
						- Main.user.getOrder().getTotalPrice();
				Main.user.getOrder().setPayment(new Payment((Integer.parseInt(answer.get(0) + 1)),
						Integer.parseInt(Main.user.getOrder().getOrderID()), Main.user.getOrder().getTotalPrice()));
				msgServer.put("msgType", "insert");
				msgServer.put("query",
						"INSERT INTO payment (PaymentID, OrderID, TotalPrice)VALUES('"
								+ (Integer.parseInt(answer.get(0)) + 1) + "','" + Main.user.getOrder().getOrderID()
								+ "'," + Main.user.getOrder().getTotalPrice() + ")");
				Double totalPrice = Main.user.getOrder().getTotalPrice()
						+ Main.user.getPaymentAccounts().get(0).getSubTotal();
				System.out.println("INSERT INTO payment (PaymentID, OrderID, TotalPrice)VALUES('"
						+ (Integer.parseInt(answer.get(0)) + 1) + "','" + Main.user.getOrder().getOrderID() + "',"
						+ Main.user.getOrder().getTotalPrice() + ")");
				try {
					Main.client.sendMessageToServer(msgServer);
					synchronized (Main.client) {
						Main.client.wait();
					}
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				msgServer.put("msgType", "update");
				msgServer.put("query", "update paymentaccount set BuyingInSub=" + totalPrice + " where UsersAccountID='"
						+ Main.user.getUserID() + "'; ");
				try {
					Main.client.sendMessageToServer(msgServer);
					synchronized (Main.client) {
						Main.client.wait();
					}
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				showPopUp("INFORMATION", "Information dialog", null,
						"You have paid,we will take money from your balance when the subscribe end!");
			} else {
				if (creditCardCheckBox.isSelected()) {
					Double newbalance = Main.user.getPaymentAccounts().get(0).getBalance()
							- Main.user.getOrder().getTotalPrice();
					if (newbalance > 0) {
						msgServer.put("msgType", "insert");
						msgServer.put("query",
								"INSERT INTO payment (PaymentID, OrderID, TotalPrice,PaymentType)VALUES('"
										+ (Integer.parseInt(answer.get(0)) + 1) + "','"
										+ Main.user.getOrder().getOrderID() + "',"
										+ Main.user.getOrder().getTotalPrice() + "','" + PaymentMethod + ")");
						System.out.println("INSERT INTO payment (PaymentID, OrderID, TotalPrice,PaymentType)VALUES('"
								+ (Integer.parseInt(answer.get(0)) + 1) + "','" + Main.user.getOrder().getOrderID()
								+ "'," + Main.user.getOrder().getTotalPrice() + "','" + PaymentMethod + ")");
						try {
							Main.client.sendMessageToServer(msgServer);
							synchronized (Main.client) {
								Main.client.wait();
							}
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						msgServer.put("msgType", "update");
						msgServer.put("query", "Update zrle.paymentaccount set zrle.paymentaccount.Balance='"
								+ newbalance + "' where UsersAccountID='" + Main.user.getUserID() + "'");
						showPopUp("ERROR", "Error dialog", null, "Please fill all the required fieilds!");

					} else {
						showPopUp("ERROR", "Error dialog", null, "Error:Please update your balance then pay!");
						return;
					}
				} else if (cashCheckBox.isSelected()) {

					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Confirmation Dialog");
					alert.setHeaderText("You have choose the cash payment method!");
					alert.setContentText(
							"You must come to take your order more 3 hours\n If you don't come we will block your account!");

					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() == ButtonType.OK) {
						msgServer.put("msgType", "insert");
						msgServer.put("query",
								"INSERT INTO payment (PaymentID, OrderID, TotalPrice,PaymentType)VALUES('"
										+ (Integer.parseInt(answer.get(0)) + 1) + "','"
										+ Main.user.getOrder().getOrderID() + "',"
										+ Main.user.getOrder().getTotalPrice() + ",'" + PaymentMethod + "')");
						System.out.println("INSERT INTO payment (PaymentID, OrderID, TotalPrice,PaymentType)VALUES('"
								+ (Integer.parseInt(answer.get(0)) + 1) + "','" + Main.user.getOrder().getOrderID()
								+ "'," + Main.user.getOrder().getTotalPrice() + ",'" + PaymentMethod + "')");
						try {
							Main.client.sendMessageToServer(msgServer);
							synchronized (Main.client) {
								Main.client.wait();
							}
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					} else {
						return;
					}
					showPopUp("INFORMATION", "Information dialog", null, "\t\t\t Your OrderID is:"
							+ Main.user.getOrder().getOrderID() + "\nPlease save it,you may need it!");
				} else {
					showPopUp("ERROR", "Error dialog", null, "You must choose payment method!");
				}
			}
			if (creditCardCheckBox.isSelected() == true || cashCheckBox.isSelected() == true
					|| (Main.user.getPaymentAccounts().get(0).getPaymentType().equals("Sub")
							&& Main.user.getOrder().getPayment() == null)) {
				if (!shippingPane.isDisable()) {
					LocalDate Date = date.getValue();
					LocalTime time = Time.getValue();
					DateTimeFormatter Dateformatter = DateTimeFormatter.ofPattern("YYYY-MM-DD");
					String formattedString1 = Date.format(Dateformatter);
					DateTimeFormatter Timeformatter = DateTimeFormatter.ofPattern("HH:mm:ss");
					String formattedString2 = time.format(Timeformatter);
					DateTime = formattedString1 + " " + formattedString2;
					// System.out.println(DateTime);
				} else {
					LocalDate Date = LocalDate.now();
					LocalTime time = LocalTime.now().plus(3, ChronoUnit.HOURS);
					DateTimeFormatter Dateformatter = DateTimeFormatter.ofPattern("YYYY-MM-DD");
					String formattedString1 = Date.format(Dateformatter);
					DateTimeFormatter Timeformatter = DateTimeFormatter.ofPattern("HH:mm:ss");
					String formattedString2 = time.format(Timeformatter);
					DateTime = formattedString1 + " " + formattedString2;
				}
				msgServer.put("msgType", "update");
				msgServer.put("query",
						"UPDATE `zrle`.`order` SET OrderStatus='1', OrderDate=now(),FinishDate='" + DateTime
								+ "',PaymentID=" + (Integer.parseInt(answer.get(0)) + 1) + " where OrderID='"
								+ Main.user.getOrder().getOrderID() + "' and UserID='" + Main.user.getUserID() + "'");
				System.out.println("UPDATE `zrle`.`order` SET OrderStatus='1', OrderDate=now() where OrderID='"
						+ Main.user.getOrder().getOrderID() + "' and UserID='" + Main.user.getUserID() + "'");
				try {
					Main.client.sendMessageToServer(msgServer);
					synchronized (Main.client) {
						Main.client.wait();
					}
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				Main.user.getOrder().setOrderStatus(1);
				ArrayList<Product> prodlis = Main.user.getOrder().getOrderProudcts();
				prodlis.clear();
				Main.user.getOrder().setOrderProudcts(prodlis);

			}
		} else {
			showPopUp("ERROR", "Error dialog", null, "You already paid for this order!");
			return;
		}

	}
/** 
 * disable the shipping pane
 */
	public void disableShipping() {
		if (!shippingPane.isDisable()) {
			if (shippingAddress.getText().isEmpty() && shippingTelephone.getText().isEmpty()) {
				shippingPane.setDisable(true);
			} else {
				showPopUp("ERROR", "Error dialog", null,
						"Error you can't disable the shipping option beacuce some fields is not empty!\n******* Clear it then you can disable*******");
			}
		} else {
			shippingPane.setDisable(false);
		}

	}

	public static Optional<ButtonType> showPopUp(String typeOfPopUpString, String title, String header,
			String content) {
		Alert alert = null;

		switch (typeOfPopUpString) {
		case "CONFIRMATION":
			alert = new Alert(AlertType.CONFIRMATION);
			break;
		case "INFORMATION":
			alert = new Alert(AlertType.INFORMATION);
			break;
		case "ERROR":
			alert = new Alert(AlertType.ERROR);
			break;
		}

		if (title != null)
			alert.setTitle(title);
		if (header != null)
			alert.setHeaderText("111");
		if (content != null)
			alert.setContentText(content);

		Optional<ButtonType> result = alert.showAndWait();

		return result;
	}
/** 
 * disable the gretting pane
 */
	public void disableGreeting() {
		if (!greetingPane.isDisable()) {
			System.out.println(greetingText.getText().isEmpty());
			if (greetingText.getText().isEmpty()) {
				greetingPane.setDisable(true);
			} else {
				showPopUp("ERROR", "Error dialog", null,
						"Error you can't disable the greeting option beacuce the greeting text field is not empty!\n******* Clear it then you can disable*******");
			}
		} else {
			greetingPane.setDisable(false);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Image checkoutimg = new Image(getClass().getResource("/images/Checkout.png").toString());
		Checkoutimage.setImage(checkoutimg);
		Image personalinfoimg = new Image(getClass().getResource("/images/Personalnfo.png").toString());
		personlinfoimage.setImage(personalinfoimg);
		Image shippingdeatilsimg = new Image(getClass().getResource("/images/ShippingDeatils.png").toString());
		shippingdeatilsimage.setImage(shippingdeatilsimg);
		Image greetingimg = new Image(getClass().getResource("/images/Greeting.png").toString());
		greetingimage.setImage(greetingimg);
		Image paymentmethodimg = new Image(getClass().getResource("/images/SelectPaymentMethod.png").toString());
		paymentmethodimage.setImage(paymentmethodimg);
		Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
		// pane.setPrefSize( visualBounds.getWidth(),visualBounds.getHeight());
		// scroll.setPrefSize(
		// visualBounds.getWidth(),visualBounds.getHeight());
		// Pane pane1 = new Pane();
		Text text = new Text("Order total   " + Main.user.getOrder().getTotalPrice() + "NIS");
		text.setFont(Font.font("Helvetica", FontWeight.BOLD, 16));
		text.setLayoutX(0);
		text.setLayoutY(18);
		// pane1.getChildren().add(text);
		// pane1.setLayoutX(268);
		// 1pane1.setLayoutY(945);
		pane.getChildren().add(text);
		RequiredFieldValidator FirstNamevalidator = new RequiredFieldValidator();
		FirstNamevalidator.setMessage("First name Can't be empty");
		FirstNamevalidator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class).glyph(FontAwesomeIcon.WARNING)
				.size(EM1).styleClass(ERROR).build());
		firstName.getValidators().add(FirstNamevalidator);
		firstName.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal) {
				firstName.validate();
			}
		});
		RequiredFieldValidator LastNamevalidator = new RequiredFieldValidator();
		LastNamevalidator.setMessage("Last name Can't be empty");
		LastNamevalidator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class).glyph(FontAwesomeIcon.WARNING)
				.size(EM1).styleClass(ERROR).build());
		lastName.getValidators().add(LastNamevalidator);
		lastName.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal) {
				lastName.validate();
			}
		});
		RequiredFieldValidator Emailvalidator = new RequiredFieldValidator();
		Emailvalidator.setMessage("Email Can't be empty");
		Emailvalidator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class).glyph(FontAwesomeIcon.WARNING).size(EM1)
				.styleClass(ERROR).build());
		Email.getValidators().add(Emailvalidator);
		Email.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal) {
				Email.validate();
			}
		});

		RequiredFieldValidator Telephonevalidator = new RequiredFieldValidator();
		Telephonevalidator.setMessage("Telephone Can't be empty");
		Telephonevalidator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class).glyph(FontAwesomeIcon.WARNING)
				.size(EM1).styleClass(ERROR).build());
		Telephone.getValidators().add(Telephonevalidator);
		Telephone.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal) {
				Telephone.validate();
			}
		});
		RequiredFieldValidator ShippingAddressvalidator = new RequiredFieldValidator();
		ShippingAddressvalidator.setMessage("Address Can't be empty");
		ShippingAddressvalidator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class).glyph(FontAwesomeIcon.WARNING)
				.size(EM1).styleClass(ERROR).build());
		shippingAddress.getValidators().add(ShippingAddressvalidator);
		shippingAddress.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal) {
				shippingAddress.validate();
			}
		});
		RequiredFieldValidator shippingTelephonevalidator = new RequiredFieldValidator();
		shippingTelephonevalidator.setMessage("Telephone Can't be empty");
		shippingTelephonevalidator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
				.glyph(FontAwesomeIcon.WARNING).size(EM1).styleClass(ERROR).build());
		shippingTelephone.getValidators().add(shippingTelephonevalidator);
		shippingTelephone.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal) {
				shippingTelephone.validate();
			}
		});
		shippingPane.setDisable(true);
		greetingPane.setDisable(true);
		if (Main.user.getPaymentAccounts().get(0).getSubscriptionType().equals("1")
				|| (Main.user.getPaymentAccounts().get(0).getSubscriptionType().equals("0"))) {
			PaymentPange.setDisable(true);
			Text text1 = new Text("You are a subcriber no need to choose payment type!");
			text1.setLayoutX(100);
			text1.setLayoutY(100);
			text1.setFont(Font.font("Helvetica", FontWeight.BOLD, 20));
			text1.setFill(Color.RED);
			text1.setUnderline(true);
			PaymentPange.getChildren().add(text1);

		}

	}

}
