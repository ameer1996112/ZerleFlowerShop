package Controller;

import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTextArea;

import Entity.Order;
import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * this controller for the cancel order operation
 *
 * @author Ameer amer
 *
 */
public class CancelOrderController extends CatalogOrderController implements Initializable {
	@FXML
	private AnchorPane pane;// paramter injected by fxml file
	@FXML
	private VBox OrderBox;// paramter injected by fxml file
	@FXML
	private AnchorPane anchorpane;// paramter injected by fxml file
	@FXML
	private ComboBox<String> OrderCombo;// this combobox to show the customer
										// orders

	@FXML
	private TextField dateField;// this field to show orders date

	@FXML
	private TextField orderCost;// this field to show the orders final cost
	@FXML
	private ImageView StoreImage;// this image the the store logo image
	@FXML
	private ImageView BackgroundImage;// this image for the background image
	private HashMap<String, String> msgServer = new HashMap<String, String>();// we
																				// use
																				// this
																				// hashmap
																				// for
																				// sending
																				// message
																				// to
																				// server
	private ArrayList<String> answerMsg;// this answerMsg for getting back
										// message from servr
	private Integer Intanswer;// server answer for select update and etc.,
	private ArrayList<Order> orderList = new ArrayList<Order>();// we usr this
																// array list to
																// store the
																// orders of the
																// customer
	private final ObservableList<String> options = FXCollections.observableArrayList();// this
																						// list
																						// to
																						// show
																						// orders
																						// id
	private static final DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");// we
																						// format
																						// the
																						// date
	private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");// format
																									// the
																									// time
	public int half = 0, full = 0, none = 0;// using this variables to show
											// refund
/**
 * this method for calculating the compenstation
 * @param wanted_date
 */
	private void calcCompensation(LocalDateTime wanted_date) {
		LocalDateTime now = LocalDateTime.now();

		if (now.plusHours(3).isBefore(wanted_date)) {

			full = 1;
		}

		else if (now.plusHours(1).isBefore(wanted_date)) {

			half = 1;

		} else {

			none = 1;
		}

	}
/**
 * we come to this method when we click cancel order button 
 * @param event
 */
	public void CancelOrder(ActionEvent event) {
		int comp = 0;
		String date = orderList.get(OrderCombo.getSelectionModel().getSelectedIndex()).getDate();
		String year = date.substring(0, 4);
		String month = date.substring(5, 7);
		String day = date.substring(8, 10);
		String hour = date.substring(11, 13);
		String min = date.substring(14, 16);
		String sec = date.substring(17, 19);
		LocalDate wanted_date = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
		LocalDateTime wanted_time = wanted_date.atTime(Integer.parseInt(hour), Integer.parseInt(min),
				Integer.parseInt(sec));
		calcCompensation(wanted_time);
		double refund;
		if (full == 1) {
			refund = Main.user.getOrder().getTotalPrice();
		} else if (half == 1) {
			refund = (Main.user.getOrder().getTotalPrice()) / 2;
		} else {
			refund = 0;
		}
		//deleting the order from the order table in database
		msgServer.put("msgType", "delete");
		msgServer.put("query",
				"DELETE FROM payment Where EXISTs(SELECT OrderID FROM delivery where delivery.OrderID=payment.OrderID and  delivery.OrderID='"
						+ OrderCombo.getSelectionModel().getSelectedItem()
						+ "');delete from zrle.order where zrle.order.OrderID='"
						+ OrderCombo.getSelectionModel().getSelectedItem() + "' and UserID='" + Main.user.getUserID()
						+ "';");
		System.out.println(
				"DELETE FROM payment Where EXISTs(SELECT OrderID FROM delivery where delivery.OrderID=payment.OrderID and  delivery.OrderID='"
						+ OrderCombo.getSelectionModel().getSelectedItem()
						+ "');delete from zrle.order where zrle.order.OrderID='"
						+ OrderCombo.getSelectionModel().getSelectedItem() + "' and UserID='" + Main.user.getUserID()
						+ "'");
		try {
			Main.client.sendMessageToServer(msgServer);
			synchronized (Main.client) {
				Main.client.wait();
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		Intanswer = (Integer) Main.client.getMessage();
		//if some error happend 
		if (Intanswer == null) {
			Main.showPopUp("ERROR", "Error dialog", null, "some error occured whilte canceling the order!");
		} else {//else we update the balance of the customer with the refund
			msgServer.put("msgType", "update");
			msgServer.put("query",
					"update  set Balance='" + (Main.user.getPaymentAccounts().get(0).getBalance() + refund)
							+ "' where UsersAccountID='" + Main.user.getUserID() + "'");
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
				Main.showPopUp("ERROR", "Error dialog", null, "some error occured whilte canceling the order!");
			} else {
				Main.showPopUp("INFORMATION", "Information dialog", null, "The order have been canceled sucssefully!");
				OrderCombo.getItems().remove(OrderCombo.getSelectionModel().getSelectedItem());
				dateField.clear();
				orderCost.clear();
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadPictures();
		loadOrders();

	}

	@SuppressWarnings("unchecked")
	private void loadOrders() {
		msgServer.put("msgType", "select");
		msgServer.put("query",
				"SELECT payment.OrderID,OrderStatus,zrle.order.FinishDate,payment.TotalPrice FROM zrle.`order`,payment where zrle.order.OrderID=payment.OrderID and UserID='"
						+ Main.user.getUserID() + "' and OrderStatus='1' and BranchID='" + Main.user.getBranch() + "'");
		System.out.println(
				"SELECT payment.OrderID,OrderStatus,zrle.order.FinishDate,payment.TotalPrice FROM  zrle.`order`,payment where zrle.order.OrderID=payment.OrderID and UserID='"
						+ Main.user.getUserID() + "' and OrderStatus='1' and BranchID='" + Main.user.getBranch() + "'");
		try {
			Main.client.sendMessageToServer(msgServer);
			synchronized (Main.client) {
				Main.client.wait();
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		answerMsg = (ArrayList<String>) Main.client.getMessage();
		for (int i = 0; i < answerMsg.size(); i += 4) {
			String OrderID = answerMsg.get(i);
			// options.add(OrderID);
			String OrderDate = answerMsg.get(i + 2);
			// dateField.setText(OrderDate);
			String OrderCost = answerMsg.get(i + 3);
			// orderCost.setText(OrderCost);
			// order.setDate(OrderDate);
			// format the wanted date
			// order.setTotalPrice(Double.parseDouble(OrderCost));
			Order order = new Order(Main.user.getUserID(), Double.parseDouble(OrderCost), OrderDate);
			options.add(OrderID);
			orderList.add(order);

		}
		OrderCombo.setItems(options);

	}

	@FXML
	void showDeatils(ActionEvent event) {
		Order order = orderList.get(OrderCombo.getSelectionModel().getSelectedIndex());
		String OrderID = order.getOrderID();
		String OrderDate = order.getDate();
		dateField.setText(OrderDate);
		String OrderCost = Double.toString(order.getTotalPrice());
		orderCost.setText(OrderCost);
		order.setDate(OrderDate);
		// format the wanted date
		order.setTotalPrice(Double.parseDouble(OrderCost));
	}

}
