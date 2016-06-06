package com.business.retail.application.dao;

import java.util.List;

import com.business.retail.application.domain.Shop;

public interface ShopDao {

	void addShop(Shop shop);
	
	List<Shop> getShops();

}
