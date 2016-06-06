package com.business.retail.application.dao;

import java.util.List;

import com.business.retail.application.domain.Shop;

/**
 * Generic Interface to support different persistence mechanisms
 * 
 * @author Kavitha
 *
 */
public interface ShopDao {

	void addShop(Shop shop);
	
	List<Shop> getShops();

}
