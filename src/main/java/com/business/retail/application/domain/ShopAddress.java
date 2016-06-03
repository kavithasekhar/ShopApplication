package com.business.retail.application.domain;

public class ShopAddress {
	private String number;
	private String postCode;
	
	ShopAddress(String number, String postCode) {
		this.number = number;
		this.postCode = postCode;
	}
	
	public String getNumber() {
		return number;
	}

	public String getPostCode() {
		return postCode;
	}
}
