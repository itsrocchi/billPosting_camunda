package it.univaq.billposting.model;

import java.io.Serializable;

public class ZoneData implements Serializable {
	
	
	private static final long serialVersionUID = 7355760466378885872L;
	private int id;
	private String name;
	private String city;
	private Float price;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}
}
