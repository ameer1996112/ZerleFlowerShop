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

import Entity.Address;
import Entity.BranchCatalog;
import Entity.Order;
import Entity.PaymentAccount;
import Entity.Product;
import Entity.ShopCatalog;
import Entity.User;
import application.ClientConnection;
import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * This is the controller class for: "SecretaryCourseToClassController.fxml"
 * 
 * @author Ameer amer
 */

public class LoginController implements Initializable {

	/** ComboBox to display branch choice */
	@FXML
	private ComboBox<String> branchComboBox;
	@FXML
	private TextField id;

	/** PasswordField for user password */
	@FXML
	private PasswordField password;

	/** TextField For server ip */
	@FXML
	private TextField ip;

	/** TextField For server port */
	@FXML
	private TextField port;

	/** Label to display messages to user */
	@FXML
	private Label guiMeg;

	/** Answer from server */
	private HashMap<String, String> answer = null;

	/** Message to server */
	private HashMap<String, String> msgServer = new HashMap<String, String>();

	/**
	 * Check all input and display user messages accordingly, if all Fields are
	 * met and valid, the user's specific welcome window open's
	 */
	private final ObservableList<String> options = FXCollections.observableArrayList("Haifa-Zrle", "Nazareth-Zrle",
			"Carmiel-Zrle", "Reneh-Zrle", "TelAviv-Zrle"); 
	private ArrayList<String> answerMsg;
	private ArrayList<byte[]> result;
	private ArrayList<String> proudctList = new ArrayList<String>();
	public Order order = null;

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
		String BranchID = Main.user.getBranch();
		String UserID = Main.user.getUserID();
		Order order = new Order(OrderID, BranchID, UserID);
		msgServer.put("msgType", "insert");
		msgServer.put("query",
				"INSERT INTO zrle.`order` (OrderID, UserID, BranchID,OrderStatus,DeliveryID,OrderDate,PaymentID)VALUES('"
						+ OrderID + "','" + UserID + "','" + BranchID + "'  ,'0',null,null,null)");
		System.out.println(
				"INSERT INTO zrle.`order` (OrderID, UserID, BranchID,OrderStatus,DeliveryID,OrderDate,PaymentID)VALUES('"
						+ OrderID + "','" + UserID + "','" + BranchID + "'  ,'0',null,null,null)");
		try {
			Main.client.sendMessageToServer(msgServer);
			synchronized (Main.client) {
				Main.client.wait();
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		Integer Intanswer = (Integer) Main.client.getMessage();
		if (Intanswer == null) {
			System.out.println("Error creating new order!");
		}
		return order;

	}

	public void loadUserOrders() {
		int OrdersNumber = checkOrders();
		int flag = 1;
		int i = 0;
		String OrderID;// we use this variable to define the orderID number
		for (i = 0; i < OrdersNumber; i++) {
			if (answerMsg.get(i).equals("0")) {
				flag = 0;
			}
		}

		ArrayList<HashMap<String, String>> answerFromServer;
		if (OrdersNumber == 0 || flag == 1) {
			OrderID = getOrderID();
			order = createOrder(OrderID);// create a new order instance and
											// we
											// initliaze it with the
											// returned
											// orderID
			order.setOrderID(OrderID);
			System.out.println("New order created for" + Main.user.getUserID() + "and the OrderID :" + OrderID + "\n");
		} else {
			msgServer.put("msgType", "loadOrders");
			// System.out.println(Main.user.getUserID());
			msgServer.put("UserID", Main.user.getUserID());
			try {
				Main.client.sendMessageToServer(msgServer);
				synchronized (Main.client) {
					Main.client.wait();
				}
				answerFromServer = (ArrayList<HashMap<String, String>>) Main.client.getMessage();
				for (i = 0; i < OrdersNumber; i++) {
					String BranchID = answerFromServer.get(i).get("BranchID");
					String UserID = Main.user.getUserID();
					String orderID = answerFromServer.get(i).get("OrderID");
					// Order(String orderID, String userID, String branchID,
					// String
					// deliveryID, String date, String paymentID,
					// ArrayList<Product> orderProudcts) {
					// System.out.println(orderID);
					order = new Order(orderID, UserID, BranchID);
				}
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		if (order.getOrderProudcts() == null) {
			ArrayList<Product> proudctList = new ArrayList<Product>();
			int[] proudctsQuan = new int[100];
			msgServer.put("msgType", "loadOrderProudcts");
			msgServer.put("OrderID", order.getOrderID());
			try {
				Main.client.sendMessageToServer(msgServer);
				synchronized (Main.client) {
					Main.client.wait();
				}
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			answerFromServer = (ArrayList<HashMap<String, String>>) Main.client.getMessage();
			for (i = 0; i < answerFromServer.size(); i++) {
				Product proudct = new Product(answerFromServer.get(i).get("ProudctID"),
						answerFromServer.get(i).get("Name"), answerFromServer.get(i).get("Type"),
						answerFromServer.get(i).get("Color"), answerFromServer.get(i).get("Price"));
				int quantity = Integer.parseInt(answerFromServer.get(i).get("Quantitiy"));
				proudctsQuan[Integer.parseInt(answerFromServer.get(i).get("ProudctID"))] = quantity;
				// proudctsQuan.add(Integer.parseInt(quantity));
				proudctList.add(proudct);
			}
			order.setOrderProudcts(proudctList);
			order.setQuantity(proudctsQuan);
			Main.user.setOrder(order);
			System.out.println(proudctsQuan);
		}
	}

	public void loadCatalogs() {
		int i = 0;// index for the loop
		int CatalogSize = 0;// we use these variable for the ShopCatalogSize
		// end checking if the UserID have order with stats 0
		/* if the user id has no opend orders or has order with status 1 */
		/* we create a new order in the database */
		ArrayList<Product> deatils = new ArrayList<Product>();
		// Main.catalog = new ShopCatalog("zrle", "1", deatils.size());
		ShopCatalog shopCat = new ShopCatalog("Zrle", "1", deatils.size());
		deatils = shopCat.loadProudctDeatils("1");
		/*
		 * for (int i = 0; i < 2; i++) { System.out.println(deatils.get(i)); }
		 */
		shopCat.setCatlaogProducts(deatils);
		shopCat.setSize(deatils.size());
		ArrayList<Product> tempdeatils = new ArrayList<Product>(deatils.size());
		Main.tempShopCatalog = new ShopCatalog("Zrle", "1", deatils.size());
		for (i = 0; i < deatils.size(); i++) {

			Main.tempShopCatalog.getCatlaogProducts().add(deatils.get(i).clone());
		}
		msgServer.put("msgType", "loadBranchCatalog");
		msgServer.put("BranchID", Main.user.getBranch());
		msgServer.put("CatalogID", "1");
		try {
			Main.client.sendMessageToServer(msgServer);
			synchronized (Main.client) {
				Main.client.wait();
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		int BranchID = Integer.parseInt(Main.user.getBranch());

		BranchCatalog branchCat = new BranchCatalog(BranchID, shopCat);
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<String, String>> answerFromServer = (ArrayList<HashMap<String, String>>) Main.client
				.getMessage();
		// System.out.println(answerFromServer.toString());
		for (i = 0; i < answerFromServer.size(); i++) {
			HashMap<String, String> HashList = new HashMap<String, String>();
			HashList.put("ProudctID", answerFromServer.get(i).get("ProudctID"));
			HashList.put("SalePrice", answerFromServer.get(i).get("SalePrice"));
			list.add(HashList);
		}
		branchCat.setSaleProudcts(list);
		Main.salelist = list;
		for (i = 0; i < deatils.size(); i++) {
			for (int j = 0; j < Main.salelist.size(); j++) {
				// System.out.println(Main.catalog.getShopCat().getCatlaogProducts().get(i).getProductID());
				if (shopCat.getCatlaogProducts().get(i).getProductID().toString()
						.equals(Main.salelist.get(j).get("ProudctID"))) {
					shopCat.getCatlaogProducts().get(i).setPrice(Main.salelist.get(j).get("SalePrice"));
				}
			}

		}
		Main.catalog = branchCat;
	}

	public void OnLogin(ActionEvent e) throws InterruptedException {

		// Establish connection to server.

		try {
			Integer.parseInt(port.getText());
		} catch (Exception ex) {
			guiMeg.setText("Port field: input not valid!");
			return;
		}

		if (Main.client == null)
			if (!ip.getText().isEmpty() && !port.getText().isEmpty())
				Main.client = new ClientConnection(ip.getText(), Integer.parseInt(port.getText()));
			else {
				guiMeg.setText("Connection to server failed!");
				return;
			}

		// Send message to server with an new thread & wait for answer.

		if (!id.getText().isEmpty() && !password.getText().isEmpty()) {
			msgServer.put("msgType", "Login");
			msgServer.put("id", id.getText());
			msgServer.put("passwrd", password.getText());

			try {
				Main.client.sendMessageToServer(msgServer);
			} catch (Exception exp) {
				guiMeg.setText("Server fatal error!");
				return;
			}

			synchronized (Main.client) {
				Main.client.wait();
			}
			answer = (HashMap<String, String>) Main.client.getMessage();
		} else if (Main.client != null) {
			guiMeg.setText("Please fill al fields..");
			return;
		}

		// Process answer from server.

		if (answer != null && answer.get("Valid").equals("true")) {

			Stage Curstage = (Stage) id.getScene().getWindow();
			Curstage.close();

			// ((Node)(e.getSource())).getScene().getWindow().hide(); // Close
			// login window.
			// String userID, String email, Address address, String phone,
			// String type, String BranchID) {
			msgServer.put("msgType", "select");
			msgServer.put("query", "SELECT  ProductID from product ;");
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
			}
			for (int i = 0; i < answerMsg.size(); i++) {
				proudctList.add(answerMsg.get(i));
			}
			msgServer.clear();
			try {
				if (Main.proudctList == null) {
					Main.proudctList = new ArrayList<Product>();
					Product proudct = new Product();
					Main.proudctList = proudct.loadProudctDeatils("1");

				}
				if (checkIfImagesExist() == true) {

					msgServer.put("msgType", "CatalogLoad");// we set the
															// msgType to
															// catalogLoad
					msgServer.put("query", "SELECT * FROM product");

					msgServer.put("filePath", "C:\\Zrlefiles\\ProudctsImages\\image");

					Main.client.sendMessageToServer(msgServer);
					synchronized (Main.client) {
						try {
							Main.client.wait();
						} catch (InterruptedException ex) {
							// TODO Auto-generated catch block
							ex.printStackTrace();
						}
					}
					File file = new File("C:\\Zrlefiles\\ProudctsImages\\image");

					// if the directory does not exist, create it
					result = (ArrayList<byte[]>) Main.client.getMessage();
					System.out.println(result.size());
					for (int i = 0; i < result.size(); i++) {
						System.out.println(result.get(i));
						ByteArrayInputStream bis = new ByteArrayInputStream(result.get(i));
						Iterator<?> readers = ImageIO.getImageReadersByFormatName("jpg");

						// ImageIO is a class containing static methods for
						// locating
						// ImageReaders
						// and ImageWriters, and performing simple encoding and
						// decoding.

						ImageReader reader = (ImageReader) readers.next();
						Object source = bis;
						ImageInputStream iis = ImageIO.createImageInputStream(source);
						reader.setInput(iis, true);
						ImageReadParam param = reader.getDefaultReadParam();

						BufferedImage image = reader.read(0, param);
						// got an image file

						BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
								BufferedImage.TYPE_INT_RGB);
						// bufferedImage is the RenderedImage to be written

						Graphics2D g2 = bufferedImage.createGraphics();
						g2.drawImage(image, null, null);

						File imageFile = new File("C:\\Zrlefiles\\ProudctsImages\\image" + proudctList.get(i) + ".jpg");
						ImageIO.write(bufferedImage, "jpg", imageFile);
					}
				}
				Address address = new Address(answer.get("AddressID"), answer.get("City"), answer.get("Street"),
						answer.get("ZipCode"));
				Main.user = new User(answer.get("Username"), answer.get("Email"), address, answer.get("Phone"),
						answer.get("Type"), Integer.toString(branchComboBox.getSelectionModel().getSelectedIndex()));
				msgServer.put("msgType", "select");
				msgServer.put("query", "select * from paymentaccount where paymentaccount.UsersAccountID='"
						+ Main.user.getUserID() + "' and paymentaccount.BranchID='" + Main.user.getBranch() + "'");
				System.out.println("select * from paymentaccount where paymentaccount.UsersAccountID='"
						+ Main.user.getUserID() + "' and paymentaccount.BranchID='" + Main.user.getBranch() + "'");
				try {
					Main.client.sendMessageToServer(msgServer);
					synchronized (Main.client) {
						Main.client.wait();
					}
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				answerMsg = (ArrayList<String>) Main.client.getMessage();
				if (answerMsg.size() != 0) {
					Main.user.setPayment(new PaymentAccount(answerMsg.get(0), answerMsg.get(1), answerMsg.get(2),
							Double.parseDouble(answerMsg.get(3)), Integer.parseInt(answerMsg.get(4)),
							Integer.parseInt(answerMsg.get(5)), Double.parseDouble(answerMsg.get(6))));
				}
				loadCatalogs();
				loadUserOrders();
				Main.openMain(answer.get("Type"), e);

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		} else
			guiMeg.setText((String) answer.get("ErrMsg"));
	}

	private boolean checkIfImagesExist() {
		for (int i = 0; i < proudctList.size(); i++) {
			File f = new File("C:\\Zrlefiles\\ProudctsImages\\image" + (i + 1) + ".jpg");

			if (f.exists()) {
				return false;
			}
		}
		return true;

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		id.setText("Ameer");
		password.setText("124");
		ip.setText("127.0.0.1");
		port.setText("5555");
		branchComboBox.setItems(options);

		id.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal) {
				if (id.getText().equals("Jhony") || id.getText().equals("Firas") || id.getText().equals("Waled")
						|| id.getText().equals("Wisam") || id.getText().equals("Jawad")) {
					branchComboBox.setDisable(true);
					guiMeg.setText("Your are branch manager\nYou don't need to choose branch!");
				}
			}
		});
	}
}