package Controller;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Entity.Product;
import application.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ViewCatalogController extends CatalogOrderController implements Initializable {
@FXML 
private AnchorPane pane;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		loadPictures();
		ImageView[] images = new ImageView[Main.catalog.getShopCat().getSize()];
		int i = 0;
		for (i = 0; i < images.length; i++) {
			for (int j = 0; j < Main.salelist.size(); j++) {
				// System.out.println(Main.catalog.getShopCat().getCatlaogProducts().get(i).getProductID());
				if (Main.catalog.getShopCat().getCatlaogProducts().get(i).getProductID().toString()
						.equals(Main.salelist.get(j).get("ProudctID"))) {
					Main.catalog.getShopCat().getCatlaogProducts().get(i)
							.setPrice(Main.salelist.get(j).get("SalePrice"));
				}
			}

		}
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
			int ShopCatPrice = Integer.parseInt(tempdeatils.get(i).getPrice());
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
			Pane BtnPane = new Pane();
			box.getChildren().addAll(ImagePane, textPane);
			pane2.getChildren().add(box);
			pane.getStylesheets().add("/FXML/style.css");
			show.getChildren().addAll(pane2);
			i++;
		}
	}

}
