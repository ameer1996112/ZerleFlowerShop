package Entity;

public class Address {
	/* AddressID */
	private String AddressID;
	/* City name */
	private String City;
	/* Street address */
	private String Street;
	/* Zip code */
	private String Zip;

	// Constructer
	public Address(String addressID, String city, String street, String zip) {
		super();
		AddressID = addressID;
		City = city;
		Street = street;
		Zip = zip;
	}

	public String getAddressID() {
		return AddressID;
	}

	public void setAddressID(String addressID) {
		AddressID = addressID;
	}

	public String getCity() {
		return City;
	}

	public void setCity(String city) {
		City = city;
	}

	public String getStreet() {
		return Street;
	}

	public void setStreet(String street) {
		Street = street;
	}

	public String getZip() {
		return Zip;
	}

	public void setZip(String zip) {
		Zip = zip;
	}

}
