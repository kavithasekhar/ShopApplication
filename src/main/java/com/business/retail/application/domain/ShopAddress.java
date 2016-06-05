package com.business.retail.application.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ShopAddress {
	private String number;
	private String postCode;
	
	@JsonCreator
	public ShopAddress(@JsonProperty("number") String number, @JsonProperty("postCode") String postCode) {
		this.number = number;
		this.postCode = postCode;
	}
	
	public String getNumber() {
		return number;
	}

	public String getPostCode() {
		return postCode;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((number == null) ? 0 : number.hashCode());
		result = prime * result
				+ ((postCode == null) ? 0 : postCode.hashCode());
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
		ShopAddress other = (ShopAddress) obj;
		if (number == null) {
			if (other.number != null)
				return false;
		} else if (!number.equals(other.number))
			return false;
		if (postCode == null) {
			if (other.postCode != null)
				return false;
		} else if (!postCode.equals(other.postCode))
			return false;
		return true;
	}

	public String toString() {
		 StringBuilder builder = new StringBuilder().append(number).append(",").append(postCode);
		 return builder.toString();
	}
}
