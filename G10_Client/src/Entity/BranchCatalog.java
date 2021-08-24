package Entity;

import java.util.ArrayList;
import java.util.HashMap;

public class BranchCatalog {
	private int BranchID;
	private ArrayList<HashMap<String, String>> saleProudcts;
	private ShopCatalog shopCat;

	public BranchCatalog(int branchID, ShopCatalog shopCat) {
		super();
		BranchID = branchID;
		this.shopCat = shopCat;
	}

	public BranchCatalog(int branchID, ArrayList<HashMap<String, String>> saleProudcts, ShopCatalog shopCat) {
		super();
		BranchID = branchID;
		this.saleProudcts = saleProudcts;
		this.shopCat = shopCat;
	}

	public int getBranchID() {
		return BranchID;
	}

	public void setBranchID(int branchID) {
		BranchID = branchID;
	}

	public ArrayList<HashMap<String, String>> getSaleProudcts() {
		return saleProudcts;
	}

	public void setSaleProudcts(ArrayList<HashMap<String, String>> saleProudcts) {
		this.saleProudcts = saleProudcts;
	}

	public ArrayList<HashMap<String, String>> loadSaleList(int branchID) {

		return saleProudcts;

	}

	public ShopCatalog getShopCat() {
		return shopCat;
	}

	public void setShopCat(ShopCatalog shopCat) {
		this.shopCat = shopCat;
	}

	

}
