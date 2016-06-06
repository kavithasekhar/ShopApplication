package com.business.retail.application.dao;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;

import com.business.retail.application.domain.Shop;

/**
 * In-memory form of persistence implementation
 * 
 * @author Kavitha
 *
 */
@Component
public class InMemoryDaoImpl implements ShopDao {
	private List<Shop> shops = new CopyOnWriteArrayList<Shop>();

	@Override
	public void addShop(Shop shop) {
		shops.add(shop);
	}

	public List<Shop> getShops() {
		return shops;
	}
}
