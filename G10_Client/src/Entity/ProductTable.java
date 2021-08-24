package Entity;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ProductTable {
	
	private  SimpleStringProperty ProductName;
	private  SimpleStringProperty Desc;
	private  SimpleStringProperty Price;
	
	public ProductTable(String productName, String desc, String price) {
		super();
		ProductName = new SimpleStringProperty(productName);
		Desc = new SimpleStringProperty(desc);
		Price = new SimpleStringProperty(price);
	}
	

	public String getProductName() {
		return ProductName.get();
	}

	public void setProductName(String productName) {
		ProductName.set(productName);
	}

	public String getDesc() {
		return Desc.get();
	}

	public void setDesc(String desc) {
		Desc.set(desc);
	}

	public String getPrice() {
		return Price.get();
	}

	public void setPrice(String price) {
		Price.set(price);
	}

	@Override
	public String toString() {
		return "ProductTable [ProductName=" + ProductName + ", Desc=" + Desc + ", Price=" + Price + "]";
	}
	
	

}
