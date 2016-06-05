package com.business.retail.application.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Shop {
	private String shopName;
	private ShopAddress shopAddress;
	private Double shopLatitude;
	private Double shopLongitude;

	@JsonCreator
	public Shop(@JsonProperty("shopName") String shopName,
			@JsonProperty("shopAddress") ShopAddress shopAddress,
			@JsonProperty("shopLatitude") Double shopLatitude,
			@JsonProperty("shopLongitude") Double shopLongitude) {
		this.shopName = shopName;
		this.shopAddress = shopAddress;
		this.shopLatitude = shopLatitude;
		this.shopLongitude = shopLongitude;
	}

	public String getShopName() {
		return shopName;
	}

	public ShopAddress getShopAddress() {
		return shopAddress;
	}

	public Double getShopLatitude() {
		return shopLatitude;
	}

	public Double getShopLongitude() {
		return shopLongitude;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((shopAddress == null) ? 0 : shopAddress.hashCode());
		result = prime * result
				+ ((shopLatitude == null) ? 0 : shopLatitude.hashCode());
		result = prime * result
				+ ((shopLongitude == null) ? 0 : shopLongitude.hashCode());
		result = prime * result
				+ ((shopName == null) ? 0 : shopName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Shop other = (Shop) obj;
		if (shopAddress == null) {
			if (other.shopAddress != null)
				return false;
		} else if (!shopAddress.equals(other.shopAddress))
			return false;
		if (shopLatitude == null) {
			if (other.shopLatitude != null)
				return false;
		} else if (!shopLatitude.equals(other.shopLatitude))
			return false;
		if (shopLongitude == null) {
			if (other.shopLongitude != null)
				return false;
		} else if (!shopLongitude.equals(other.shopLongitude))
			return false;
		if (shopName == null) {
			if (other.shopName != null)
				return false;
		} else if (!shopName.equals(other.shopName))
			return false;
		return true;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder().append(shopName)
				.append("\n").append(shopAddress.toString()).append("\n")
				.append(shopLatitude).append("\n").append(shopLongitude);
		return builder.toString();
	}
}
