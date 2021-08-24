package application;
// This file contains material supporting section 3.7 of the textbook:

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.JOptionPane;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ocsf.server.*;
import java.util.Date;

/**
 * This class overrides some of the methods in the abstract superclass in order
 * to give more functionality to the server.
 *
 * @author Ameer Amer
 * 
 */
public class MainServer extends AbstractServer {
	// Class variables *************************************************

	/**
	 * The default port to listen on.
	 */
	final public static int DEFAULT_PORT = 5555;
	private LogController logController;
	private Connection DBConn;
	private String assFilesDirPath = "C:\\Zrlefiles\\ProudctsImages\\";
	private String subFilesDirPath = "C:\\Zrlefiles\\submissions\\"; // Constructors
																		// ****************************************************

	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port
	 *            The port number to connect on.
	 */
	public MainServer(int port) {
		super(port);
	}

	// Instance methods ************************************************

	/**
	 * This method handles any messages received from the client.
	 *
	 * @param msg
	 *            The message received from the client.
	 * @param client
	 *            The connection from which the message originated.
	 */
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {

		if (msg instanceof byte[]) {
			byte byteArray[] = (byte[]) msg;
		} else if (msg instanceof HashMap<?, ?>) {
			@SuppressWarnings("unchecked")
			HashMap<String, String> clientMsg = (HashMap<String, String>) msg;

			// shows the received msg to the event log
			logController.showMsg("Message received: " + clientMsg.get("msgType") + " from " + client);

			// check the msg type
			if (clientMsg.get("msgType").equals("Login")) {
				login(clientMsg, client);
			} else if (clientMsg.get("msgType").equals("loadBranchCatalog")) {
				loadBranch(clientMsg, client);
			} else if (clientMsg.get("msgType").equals("loadOrders")) {
				loadOrders(clientMsg, client);
			} else if (clientMsg.get("msgType").equals("loadOrderProudcts")) {
				loadOrderProudcts(clientMsg, client);
			} else if (clientMsg.get("msgType").equals("CatalogLoad")) {
				loadCatalogImages(clientMsg, client);
			} else if (clientMsg.get("msgType").equals("loadDeatils")) {
				loadCatalaogDeatils(clientMsg, client);
			} else if (clientMsg.get("msgType").equals("select")) {
				selectQuery(clientMsg, client);
			} else if (clientMsg.get("msgType").equals("update")) {
				updateQuery(clientMsg, client);
			} else if (clientMsg.get("msgType").equals("delete")) {
				updateQuery(clientMsg, client);
			} else if (clientMsg.get("msgType").equals("save")) {
				save(clientMsg, client);
			} else if (clientMsg.get("msgType").equals("saveImage")) {
				HashMap<String, Object> clientMsg2 = (HashMap<String, Object>) msg;
				saveImage(clientMsg2, client);
			} else if (clientMsg.get("msgType").equals("insert")) {
				updateQuery(clientMsg, client);
			} else if (clientMsg.get("msgType").equals("loadReports")) {
				try {
					loadReportsDetails(clientMsg, client);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private void saveImage(HashMap<String, Object> clientMsg, ConnectionToClient client) {
		byte[] buffer = (byte[]) clientMsg.get("byteArr");
		System.out.println(buffer);
		String filePath = (String) clientMsg.get("path");
		System.out.println(filePath);
		// ByteArrayInputStream bis = new ByteArrayInputStream(buffer);
		// Iterator<?> readers = ImageIO.getImageReadersByFormatName("jpg");

		// ImageIO is a class containing static methods for locating
		// ImageReaders
		// and ImageWriters, and performing simple encoding and
		// decoding.
		if (buffer != null) {

			try {
				ByteArrayInputStream bis = new ByteArrayInputStream(buffer);
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

				File imageFile = new File(filePath);
				ImageIO.write(bufferedImage, "jpg", imageFile);
				client.sendToClient(1);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private void save(HashMap<String, String> clientMsg, ConnectionToClient client) {
		try {
			String filePath = clientMsg.get("path");
			InputStream inputStream = new FileInputStream(new File(filePath));
			String ProductID = clientMsg.get("ProductID");

			String sql = "update product set image=?  where ProductID=?";
			PreparedStatement statement = DBConn.prepareStatement(sql);
			statement.setBlob(1, inputStream);
			statement.setString(2, ProductID);
			statement.executeUpdate();
			client.sendToClient(1);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * func that loads the reports from dataBase and send them to the client as
	 * arraylist with hashmap fields that contain the reports data
	 * 
	 * @param clientMsg
	 * @param client
	 * @throws IOException
	 */
	private void loadReportsDetails(HashMap<String, String> clientMsg, ConnectionToClient client) throws IOException {
		Statement stmt;
		String query = "SELECT * FROM Reports ";// firas to updateDataBase
		InputStream input = null;
		FileOutputStream output = null;

		try {
			stmt = DBConn.createStatement();
			ResultSet result = stmt.executeQuery(query);
			ArrayList<HashMap<String, String>> serverMsg = new ArrayList<>();

			while (result.next()) {
				HashMap<String, String> Msg = new HashMap<>();
				Msg.put("ReportID", result.getString(1));
				Msg.put("ReportDate", result.getString(2));
				Msg.put("ReportType", result.getString(4));
				Msg.put("BranchID", result.getString(5));
				Msg.put("ReportQuart", result.getString(6));
				Msg.put("ReportYear", result.getString(7));
				Msg.put("ManagerID", result.getString(9));
				Msg.put("status", result.getString(8));
				Blob blob = result.getBlob(3);
				InputStream inputStream = blob.getBinaryStream();
				/**
				 * store the reports file data in a string
				 */
				Scanner s = new Scanner(inputStream).useDelimiter("\\A");
				String res = s.hasNext() ? s.next() : "";
				Msg.put("ReportFile", res);
				serverMsg.add(Msg);
			}
			try {
				client.sendToClient(serverMsg);
				logController.showMsg("Message sent to client: " + client);
			} catch (IOException e) {
				logController.showMsg("ERROR: server failed to send message to client");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * timer and task varibale is used in the func produce reports every quarter
	 */
	Timer timer = new Timer();
	TimerTask task = new TimerTask() {
		/**
		 * runs the ProduceReportFile every time the timer clicks
		 */
		@Override
		public void run() {
			// TODO Auto-generated method stub
			ProduceReportFile();

		}
	};

	/**
	 * func that start the timer running
	 */
	public void start() {
		timer.schedule(task, 1000 * 10);

	};

	/**
	 * func that create ReportFile every quarter and store the report in data
	 * base
	 */
	public void ProduceReportFile() {
		Statement stmt;
		logController.showMsg("The server is proudcing reports right now!");
		/**
		 * cal is a Calender variable that give us the right time now
		 * 
		 */
		Calendar cal = Calendar.getInstance();
		Date currDate;
		int currQuart = 0;
		int currMonth = cal.get(Calendar.MONTH);
		currMonth++;
		int currYear = cal.get(Calendar.YEAR);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		HashMap<String, String> Msg = new HashMap<>();
		/**
		 * this part we produce a complaints report we take all the complaints
		 * branchid from complaints table in database
		 */
		String BranchQuery = "SELECT BranchID FROM complaine";
		String BranchIDArr[] = new String[30];
		String ReportID = "";
		String query;
		String str;
		File newTextFile = null;
		int w2eek1 = 0, w2eek2 = 0, w2eek3 = 0, w2eek4 = 0, w2eek5 = 0, w2eek6 = 0, tmp;
		String BranchID = "";
		String insertQuery;
		ResultSet result;
		int counter = 0;
		try {
			int i = 0;
			stmt = DBConn.createStatement();
			result = stmt.executeQuery(BranchQuery);
			while (result.next()) {
				/**
				 * Store the branchId FOr every complaint
				 */
				BranchIDArr[i] = result.getString(1);
				i++;
				counter++;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/**
		 * check in what quarter of the year we are
		 */
		if (currMonth <= 3) {
			currQuart = 1;
		}
		if (currMonth <= 6 && currMonth > 3) {
			currQuart = 2;
		}
		if (currMonth <= 9 && currMonth > 6) {
			currQuart = 3;
		}
		if (currMonth <= 12 && currMonth > 9) {
			currQuart = 4;
		}
		/**
		 * loop to find for each complaint the appropriate time part and count
		 * it there to store it
		 */
		for (int i = 0; i < counter; i++) {

			ReportID += 12;
			w2eek1 = 0;
			w2eek2 = 0;
			w2eek3 = 0;
			w2eek4 = 0;
			w2eek5 = 0;
			w2eek6 = 0;
			BranchID = "";
			/**
			 * query that we execute to load the complaints from dataBase by the
			 * branchID
			 */
			if (!BranchIDArr[i].equals(null)) {
				query = "SELECT * FROM complaine Where BranchID = " + BranchIDArr[i] + "";
				if (currMonth % 3 == 0) {
					try {
						stmt = DBConn.createStatement();
						result = stmt.executeQuery(query);
						while (result.next()) {

							/**
							 * loop to store for each complaints its fields in a
							 * hashmap and the fields names are the keys
							 */
							Msg.put("ComplaintID", result.getString(1));
							Msg.put("ComplaintText", result.getString(2));
							Msg.put("UserID", result.getString(3));
							Msg.put("ComplaintMonth", result.getString(4));
							Msg.put("ComplaintDay", result.getString(5));
							Msg.put("BranchID", result.getString(6));

							tmp = Integer.parseInt(Msg.get("ComplaintDay"));
							/**
							 * check in what part of the days the complaint has
							 * been committed
							 */
							if (Integer.parseInt(Msg.get("ComplaintMonth")) % 3 == 1) {
								if (tmp <= 15) {
									/**
									 * w2eek1 is the counter for the complaints
									 * that had been committed in a range of the
									 * first 15 days of the quarter
									 */
									w2eek1++;
								}
								if (tmp > 15 && tmp <= 30) {
									/**
									 * w2eek2 is the counter for the complaints
									 * that had been committed in a range of the
									 * second 15 days of the quarter
									 */
									w2eek2++;
								}
							}
							if (Integer.parseInt(Msg.get("ComplaintMonth")) % 3 == 2) {
								if (tmp > 15) {
									/**
									 * w2eek3 is the counter for the complaints
									 * that had been committed in a range of
									 * 30-45 days of the quarter
									 */
									w2eek3++;
								}
								if (tmp > 15 && tmp < 30) {
									/**
									 * w2eek4 is the counter for the complaints
									 * that had been committed in a range of
									 * 45-60 days of the quarter
									 */
									w2eek4++;
								}
							}
							if (Integer.parseInt(Msg.get("ComplaintMonth")) % 3 == 0) {
								if (tmp < 15) {
									/**
									 * w2eek3 is the counter for the complaints
									 * that had been committed in a range of
									 * 60-75 days of the quarter
									 */
									w2eek5++;
								}
								if (tmp > 15 && tmp < 30) {
									/**
									 * w2eek3 is the counter for the complaints
									 * that had been committed in a range of
									 * 75-90 days of the quarter
									 */
									w2eek6++;
								}
							}
							BranchID = Msg.get("BranchID");
						}
						/**
						 * store the data we load it from the complaints into
						 * one string
						 */
						str = String.format("15/%d %d 30/%d %d 15/%d %d/30 %d %d/15 %d %d/30 %d %d", currMonth - 2,
								w2eek1, currMonth - 2, w2eek2, currMonth - 2, w2eek1, currMonth - 2, w2eek2,
								currMonth - 2, w2eek1, currMonth - 2, w2eek2);
						byte[] byteContent = str.getBytes();
						Blob blob = DBConn.createBlob();// Where connection is
														// the
														// connection to db
														// object.
						blob.setBytes(1, byteContent);

						insertQuery = "INSERT INTO reports (ReportID,ReportDate,ReportFile,ReportType,BranchID,ReportQuart) VALUES"
								+ "(" + ReportID + "," + currMonth + "," + blob + ",'ComplaintsReport',"
								+ BranchIDArr[i] + "," + currQuart + ")" + "";
						stmt = DBConn.createStatement();
						result = stmt.executeQuery(insertQuery);
						logController.showMsg("New complaint report have been proudced!");
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		String BranchQuery2 = "SELECT BranchID FROM zrle.order";

		try {
			int i = 0;
			stmt = DBConn.createStatement();
			result = stmt.executeQuery(BranchQuery2);
			while (result.next()) {
				BranchIDArr[i] = result.getString(1);
				i++;
			}
			for (int j = 0; j < 30; j++) {

				ReportID += 1;
				w2eek1 = 0;
				w2eek2 = 0;
				w2eek3 = 0;
				w2eek4 = 0;
				w2eek5 = 0;
				w2eek6 = 0;
				BranchID = null;
				String OrderType1 = "ArrangmentFlowers";
				String OrderType2 = "FloweringPlant";
				String OrderType3 = "BridalBouquet";
				String OrderType4 = "ClusterOfFlowers";
				int t1 = 0, t2 = 0, t3 = 0, t4 = 0;// variables for the
													// orderTypesCounting
				int totalCost = 0;

				query = "SELECT * FROM order Where BranchID = " + BranchIDArr[j] + "";

				try {
					stmt = DBConn.createStatement();
					result = stmt.executeQuery(query);
					while (result.next()) {
						Msg.put("OrderID", result.getString(1));
						Msg.put("UserAccount", result.getString(2));
						Msg.put("BranchID", result.getString(3));
						Msg.put("OrderType", result.getString(8));
						Msg.put("OrderMonth", result.getString(9));

						tmp = Integer.parseInt(Msg.get("OrderMonth"));

						if (tmp <= currMonth && tmp >= currMonth - 2) {
							if ((Msg.get("OrderType")).equals(OrderType1)) {
								t1++;

							}
							if ((Msg.get("OrderType")).equals(OrderType2)) {
								t2++;

							}
							if ((Msg.get("OrderType")).equals(OrderType3)) {
								t3++;

							}
							if ((Msg.get("OrderType")).equals(OrderType4)) {
								t4++;

							}
							totalCost += Integer.parseInt(Msg.get("OrderCost"));
						}
						BranchID = Msg.get(BranchID);
					}

					str = String.format("%s %d %s %d %s %d %s %d ", OrderType1, t1, OrderType2, t2, OrderType3, t3,
							OrderType4, t4);
					byte[] byteContent = str.getBytes();
					Blob blob = DBConn.createBlob();// Where connection is the
													// connection to db object.
					blob.setBytes(1, byteContent);
					insertQuery = "INSERT INTO reports (ReportID,ReportDate,ReportFile,ReportType,BranchID,ReportQuart) VALUES"
							+ "(" + ReportID + "," + currMonth + "," + blob + ",'OrdersReport'," + BranchIDArr[j] + ","
							+ currQuart + ")" + "";

					stmt = DBConn.createStatement();
					ResultSet result4 = stmt.executeQuery(insertQuery);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			String BranchQuery3 = "SELECT BranchID FROM survey";

			try {
				int k = 0;
				stmt = DBConn.createStatement();
				result = stmt.executeQuery(BranchQuery3);
				while (result.next()) {
					BranchIDArr[k] = result.getString(1);
					k++;
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (currMonth <= 3) {
				currQuart = 1;
			}
			if (currMonth <= 6) {
				currQuart = 2;
			}
			if (currMonth <= 9) {
				currQuart = 3;
			}
			if (currMonth <= 12) {
				currQuart = 4;
			}
			for (int k = 0; k < 30; k++) {

				ReportID += 1;
				w2eek1 = 0;
				w2eek2 = 0;
				w2eek3 = 0;
				w2eek4 = 0;
				w2eek5 = 0;
				w2eek6 = 0;
				BranchID = null;

				query = "SELECT * FROM survey Where BranchID = " + BranchIDArr[k] + "";
				if (currMonth % 3 == 0) {
					try {
						stmt = DBConn.createStatement();
						result = stmt.executeQuery(query);
						while (result.next()) {
							Msg.put("surveyID", result.getString(1));
							Msg.put("DateStarted", result.getString(2));
							Msg.put("DateEnded", result.getString(3));
							Msg.put("surveyAvg", result.getString(8));

							// try to compare with date!!
							tmp = Integer.parseInt(Msg.get("SurveyDay"));
							if (Integer.parseInt(Msg.get("SurveyMonth")) % 3 == 1) {
								if (tmp <= 15) {
									w2eek1 += (Integer.parseInt(Msg.get("surveyAvg")));
								}
								if (tmp > 15 && tmp <= 30) {
									w2eek2 += (Integer.parseInt(Msg.get("surveyAvg")));
								}
							}
							if (Integer.parseInt(Msg.get("ComplaintMonth")) % 3 == 2) {
								if (tmp > 15) {
									w2eek3 += (Integer.parseInt(Msg.get("surveyAvg")));
								}
								if (tmp > 15 && tmp < 30) {
									w2eek4 += (Integer.parseInt(Msg.get("surveyAvg")));
								}
							}
							if (Integer.parseInt(Msg.get("ComplaintMonth")) % 3 == 0) {
								if (tmp < 15) {
									w2eek5 += (Integer.parseInt(Msg.get("surveyAvg")));
								}
								if (tmp > 15 && tmp < 30) {
									w2eek6 += (Integer.parseInt(Msg.get("surveyAvg")));
								}
							}
							BranchID = Msg.get(BranchID);
						}
						str = String.format("15/%d %d/30 %d %d/15 %d %d/30 %d %d/15 %d %d 30/%d %d", currMonth - 2,
								w2eek1, currMonth - 2, w2eek2, currMonth - 2, w2eek1, currMonth - 2, w2eek2,
								currMonth - 2, w2eek1, currMonth - 2, w2eek2);
						byte[] byteContent = str.getBytes();
						Blob blob = DBConn.createBlob();// Where connection is
														// the connection to db
														// object.
						blob.setBytes(1, byteContent);
						insertQuery = "INSERT INTO reports (ReportID,ReportDate,ReportFile,ReportType,BranchID,ReportQuart) VALUES"
								+ "(" + ReportID + "," + currMonth + "," + blob + ",'CustomerSatisfactionReport',"
								+ BranchIDArr[k] + "," + currQuart + ")" + "";
						stmt = DBConn.createStatement();
						result = stmt.executeQuery(insertQuery);

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String BranchQuery4 = "SELECT BranchID FROM zrle.order";

		try {
			int k = 0;
			stmt = DBConn.createStatement();
			result = stmt.executeQuery(BranchQuery4);
			while (result.next()) {
				BranchIDArr[k] = result.getString(1);
				k++;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (currMonth <= 3) {
			currQuart = 1;
		}
		if (currMonth <= 6) {
			currQuart = 2;
		}
		if (currMonth <= 9) {
			currQuart = 3;
		}
		if (currMonth <= 12) {
			currQuart = 4;
		}
		for (int k = 0; k < 30; k++) {

			ReportID += 1;
			w2eek1 = 0;
			w2eek2 = 0;
			w2eek3 = 0;
			w2eek4 = 0;
			w2eek5 = 0;
			w2eek6 = 0;
			BranchID = null;

			query = "SELECT * FROM survey Where BranchID = " + BranchIDArr[k] + "";
			if (currMonth % 3 == 0) {
				try {
					stmt = DBConn.createStatement();
					result = stmt.executeQuery(query);
					while (result.next()) {
						Msg.put("OrderMonth", result.getString(8));
						Msg.put("OrderDay", result.getString(12));
						Msg.put("OrderCost", result.getString(11));
						Msg.put("BranchID", result.getString(3));

						// try to compare with date!!
						tmp = Integer.parseInt(Msg.get("OrderDay"));
						if (Integer.parseInt(Msg.get("OrderMonth")) % 3 == 1) {
							if (tmp <= 15) {
								w2eek1 += (Integer.parseInt(Msg.get("OrderCost")));
							}
							if (tmp > 15 && tmp <= 30) {
								w2eek2 += (Integer.parseInt(Msg.get("OrderCost")));
							}
						}
						if (Integer.parseInt(Msg.get("OrderMonth")) % 3 == 2) {
							if (tmp > 15) {
								w2eek3 += (Integer.parseInt(Msg.get("OrderCost")));
							}
							if (tmp > 15 && tmp < 30) {
								w2eek4 += (Integer.parseInt(Msg.get("OrderCost")));
							}
						}
						if (Integer.parseInt(Msg.get("OrderMonth")) % 3 == 0) {
							if (tmp < 15) {
								w2eek5 += (Integer.parseInt(Msg.get("OrderCost")));
							}
							if (tmp > 15 && tmp < 30) {
								w2eek6 += (Integer.parseInt(Msg.get("OrderCost")));
							}
						}
						BranchID = Msg.get(BranchID);
					}
					str = String.format("15/%d %d 30/%d %d 15/%d %d 30/%d %d 15/%d %d 30/%d %d", currMonth - 2, w2eek1,
							currMonth - 2, w2eek2, currMonth - 2, w2eek1, currMonth - 2, w2eek2, currMonth - 2, w2eek1,
							currMonth - 2, w2eek2);
					byte[] byteContent = str.getBytes();
					Blob blob = DBConn.createBlob();// Where connection is the
													// connection to db object.
					blob.setBytes(1, byteContent);

					insertQuery = "INSERT INTO reports (ReportID,ReportDate,ReportFile,ReportType,BranchID,ReportQuart) VALUES"
							+ "(" + ReportID + "," + currMonth + "," + blob + ",'IncomeReport'," + BranchIDArr[k] + ","
							+ currQuart + ")" + "";
					stmt = DBConn.createStatement();
					result = stmt.executeQuery(insertQuery);

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	private void loadBranch(HashMap<String, String> clientMsg, ConnectionToClient client) {
		String branchID = clientMsg.get("BranchID");
		try {
			Statement stmt = DBConn.createStatement();
			String query = "select * from catalog_product c ,product p,branch_catalog  b where c.ProductID=p.ProductID and b.ProudctID=p.ProductID and b.BranchID='"
					+ branchID + "'";
			ResultSet result = stmt.executeQuery(query);
			ArrayList<HashMap<String, String>> serverMsg = new ArrayList<>();
			while (result.next()) {
				HashMap<String, String> Msg = new HashMap<>();
				Msg.put("ProudctID", result.getString(2));
				Msg.put("Price", result.getString(7));
				Msg.put("SalePrice", result.getString(12));
				serverMsg.add(Msg);
			}
			try {
				client.sendToClient(serverMsg);
			} catch (IOException e) {
				logController.showMsg("Error: the branch catalog didn't send to the client");
				e.printStackTrace();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void loadOrderProudcts(HashMap<String, String> clientMsg, ConnectionToClient client) {
		String OrderID = clientMsg.get("OrderID");
		try {
			Statement stmt = DBConn.createStatement();
			String query = "SELECT * FROM order_product join product on order_product.ProductID=product.ProductID  where OrderID='"
					+ OrderID + "'";
			ResultSet result = stmt.executeQuery(query);
			ArrayList<HashMap<String, String>> serverMsg = new ArrayList<>();
			while (result.next()) {
				HashMap<String, String> Msg = new HashMap<>();
				Msg.put("ProudctID", result.getString(2));
				Msg.put("Quantitiy", result.getString(3));
				Msg.put("Name", result.getString(5));
				Msg.put("Type", result.getString(6));
				Msg.put("Color", result.getString(7));
				Msg.put("Price", result.getString(8));
				serverMsg.add(Msg);
			}
			try {
				client.sendToClient(serverMsg);
			} catch (IOException e) {
				logController.showMsg("Error: the order proudct list didn't sent to the client");
				e.printStackTrace();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void loadOrders(HashMap<String, String> clientMsg, ConnectionToClient client) {
		try {
			Statement stmt = DBConn.createStatement();
			String UserID = clientMsg.get("UserID");
			String query = "SELECT * FROM zrle.`order` where UserID = '" + UserID + "' and OrderStatus='0';";
			try {

				ResultSet result = stmt.executeQuery(query);
				ArrayList<HashMap<String, String>> serverMsg = new ArrayList<>();
				while (result.next()) {
					HashMap<String, String> Msg = new HashMap<>();
					// System.out.println(result.getString(2));
					Msg.put("OrderID", result.getString(1));
					Msg.put("BranchID", result.getString(3));
					Msg.put("OrderStatus", result.getString(4));
					// System.out.println(Msg.toString());
					// Msg.put("CatalogID", result.getString(1));
					serverMsg.add(Msg);
				}
				try {
					client.sendToClient(serverMsg);
					logController.showMsg("Message sent to client: " + client);
				} catch (IOException e) {
					logController.showMsg("ERROR: server failed to send message to client");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void loadCatalaogDeatils(HashMap<String, String> clientMsg, ConnectionToClient client) {
		Statement stmt;
		String query = clientMsg.get("query");
		try {
			stmt = DBConn.createStatement();
			ResultSet result = stmt.executeQuery(query);
			ArrayList<HashMap<String, String>> serverMsg = new ArrayList<>();
			while (result.next()) {
				HashMap<String, String> Msg = new HashMap<>();
				System.out.println(result.getString(2));
				Msg.put("ID", result.getString(1));
				Msg.put("Name", result.getString(2));
				Msg.put("Type", result.getString(3));
				Msg.put("Color", result.getString(4));
				Msg.put("Price", result.getString(5));
				// System.out.println(Msg.toString());
				// Msg.put("CatalogID", result.getString(1));
				serverMsg.add(Msg);
			}
			try {
				client.sendToClient(serverMsg);
				logController.showMsg("Message sent to client: " + client);
			} catch (IOException e) {
				logController.showMsg("ERROR: server failed to send message to client");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * sends the requested file to the client
	 * 
	 * @param clientMsg
	 *            The message received from the client.
	 * @param client
	 *            The connection from which the message originated.
	 * @throws IOException
	 */
	private void getFile(HashMap<String, String> clientMsg, ConnectionToClient client) throws IOException {
		Path path = Paths.get(clientMsg.get("filePath"));
		try {
			byte[] data = Files.readAllBytes(path);
			client.sendToClient(data);
			logController.showMsg("File was sent to client: " + client);
		} catch (NoSuchFileException e) {
			client.sendToClient(null);
			logController.showMsg("File from client: " + client + " was not found");
		} catch (IOException e) {
			logController.showMsg("Failed to send the file to client: " + client);
			e.printStackTrace();
		}
	}

	private void loadCatalogImages(HashMap<String, String> clientMsg, ConnectionToClient client) {
		Statement stmt;
		InputStream input = null;
		FileOutputStream output = null;
		String FilePath = null;
		String query = clientMsg.get("query");
		System.out.println(query);
		HashMap<String, String> serverMsg = new HashMap<String, String>();
		ArrayList<byte[]> data1 = new ArrayList<byte[]>();
		// HashMap<String, Object> serverMsg1 = new HashMap<String, Object>();
		// ArrayList<HashMap<String, String>> server = new ArrayList<>();

		try {
			stmt = DBConn.createStatement();
			ResultSet result = stmt.executeQuery(query);
			File theDir = new File("CatalogItems");

			// if the directory does not exist, create it
			if (!theDir.exists()) {
				System.out.println("creating directory: " + theDir.getName());

				try {
					theDir.mkdir();
				} catch (SecurityException se) {
					se.printStackTrace();
				}
			}
			while (result.next()) {
				int ID = result.getInt(1);
				File theFile = new File("C:\\Zrlefiles\\ProudctsImages\\image" + ID + ".jpg");
				output = new FileOutputStream(theFile);
				input = result.getBinaryStream("image");
				// System.out.println("reading image from database");
				byte[] buffer = new byte[1024];
				while (input.read(buffer) > 0) {
					output.write(buffer);
				}
				Path path = Paths.get(((String) clientMsg.get("filePath")) + ID + ".jpg");
				// System.out.println(path.toString());
				byte[] data = Files.readAllBytes(path);
				// System.out.println(data);
				data1.add(data);
				// server.add(serverMsg);

			}
			// serverMsg1.put("Data", server);
			// serverMsg1.put("ByteArray", data1);
			System.out.println(data1.size());
			logController.showMsg("The catalog products have been loaded.");
			client.sendToClient(data1);
			// System.out.println(data1.size());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * request from client.
	 * 
	 * @return true if accepted, false else
	 * 
	 * @param clientMsg
	 *            The message received from the client.
	 * @param client
	 *            The connection from which the message originated.
	 */
	private void login(HashMap<String, String> clientMsg, ConnectionToClient client) {
		String id, password, school;
		Statement stmt;
		HashMap<String, String> serverMsg = new HashMap<String, String>();

		id = clientMsg.get("id");
		password = clientMsg.get("passwrd");

		String query = "select * from users where UserID='" + id + "';";

		try {
			stmt = DBConn.createStatement();
			ResultSet result = stmt.executeQuery(query);

			if (result.next()) {// if the user found
				if (result.getInt(9) == 0) {// if the user is not blocked
					if (result.getString(2).equals(password)) {// if the given
																// password is
																// valid
						if (result.getInt(7) == 0) {// if the user is not logged
													// in
							serverMsg.put("Valid", "true");
							serverMsg.put("Type", result.getString(8));
							serverMsg.put("Username", result.getString(1));
							stmt.executeUpdate(
									"UPDATE users SET LoginStatus = 1, numoftries = 0 WHERE UserID = '" + id + "';");
						} else {// user is already logged in

							serverMsg.put("Valid", "false");
							serverMsg.put("ErrMsg", "User already logged in.");
						}
					} else {// user password is wrong
						int tryNum = result.getInt(12);
						if (tryNum >= 2) {
							// user entered wrong password 3 times
							stmt.executeUpdate(
									"UPDATE users SET UserStatus = 1, lastLogin = now() WHERE UserID = '" + id + "';");
							serverMsg.put("Valid", "false");
							serverMsg.put("ErrMsg", "User is blocked, try agian in 30 minutes.");
						} else {
							serverMsg.put("Valid", "false");
							serverMsg.put("ErrMsg", "Invalid username or password.");
						}
						// increment number of wrong password in DB
						stmt.executeUpdate("UPDATE users SET numoftries = numoftries + 1 WHERE UserID = '" + id + "';");
					}
				} else {
					// user was blocked
					Date date;
					Timestamp timestamp = result.getTimestamp(13);
					date = new java.util.Date(timestamp.getTime());
					Date now = new Date();
					long diff = now.getTime() - date.getTime();
					if (diff > 1000 * 60 * 30) {
						// if 30 minutes has passed reset block
						stmt.executeUpdate(
								"UPDATE users SET UserStatus = 0, numoftries = 0 WHERE UserID = '" + id + "';");
						if (result.getString(2).equals(password)) {
							// user password is valid, gives the client
							// permeation to log in
							serverMsg.put("Valid", "true");
							serverMsg.put("Type", result.getString(8));
							serverMsg.put("Username", result.getString(1));
							stmt.executeUpdate("UPDATE users SET LoginStatus = 1 WHERE UserID = '" + id + "';");
						} else {
							// user password is wrong
							stmt.executeUpdate(
									"UPDATE users SET numoftries = numoftries + 1 WHERE UserID = '" + id + "';");
							serverMsg.put("Valid", "false");
							serverMsg.put("ErrMsg", "Username or password  is incorrect.");
						}
					} else {
						// user is still blocked
						serverMsg.put("Valid", "false");
						serverMsg.put("ErrMsg",
								"User is blocked, try agian in " + (30 - (diff / (1000 * 60))) + " minutes.");
					}
				}
			} else {
				// user id was not found
				serverMsg.put("Valid", "false");
				serverMsg.put("ErrMsg", "Username or password  is incorrect.");
			}
		} catch (Exception e) {
			logController.showMsg("ERROR: server could not execute the query");
			e.printStackTrace();
		}
		try {
			client.sendToClient(serverMsg);
			logController.showMsg("Message sent to client: " + client);
		} catch (Exception e) {
			logController.showMsg("ERROR: server failed to send message to client");
			e.printStackTrace();
		}
	}

	/**
	 * @return array list of the data requested by the client
	 * @param clientMsg
	 *            The message received from the client.
	 * @param client
	 *            The connection from which the message originated.
	 */
	private void selectQuery(HashMap<String, String> clientMsg, ConnectionToClient client) {
		Statement stmt;

		ArrayList<String> arrayList = new ArrayList<String>();

		// execute the query and translate the result to array list
		try {
			stmt = DBConn.createStatement();
			ResultSet result = stmt.executeQuery(clientMsg.get("query"));

			/* Counting the number of columns */
			int numberOfColumns = result.getMetaData().getColumnCount();

			/* Converting resaultSet into arraylist */
			while (result.next()) {
				int i = 1;
				while (i <= numberOfColumns) {
					arrayList.add(result.getString(i++));
				}
			}
		} catch (Exception e) {
			logController.showMsg("ERROR: server could not execute the query");
			e.printStackTrace();
		}

		// return the result to client
		try {
			client.sendToClient(arrayList);
			logController.showMsg("Message sent to client: " + client);
		} catch (IOException e) {
			logController.showMsg("ERROR: server failed to send message to client");
			e.printStackTrace();
		}
	}

	/**
	 * @return the number of rows affected
	 * 
	 * @param clientMsg
	 *            The message received from the client.
	 * @param client
	 *            The connection from which the message originated.
	 */
	private void updateQuery(HashMap<String, String> clientMsg, ConnectionToClient client) {
		Statement stmt;
		int result = 1;
		// execute the query and return the number of effected rows to client
		try {
			stmt = DBConn.createStatement();
			result = stmt.executeUpdate(clientMsg.get("query"));
		} catch (Exception e) {
			result = 0;
			logController.showMsg("ERROR: server could not execute the query");
			e.printStackTrace();
		}
		try {
			client.sendToClient(result);
			logController.showMsg("Message sent to client: " + client);
		} catch (IOException e) {
			logController.showMsg("ERROR: server failed to send message to client");
			e.printStackTrace();
		}
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		logController.showMsg("Server listening for connection on port " + getPort());
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * stops listening for connections.
	 */
	protected void serverStopped() {
		logController.showMsg("Server has stopped listening for connections.");
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * connects to a client.
	 */
	@Override
	protected void clientConnected(ConnectionToClient client) {
		logController.showMsg("Client: " + client + " has connected");
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * disconnects from a client.
	 */
	@Override
	synchronized protected void clientDisconnected(ConnectionToClient client) {
		logController.showMsg("Client: " + client + " has been disconnected");
	}
	// Class methods ***************************************************

	/**
	 * This method is responsible for the creation of the server instance (there
	 * is no UI in this phase).
	 * 
	 * @param user
	 *            User of the DB
	 * @param password
	 *            Password for the DB
	 * @param sqlPort
	 *            The port that mysql is listening on
	 * @throws IOException
	 */
	public void setServerCon(String user, String password, int sqlPort) throws IOException {
		// open log events controller
		openLogEventGUI();
		setFilesDir();
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ex) {
			/* handle the error */}

		// connect to DB
		try {
			String driverPath = "jdbc:mysql://localhost:" + sqlPort + "/zrle";
			DBConn = DriverManager.getConnection(driverPath, user, password);
			logController.showMsg("SQL connection succeed");
		} catch (SQLException ex) {/* handle any errors */
			logController.showMsg("SQL connection failed");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}

		try {
			this.listen(); // Start listening for connections
			// ProduceReportFile();

		} catch (Exception ex) {
			logController.showMsg("ERROR - Could not listen for clients!");
		}
	}

	/**
	 * open the log event GUI
	 */
	private void openLogEventGUI() {
		// open log events controller
		try {
			Stage primaryStage = new Stage();
			primaryStage.setTitle("MainServer log system");
			primaryStage.getIcons().add(new Image("/server_earth.png"));
			FXMLLoader loader = new FXMLLoader();
			Pane root;
			root = loader.load(getClass().getResource("LogController.fxml").openStream());

			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			logController = loader.getController();
			logController.setIp(getPort());
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we) {
					logController.exit(null);
				}
			});
			primaryStage.show();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Failed to open log view!", "ERROR", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	/**
	 * sets the directory that the file will be saved in
	 */
	private void setFilesDir() {
		File assFiles = new File(assFilesDirPath);
		File subFiles = new File(subFilesDirPath);
		if (!assFiles.exists()) {
			if (assFiles.mkdirs() && subFiles.mkdir()) {
				logController.showMsg("Zrlefiles files directory was created.");
			} else {
				logController.showMsg("Failed to create Zrlefiles files directory!");
				logController.showMsg("Server will shutdown in 5 seconds!!");
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {

				}
				System.exit(0);
			}
		} else {
			logController.showMsg("Zrlefiles files directory already exists.");
		}
	}
}
// End of MainServer class