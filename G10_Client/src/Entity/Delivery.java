package Entity;

import java.time.LocalDate;

public class Delivery {
	private String OrderID;
	private String AddressCity;
	private String FirstName;
	private String LastName;
	private String Phone;
	private LocalDate deliveryDate;

	public Delivery(String orderID, String addressCity, String firstName, String lastName, String shippingTelephones,
			LocalDate date) {
		super();
		OrderID = orderID;
		AddressCity = addressCity;
		FirstName = firstName;
		LastName = lastName;
		Phone = shippingTelephones;
		this.deliveryDate = date;
	}

	public String getOrderID() {
		return OrderID;
	}

	public void setOrderID(String orderID) {
		OrderID = orderID;
	}

	public String getAddressCity() {
		return AddressCity;
	}

	public void setAddressCity(String addressCity) {
		AddressCity = addressCity;
	}

	public String getFirstName() {
		return FirstName;
	}

	public void setFirstName(String firstName) {
		FirstName = firstName;
	}

	public String getLastName() {
		return LastName;
	}

	public void setLastName(String lastName) {
		LastName = lastName;
	}

	public String getPhone() {
		return Phone;
	}

	public void setPhone(String phone) {
		Phone = phone;
	}

	public LocalDate getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(LocalDate deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
}
