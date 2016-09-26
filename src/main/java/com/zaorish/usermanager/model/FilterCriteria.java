package com.zaorish.usermanager.model;

public class FilterCriteria {

	private String country;

	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public String toString() {
		return "FilterCriteria{" +
				"country='" + getCountry() + '\'' +
				'}';
	}

}
