package com.business.retail.application.domain;

public class Shop {
	private String shopName;
	private ShopAddress shopAddress;
	private String shopLatitude;
	private String shopLongitude;
	
	public String getShopName() {
		return shopName;
	}
	
	public ShopAddress getShopAddress() {
		return shopAddress;
	}
	
	public String getShopLatitude() {
		return shopLatitude;
	}
	
	public String getShopLongitude() {
		return shopLongitude;
	}
}
