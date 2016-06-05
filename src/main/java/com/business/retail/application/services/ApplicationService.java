package com.business.retail.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.business.retail.application.dao.ShopDao;
import com.business.retail.application.domain.Shop;

@Service
public class ApplicationService {

	@Autowired
	private ShopDao dao;
	@Autowired
	private GeocodeApiService geocodeApiService;

	public Shop addShop(Shop shop) {
		Shop shopWithLatLong = geocodeApiService.populateLatLong(shop);
		dao.addShop(shopWithLatLong);
		return shopWithLatLong;
	}

}
