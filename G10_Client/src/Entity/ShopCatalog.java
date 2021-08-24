package Entity;

import java.util.ArrayList;
import java.util.HashMap;

import application.Main;

public class ShopCatalog {
	int Size;
	private String ShopID;
	private String CatalogID;
	private ArrayList<Product> CatlaogProducts;

	public ShopCatalog(String shopID) {
		super();
		ShopID = shopID;
	}

	public ShopCatalog(String shopID, String catalogID, int size) {
		super();
		ShopID = shopID;
		CatalogID = catalogID;
		Size = size;
	}

	public ShopCatalog(String catalogID, ArrayList<Product> catlaogProducts, String shopID, int size) {
		super();
		Size = size;
		ShopID = shopID;
		CatalogID = catalogID;
		CatlaogProducts = catlaogProducts;
	}

	public int getSize() {
		return Size;
	}

	public void setSize(int size) {
		Size = size;
	}

	public String getShopID() {
		return ShopID;
	}

	public void setShopID(String shopID) {
		ShopID = shopID;
	}

	public String getCatalogID() {
		return CatalogID;
	}

	public void setCatalogID(String catalogID) {
		CatalogID = catalogID;
	}

	public ArrayList<Product> getCatlaogProducts() {
		if (CatlaogProducts == null)
			CatlaogProducts = new ArrayList<Product>();
		return CatlaogProducts;
	}

	public void setCatlaogProducts(ArrayList<Product> catlaogProducts) {
		CatlaogProducts = catlaogProducts;
	}

	public ArrayList<Product> loadProudctDeatils(String CatalogID) {
		HashMap<String, String> msgServer = new HashMap<String, String>();
		ArrayList<Product> list = new ArrayList<Product>();
		msgServer.put("msgType", "loadDeatils");
		msgServer.put("query",
				"select catalog_product.ProductID,product.Name,product.Type,product.Color,product.Price from catalog_product,product where catalog_product.ProductID=product.ProductID and catalog_product.CatalogID='"
						+ CatalogID + "'");
		Main.client.sendMessageToServer(msgServer);
		synchronized (Main.client) {
			try {
				Main.client.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ArrayList<HashMap<String, String>> Deatils = (ArrayList<HashMap<String, String>>) Main.client.getMessage();
		// System.out.println(Deatils.size());
		if (Deatils == null) {
			return null;
		}
		Product product;
		for (int i = 0; i < Deatils.size(); i++) {
			product = new Product(Deatils.get(i).get("ID"), Deatils.get(i).get("Name"), Deatils.get(i).get("Type"),
					Deatils.get(i).get("Color"), Deatils.get(i).get("Price"));
			// System.out.println(product.toString());
			list.add(product);
		}
		return list;
	}

	@Override
	public String toString() {
		return "ShopCatalog [CatalogID=" + CatalogID + ", CatlaogProducts=" + CatlaogProducts + "]";
	}

}
