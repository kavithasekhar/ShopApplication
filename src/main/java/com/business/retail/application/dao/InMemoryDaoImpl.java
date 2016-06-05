package com.business.retail.application.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.business.retail.application.domain.Shop;

@Component
public class InMemoryDaoImpl implements ShopDao {
	private List<Shop> shops = new ArrayList<Shop>();

	@Override
	public void addShop(Shop shop) {
		shops.add(shop);
	}
}
