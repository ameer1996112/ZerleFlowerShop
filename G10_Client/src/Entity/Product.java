package Entity;

import java.util.ArrayList;
import java.util.HashMap;

import application.Main;
import javafx.scene.image.Image;

public class Product {
	private String ProductID;
	private String ProductName;
	private String Type;
	private String Color;
	private String Price;
	private double Sale;
	private Image img;
	
	/*public Product(String productID, String productName, String type, String color, String price,double sale) {
		ProductID = productID;
		ProductName = productName;
		Type = type;
		Color = color;
		Price = price;
		Sale=sale;
	}*/
	public Product(String productID, String productName, String type, String color, String price) {
		ProductID = productID;
		ProductName = productName;
		Type = type;
		Color = color;
		Price = price;
		Sale=0;
		img=null;
	}



	public Product() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "Product [ProductID=" + ProductID + ", ProductName=" + ProductName + ", Type=" + Type + ", Color="
				+ Color + ", Price=" + Price + "]";
	}

	public String getProductID() {
		return ProductID;
	}

	public void setProductID(String productID) {
		ProductID = productID;
	}

	public String getProductName() {
		return ProductName;
	}

	public void setProductName(String productName) {
		ProductName = productName;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getColor() {
		return Color;
	}

	public void setColor(String color) {
		Color = color;
	}

	public String getPrice() {
		return Price;
	}

	public void setPrice(String price) {
		Price = price;
	}

	public Double getSale() {
		return Sale;
	}

	public void setSale(Double sale) {
		Sale = sale;
	}

	public Image getImg() {
		return img;
	}

	public void setImg(Image img) {
		this.img = img;
	}

	public Product clone() {
		Product p = new Product();
		p.ProductID = this.ProductID;
		p.Color = this.Color;
		p.Price = this.Price;
		p.ProductName = this.ProductName;
		p.Type = this.Type;

		return p;
	}

	public ArrayList<Product> loadProudctDeatils(String CatalogID) {
		HashMap<String, String> msgServer = new HashMap<String, String>();
		ArrayList<Product> list = new ArrayList<Product>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select ProductID, Name, Type, Color, Price from product");
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
		// System.out.println(Deatils.size());
		if (Deatils == null) {
			return null;
		}
		Product product;
		for (int i = 0; i < (Deatils.size()); i+=5) {
			product = new Product(Deatils.get(i), Deatils.get(i+1), Deatils.get(i+2),Deatils.get(i+3),Deatils.get(i+4));
			// System.out.println(product.toString());
			list.add(product);
		}
		return list;
	}
}