package Controller;

import java.io.IOException;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.ComboBox;
import Entity.PaymentAccount;
import Entity.Survey;
import Entity.report;
import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;

import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Group;

public class ReportsController implements Initializable {
	/**
	 * Creating a static client to pass to the controllers
	 */
	/**
	 * Select Variables for storing the user choose values in the combobox
	 */
	public static String BranchIDSelect;
	public static String QuarterSelect;
	public static String YearSelect;
	public static String BranchIDSelect1;
	public static String QuarterSelect1;
	public static String YearSelect1;
	public static String CategorySelect;
	public static String CategorySelect1;
	/**
	 * two static arrays to store the data that was sent from server
	 */
	public static report arr[] = new report[100];
	static ArrayList<report> reports = new ArrayList<report>();
	Timer t = new Timer();

	/**
	 * Event that shows view reports menu by clicking the view reportsButton
	 */
	public void ViewReports(ActionEvent event) throws Exception {
		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/ViewReport.fxml"));
		Scene scene = new Scene(root, 800, 550);
		// scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
		loadReportsforClient();
	}

	@FXML
	void ApprovePaymentAccountMenu(ActionEvent event) throws IOException {
		Main.OpenFxmlFile(event, "/FXML/ApprovePaymentAccount.fxml");
	}

	@FXML
	void CatalogWorkerMenu(ActionEvent event) throws IOException {
		Main.OpenFxmlFile(event, "/FXML/Catalog.fxml");
	}

	@FXML
	void logO(ActionEvent event) throws IOException {
Main.OpenFxmlFile(event, "/FXML/lLogin.fxml");
	}

	/**
	 * count is a varibale to count the number of reports that were sent from
	 * server
	 */
	static int count = 0;

	/**
	 * client sends message to the server to load the reports from data base and
	 * sent them back to client
	 * 
	 * @return
	 */
	public static boolean loadReportsforClient() {
		HashMap<String, String> msg = new HashMap<>();

		msg.put("msgType", "loadReports");

		try {
			Main.client.sendMessageToServer(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}

		synchronized (Main.client) {
			try {
				Main.client.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		/**
		 * serverAnswer is a arraylist that store the server message in hashmap
		 * collection
		 */
		ArrayList<HashMap<String, String>> serverAnswer = (ArrayList<HashMap<String, String>>) Main.client.getMessage();
		/**
		 * report ar[] array that store elements of report class
		 */
		report ar[] = new report[100];
		/**
		 * load data from hashmap and store it in a report variable
		 */
		for (int size = 0; size < serverAnswer.size(); size++) {
			HashMap<String, String> hashmap = serverAnswer.get(size);
			report a = new report(hashmap.get("ReportType"), "", hashmap.get("ReportQuart"),
					serverAnswer.get(size).get("BranchID"), hashmap.get("ReportFile"), "", hashmap.get("ReportYear"));
			reports.add(a);
			count++;
		}

		Main.client = null;

		return true;
	}
	// constructor variables for income functions

	/**
	 * varibale: CategoryAxis xAxis,NumberAxis yAxis two variables for the
	 * lineChart
	 */
	@FXML
	private CategoryAxis xAxis;

	@FXML
	private NumberAxis yAxis;
	/*
	 * IncomeChart line chart to show
	 * 
	 */
	@FXML
	private LineChart<String, Number> IncomeChart;
	@FXML
	private LineChart<String, Number> IncomeChart1;

	/*
	 * IncomeReportsLoaders func that find the wanted report by the choosing
	 * values
	 * 
	 */
	public void IncomeReportsLoaders(String quarterSelect2, String branchIDSelect2, String year) {
		/*
		 * take the number of the Income report for each in everyline in file
		 * key there is the name of the y and then the income number for the
		 * month three months per report
		 */
		String res;
		int y_value[] = new int[30];
		String x_value[] = new String[30];
		for (int i = 0; i < count; i++) {
			/**
			 * find the wanted report
			 */
			if ((reports.get(i).getReportType().equals("IncomeReport"))
					&& (reports.get(i).getBranchID().equals(branchIDSelect2))
					&& (reports.get(i).getReportQuarter().equals(quarterSelect2))
					&& (reports.get(i).getReportYear().equals(year))) {
				res = reports.get(i).getReportFile();

				/**
				 * String[] is an array that store the words from res the
				 * contains the file data
				 */
				String[] parts = res.split("\\s+");

				/**
				 * concatenating number of words that you required
				 */

				for (int j = 0, t = 0; j < parts.length; j += 2, t++) {
					x_value[t] = parts[j];
					y_value[t] = Integer.parseInt(parts[j + 1]);

				}
				IncomeChart.getData().clear();
				XYChart.Series series = new XYChart.Series();
				/**
				 * series is an XYChart that store the res parts
				 */
				series.getData().add(new XYChart.Data(x_value[0], y_value[0]));
				series.getData().add(new XYChart.Data(x_value[1], y_value[1]));
				series.getData().add(new XYChart.Data(x_value[2], y_value[2]));
				series.getData().add(new XYChart.Data(x_value[3], y_value[3]));
				series.getData().add(new XYChart.Data(x_value[4], y_value[4]));
				series.getData().add(new XYChart.Data(x_value[5], y_value[5]));
				/**
				 * store the data to the income chart
				 */
				IncomeChart.getData().add(series);
			}

		}
	}

	/**
	 * IncomeReportsLoaders1 func that find the wanted report by the choosing
	 * values
	 */
	public void IncomeReportsLoaders1(String quarterSelect2, String branchIDSelect2, String year) {
		/**
		 * take the number of the Income report for each in everyline in file
		 * key there is the name of the month and then the income number for the
		 * month three months per report used for the IncomeChart1
		 */
		String res;
		int y_value[] = new int[30];
		String x_value[] = new String[30];
		/**
		 * loop for finding the required report in the reports arraylist and
		 * store the data in the incomeChart1
		 */
		for (int i = 0; i < count; i++) {
			if ((reports.get(i).getReportType().equals("IncomeReport"))
					&& (reports.get(i).getBranchID().equals(branchIDSelect2))
					&& (reports.get(i).getReportQuarter().equals(quarterSelect2))
					&& (reports.get(i).getReportYear().equals(year))) {
				res = reports.get(i).getReportFile();
				String[] array = res.split("\\s+");

				/**
				 * concatenating number of words that you required
				 */

				for (int j = 0, t = 0; j < array.length; j += 2, t++) {
					x_value[t] = array[j];
					y_value[t] = Integer.parseInt(array[j + 1]);

				}
				IncomeChart1.getData().clear();
				XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
				for (int t = 0; t < 6; t++) {
					series.getData().add(new XYChart.Data<String, Number>(x_value[t], y_value[t]));
				}
				IncomeChart1.getData().add(series);
			}

		}
	}

	/**
	 * OrderReportsPieChart two pie chart to show the report data
	 */
	@FXML
	private PieChart OrderReportsPieChart;

	@FXML
	private PieChart OrderReportsPieChart1;

	/**
	 * func that finds the wanted report by the variables below that the user
	 * chose
	 * 
	 * @param quarterSelect2
	 * @param branchIDSelect2
	 * @param year
	 */
	public void OrdersReportsLoaders(String quarterSelect2, String branchIDSelect2, String year) {
		// take the number of the Income report for each
		String res;
		int y_value[] = new int[30];
		String x_value[] = new String[30];
		/**
		 * loop for finding the wanted report and store its data to the pieChart
		 */
		for (int i = 0; i < count; i++) {
			if ((reports.get(i).getReportType().equals("OrdersReport"))
					&& (reports.get(i).getBranchID().equals(branchIDSelect2))
					&& (reports.get(i).getReportQuarter().equals(quarterSelect2))
					&& (reports.get(i).getReportYear().equals(year))) {
				res = reports.get(i).getReportFile();
				String[] array = res.split("\\s+");

				// concatenating number of words that you required

				for (int j = 0, t = 0; j < array.length; j += 2, t++) {
					x_value[t] = array[j];

					y_value[t] = Integer.parseInt(array[j + 1]);

				}

				int t = 0;
				ObservableList<Data> list = FXCollections.observableArrayList(

						new PieChart.Data(x_value[t], y_value[t]), new PieChart.Data(x_value[t + 1], y_value[t + 1]),
						new PieChart.Data(x_value[t + 2], y_value[t + 2]),
						new PieChart.Data(x_value[t + 3], y_value[t + 3]));
				System.out.print(list);
				OrderReportsPieChart.setData(list);
			}
		}
	}

	/**
	 * func that finds the wanted report by the variables below that the user
	 * chose
	 * 
	 * @param quarterSelect2
	 * @param branchIDSelect2
	 * @param year
	 */
	public void OrdersReportsLoaders1(String quarterSelect2, String branchIDSelect2, String year) {
		/**
		 * take the number of the Income report for each
		 */
		String res;
		int y_value[] = new int[30];
		String x_value[] = new String[30];
		/**
		 * loop to find the requird report and store data in the piechart
		 */
		for (int i = 0; i < count; i++) {
			if ((reports.get(i).getReportType().equals("OrdersReport"))
					&& (reports.get(i).getBranchID().equals(branchIDSelect2))
					&& (reports.get(i).getReportQuarter().equals(quarterSelect2))
					&& (reports.get(i).getReportYear().equals(year))) {
				res = reports.get(i).getReportFile();
				String[] array = res.split("\\s+");

				/**
				 * concatenating number of words that you required
				 */

				for (int j = 0, t = 0; j < array.length; j += 2, t++) {
					x_value[t] = array[j];
					y_value[t] = Integer.parseInt(array[j + 1]);

				}
				int t = 0;
				ObservableList<Data> list = FXCollections.observableArrayList(

						new PieChart.Data(x_value[t], y_value[t]), new PieChart.Data(x_value[t + 1], y_value[t + 1]),
						new PieChart.Data(x_value[t + 2], y_value[t + 2]),
						new PieChart.Data(x_value[t + 3], y_value[t + 3]));
				OrderReportsPieChart1.setData(list);
			}
		}
	}

	@FXML
	private CategoryAxis x11;

	@FXML
	private NumberAxis yAxis2;
	/**
	 * CustomerSatisfactionChart is line chart to show customers satisfaction
	 * reports CustomerSatisfactionChart1 is line chart to show customers
	 * satisfaction reports
	 */
	@FXML
	private LineChart<String, Number> CustomerSatisfactionChart;
	@FXML
	private LineChart<String, Number> CustomerSatisfactionChart1;

	/**
	 * func that finds the wanted report by the variables below that the user
	 * chose
	 * 
	 * @param quarterSelect2
	 * @param branchIDSelect2
	 * @param year
	 */
	public void CustomerSatisfactionReportsLoaders(String quarterSelect2, String branchIDSelect2, String year) {

		String res;
		int y_value[] = new int[30];
		String x_value[] = new String[30];
		/**
		 * loop for finding the wanted report and store its data to the chart
		 */
		for (int i = 0; i < count; i++) {
			if ((reports.get(i).getReportType().equals("CustomerSatisfactionReport"))
					&& (reports.get(i).getBranchID().equals(branchIDSelect2))
					&& (reports.get(i).getReportQuarter().equals(quarterSelect2))
					&& (reports.get(i).getReportYear().equals(year))) {
				res = reports.get(i).getReportFile();
				System.out.print(res);
				String[] array = res.split("\\s+");

				/**
				 * concatenating number of words that you required
				 */

				for (int j = 0, t = 0; j < array.length; j += 2, t++) {

					x_value[t] = array[j];
					y_value[t] = Integer.parseInt(array[j + 1]);

				}
			}

			CustomerSatisfactionChart.getData().clear();
			XYChart.Series series = new XYChart.Series();

			series.getData().add(new XYChart.Data("15", y_value[0]));
			series.getData().add(new XYChart.Data("30", y_value[1]));
			series.getData().add(new XYChart.Data("45", y_value[2]));
			series.getData().add(new XYChart.Data("60", y_value[3]));
			series.getData().add(new XYChart.Data("75", y_value[4]));
			series.getData().add(new XYChart.Data("90", y_value[5]));

			/**
			 * display the data in the chart
			 */
			CustomerSatisfactionChart.getData().add(series);

		}
	}

	/**
	 * func that finds the wanted report by the variables below that the user
	 * chose
	 * 
	 * @param quarterSelect2
	 * @param branchIDSelect2
	 * @param year
	 */
	public void CustomerSatisfactionReportsLoaders1(String quarterSelect2, String branchIDSelect2, String year) {
		// take the number of the Income report for each
		String res;
		int y_value[] = new int[30];
		String x_value[] = new String[30];
		/**
		 * loop for finding the wanted report and store its data to the chart
		 */
		for (int i = 0; i < count; i++) {
			if ((reports.get(i).getReportType().equals("CustomerSatisfactionReport"))
					&& (reports.get(i).getBranchID().equals(branchIDSelect2))
					&& (reports.get(i).getReportQuarter().equals(quarterSelect2))
					&& (reports.get(i).getReportYear().equals(year))) {
				res = reports.get(i).getReportFile();
				String[] array = res.split("\\s+");

				/**
				 * concatenating number of words that you required
				 */

				for (int j = 0, t = 0; j < array.length; j += 2, t++) {
					x_value[t] = array[j];
					y_value[t] = Integer.parseInt(array[j + 1]);

				}
			}
			CustomerSatisfactionChart1.getData().clear();
			XYChart.Series series = new XYChart.Series();

			series.getData().add(new XYChart.Data("15", y_value[0]));
			series.getData().add(new XYChart.Data("30", y_value[1]));
			series.getData().add(new XYChart.Data("45", y_value[2]));
			series.getData().add(new XYChart.Data("60", y_value[3]));
			series.getData().add(new XYChart.Data("75", y_value[4]));
			series.getData().add(new XYChart.Data("90", y_value[5]));
			/**
			 * display the report data into the chart
			 */
			CustomerSatisfactionChart1.getData().add(series);

		}
	}

	@FXML
	private NumberAxis Axis1;
	@FXML
	private CategoryAxis Axis2;
	/**
	 * ComplaintReportsBarChart is a barchart the display report data about
	 * complaints
	 */
	@FXML
	private BarChart<String, Number> ComplaintReportsBarChart;

	@FXML
	private CategoryAxis x;
	@FXML
	private NumberAxis y;

	/**
	 * func that finds the wanted report by the variables below that the user
	 * chose
	 * 
	 * @param quarterSelect2
	 * @param branchIDSelect2
	 * @param year
	 */
	public void ComplaintsReportsLoaders(String quarterSelect2, String branchIDSelect2, String year) {
		String res;
		int y_value[] = new int[30];
		String x_value[] = new String[30];
		/**
		 * loop for finding the wanted report and store its data to the chart
		 */
		for (int i = 0; i < count; i++) {
			if ((reports.get(i).getReportType().equals("ComplaintsReport"))
					&& (reports.get(i).getBranchID().equals(branchIDSelect2))
					&& (reports.get(i).getReportQuarter().equals(quarterSelect2))
					&& (reports.get(i).getReportYear().equals(year))) {
				res = reports.get(i).getReportFile();
				String[] array = res.split("\\s+");

				/**
				 * concatenating number of words that you required
				 */

				for (int j = 0, t = 0; j < array.length; j += 2, t++) {
					x_value[t] = array[j];
					y_value[t] = Integer.parseInt(array[j + 1]);

				}
				Series<String, Number> set = new XYChart.Series<>();
				for (int k = 0; k < 6; k++) {
					set.getData().add(new XYChart.Data(x_value[k], y_value[k]));

				}
				/**
				 * display the report data in chart
				 */
				ComplaintReportsBarChart.getData().addAll(set);

			}
		}
	}

	/**
	 * ComplaintReportsBarChart1 to display camplaint reportdata
	 */
	@FXML
	private BarChart<String, Number> ComplaintReportsBarChart1;
	@FXML
	private CategoryAxis x1;
	@FXML
	private NumberAxis y1;

	/**
	 * func that finds the wanted report by the variables below that the user
	 * chose
	 * 
	 * @param quarterSelect2
	 * @param branchIDSelect2
	 * @param year
	 */
	public void ComplaintsReportsLoaders1(String quarterSelect2, String branchIDSelect2, String year) {
		String res;
		int y_value[] = new int[30];
		String x_value[] = new String[30];
		/**
		 * find the requierd report
		 */
		for (int i = 0; i < count; i++) {
			if ((reports.get(i).getReportType().equals("ComplaintsReport"))
					&& (reports.get(i).getBranchID().equals(branchIDSelect2))
					&& (reports.get(i).getReportQuarter().equals(quarterSelect2))
					&& (reports.get(i).getReportYear().equals(year))) {
				res = reports.get(i).getReportFile();
				String[] array = res.split("\\s+");

				/**
				 * concatenating number of words that you required
				 */

				for (int j = 0, t = 0; j < array.length; j += 2, t++) {
					x_value[t] = array[j];
					y_value[t] = Integer.parseInt(array[j + 1]);

				}
				Series<String, Number> set = new XYChart.Series<>();
				for (int k = 0; k < 6; k++) {
					set.getData().add(new XYChart.Data(x_value[k], y_value[k]));

				}
				/**
				 * display the report data in the chart
				 */
				ComplaintReportsBarChart1.getData().addAll(set);

			}
		}
	}

	/**
	 * Action event that open CustomerSatisfactionReport GUI
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void CustomerSatisfactionReport(ActionEvent event) throws Exception {
		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/CustomerSatisfactionReport.fxml"));
		Scene scene = new Scene(root, 600, 550);
		// scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Action event that open THE Comparison CustomerSatisfactionReport GUI
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void ComparisonCustomerSatisfactionReport(ActionEvent event) throws Exception {
		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/ComparisonCustomerSatisfactionReport.fxml"));
		Scene scene = new Scene(root, 800, 550);
		// scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	/**
	 * Action event that open IncomeReport GUI
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void IncomeReport(ActionEvent event) throws Exception {
		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/IncomeReport.fxml"));
		Scene scene = new Scene(root, 600, 550);
		// scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Action event that open ComparisonIncomeReport GUI
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void ComparisonIncomeReport(ActionEvent event) throws Exception {
		Stage primaryStage = new Stage();

		Parent root = FXMLLoader.load(getClass().getResource("/fxml/ComparisonIncomeReport.fxml"));
		Scene scene = new Scene(root, 800, 500);
		// scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	/**
	 * Action event that open ordersReport GUI
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void OrdersReport(ActionEvent event) throws Exception {
		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/OrdersReport.fxml"));
		Scene scene = new Scene(root, 600, 550);
		// scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	/**
	 * Action event that open ComparisonOrderReport GUI
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void ComparisonOrdersReport(ActionEvent event) throws Exception {
		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/ComparisonOrdersReport.fxml"));
		Scene scene = new Scene(root, 600, 600);
		// scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	/**
	 * Action event that open ComplaintsReport GUI
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void ComplaintsReport(ActionEvent event) throws Exception {
		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/ComplaintReport.fxml"));
		Scene scene = new Scene(root, 600, 550);
		// scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Action event that open ComparisonComplaints Report GUI
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void ComparisonComplaintsReport(ActionEvent event) throws Exception {
		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/ComparisonComplaintReport.fxml"));

		Scene scene = new Scene(root, 600, 400);
		// scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	/**
	 * 
	 * @param event
	 *            that calls callLoadsFunc to send message to server
	 */
	@FXML
	public void loadComplaintReportsBarChart(ActionEvent event) {
		callLoadsFunc(QuarterSelect, BranchIDSelect, YearSelect, CategorySelect);
	}

	/**
	 * 
	 * @param event
	 *            that calls callLoadsFunc to send message to server
	 */
	public void loadOrderReportsPieChart(ActionEvent event) {
		callLoadsFunc(QuarterSelect, BranchIDSelect, YearSelect, CategorySelect);

	}

	/**
	 * 
	 * @param event
	 *            that calls callLoadsFunc to send message to server
	 */
	@FXML
	public void loadCustomerSatisfactionChart(ActionEvent event) {
		callLoadsFunc(QuarterSelect, BranchIDSelect, YearSelect, CategorySelect);
	}

	/**
	 * 
	 * @param event
	 *            that calls callLoadsFunc to send message to server
	 */
	@FXML
	void loadIncomeChart(ActionEvent event) {
		callLoadsFunc(QuarterSelect, BranchIDSelect, YearSelect, CategorySelect);
	}

	/**
	 * 
	 * @param event
	 *            that calls callLoadsFunc to send message to server
	 */
	@FXML
	public void loadComplaintReportsBarChart1(ActionEvent event) {
		callLoadsFunc1(QuarterSelect1, BranchIDSelect1, YearSelect1, CategorySelect1);
	}

	/**
	 * 
	 * @param event
	 *            that calls callLoadsFunc to send message to server
	 */
	@FXML
	public void loadOrderReportsPieChart1(ActionEvent event) {
		callLoadsFunc1(QuarterSelect1, BranchIDSelect1, YearSelect1, CategorySelect1);

	}

	/**
	 * 
	 * @param event
	 *            that calls callLoadsFunc to send message to server
	 */
	@FXML
	public void loadCustomerSatisfactionChart1(ActionEvent event) {
		callLoadsFunc1(QuarterSelect1, BranchIDSelect1, YearSelect1, CategorySelect1);
	}

	/**
	 * 
	 * @param event
	 *            that calls callLoadsFunc to send message to server
	 */
	@FXML
	void loadIncomeChart1(ActionEvent event) {
		callLoadsFunc1(QuarterSelect1, BranchIDSelect1, YearSelect1, CategorySelect1);
	}

	/**
	 * the comboboxes below is to show the user multiple choices
	 */
	@FXML
	public ComboBox<String> BranchIDCombobox = new ComboBox<String>();
	@FXML
	public ComboBox<String> QuarterCombobox = new ComboBox<String>();
	@FXML
	public ComboBox<String> YearCombobox = new ComboBox<String>();
	@FXML
	public ComboBox<String> BranchIDCombobox1 = new ComboBox<String>();
	@FXML
	public ComboBox<String> QuarterCombobox1 = new ComboBox<String>();
	@FXML
	public ComboBox<String> YearCombobox1 = new ComboBox<String>();
	@FXML
	public ComboBox<String> CategoryCombobox = new ComboBox<String>();
	@FXML
	public ComboBox<String> CategoryCombobox1 = new ComboBox<String>();

	/**
	 * initilize the comboboxes with the attached values
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		BranchIDCombobox.getItems().addAll("1", "2", "3", "4");
		QuarterCombobox.getItems().addAll("1", "2", "3", "4");
		YearCombobox.getItems().addAll("2017", "2016", "2015");
		BranchIDCombobox1.getItems().addAll("1", "2", "3", "4");
		QuarterCombobox1.getItems().addAll("1", "2", "3", "4");
		YearCombobox1.getItems().addAll("2017", "2016", "2015");
		CategoryCombobox.getItems().addAll("ComplaintReport", "IncomeReport", "CustomerSatisfactionReport",
				"OrdersReport");
		CategoryCombobox1.getItems().addAll("ComplaintReport", "IncomeReport", "CustomerSatisfactionReport",
				"OrdersReport");
	}

	/**
	 * 
	 * @param event
	 *            that store the choosing value
	 */
	public void ComboBranch(ActionEvent event) {
		BranchIDSelect = BranchIDCombobox.getValue().toString();
	}

	/**
	 * 
	 * @param event
	 *            that store the choosing value
	 */
	public void ComboQuarter(ActionEvent event) {
		QuarterSelect = QuarterCombobox.getValue().toString();
	}

	/**
	 * 
	 * @param event
	 *            that store the choosing value
	 */
	public void ComboYear(ActionEvent event) {
		YearSelect = YearCombobox.getValue().toString();
	}

	/**
	 * 
	 * @param event
	 *            that store the choosing value
	 */
	public void ComboCategory(ActionEvent event) {
		CategorySelect = CategoryCombobox.getValue().toString();
	}

	/**
	 * 
	 * @param event
	 *            that store the choosing value
	 */
	public void ComboBranch1(ActionEvent event) {
		BranchIDSelect1 = BranchIDCombobox1.getValue().toString();
	}

	/**
	 * 
	 * @param event
	 *            that store the choosing value
	 */
	public void ComboQuarter1(ActionEvent event) {
		QuarterSelect1 = QuarterCombobox1.getValue().toString();
	}

	/**
	 * 
	 * @param event
	 *            that store the choosing value
	 */
	public void ComboYear1(ActionEvent event) {
		YearSelect1 = YearCombobox1.getValue().toString();
	}

	/**
	 * 
	 * @param event
	 *            that store the choosing value
	 */
	public void ComboCategory1(ActionEvent event) {
		CategorySelect1 = CategoryCombobox1.getValue().toString();
	}

	/**
	 * func that call the reports loader func that fit the category select
	 * 
	 * @param QuarterSelect
	 * @param BranchIDSelect
	 * @param YearSelect
	 * @param categorySelect2
	 */
	public void callLoadsFunc(String QuarterSelect, String BranchIDSelect, String YearSelect, String categorySelect2) {
		if (categorySelect2 == "ComplaintReport") {
			ComplaintsReportsLoaders(QuarterSelect, BranchIDSelect, YearSelect);
		}
		if (categorySelect2 == "CustomerSatisfactionReport") {
			CustomerSatisfactionReportsLoaders(QuarterSelect, BranchIDSelect, YearSelect);

		}
		if (categorySelect2 == "OrdersReport") {
			OrdersReportsLoaders(QuarterSelect, BranchIDSelect, YearSelect);
		}
		if (categorySelect2 == "IncomeReport") {
			IncomeReportsLoaders(QuarterSelect, BranchIDSelect, YearSelect);
		}
	}

	/**
	 * func that call the reports loader func that fit the category select
	 * 
	 * @param QuarterSelect
	 * @param BranchIDSelect
	 * @param YearSelect
	 * @param categorySelect2
	 */
	public void callLoadsFunc1(String QuarterSelect, String BranchIDSelect, String YearSelect, String categorySelect2) {
		if (categorySelect2 == "ComplaintReport") {
			ComplaintsReportsLoaders1(QuarterSelect, BranchIDSelect, YearSelect);
		}
		if (categorySelect2 == "CustomerSatisfactionReport") {
			CustomerSatisfactionReportsLoaders1(QuarterSelect, BranchIDSelect, YearSelect);

		}
		if (categorySelect2 == "OrdersReport") {
			OrdersReportsLoaders1(QuarterSelect, BranchIDSelect, YearSelect);
		}
		if (categorySelect2 == "IncomeReport") {
			IncomeReportsLoaders1(QuarterSelect, BranchIDSelect, YearSelect);
		}
	}

	@FXML
	void FillSurveyDetails(ActionEvent event) {

	}

	/**
	 * the text fields belows is used in add payment account GUI
	 */
	@FXML
	private TextField txtf1;

	@FXML
	private TextField txtf2;

	@FXML
	private TextField txtf3;

	@FXML
	private TextField txtf4;

	@FXML
	private TextField txtf5;

	@FXML
	private TextField txt1;
	@FXML
	private TextField txt2;
	@FXML
	private TextField txt3;
	@FXML
	private TextField txt4;

	public void fillSurveyDetails(ActionEvent event) throws Exception {
		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/AddSurveyAnswers.fxml"));
		Scene scene = new Scene(root, 400, 400);
		// scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
