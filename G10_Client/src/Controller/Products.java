package Controller;

import java.io.Serializable;

import javafx.scene.image.Image;

public class Products implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int ProductID;
	private String ProductName;
	private String Type;
	private String Color;
	private double Sale;
	private double Price;
	private Image img;
	
	public Products(int productid ,String productName, String type, String color,double price, double sale, Image img) {
		super();
		ProductID=productid;
		ProductName = productName;
		Type = type;
		Color = color;
		Price=price;
		Sale = sale;
		this.img = img;
	}
	
	public Products() {
		
	}

	public int getProductID() {
		return ProductID;
	}

	public void setProductID(int productID) {
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

	public double getSale() {
		return Sale;
	}

	public void setSale(double sale) {
		Sale = sale;
	}
	
	public double getPrice() {
		return Price;
	}

	public void setPrice(double price) {
		Price = price;
	}

	public Image getImg() {
		return img;
	}

	public void setImg(Image img) {
		this.img = img;
	}

	@Override
	public String toString() {
		return "Products [ProductID=" +ProductID +" ,ProductName=" + ProductName + ", Type=" + Type + ", Color=" + Color + ", Sale=" + Sale
				+", Price=" + Price + ", img=" + img + "]";
	}


}
