package com.business.retail.application.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.business.retail.application.dao.ShopDao;
import com.business.retail.application.domain.Shop;

/**
 * Main application service class which accesses other service/dao classes to
 * perform business functions
 * 
 * @author Kavitha
 *
 */
@Service
public class ApplicationService {

	private static final String RADIUS_KEY_NAME = "customer.preferred.radius.inmiles";
	@Autowired
	private ShopDao dao;
	@Autowired
	private GeocodeApiService geocodeApiService;
	@Autowired
	private DistanceMatrixApiService distanceMatrixApiService;
	@Autowired
	Environment env;

	public Shop addShop(Shop shop) {
		Shop shopWithLatLong = geocodeApiService.populateLatLong(shop);
		dao.addShop(shopWithLatLong);
		return shopWithLatLong;
	}

	public List<Shop> getShops() {
		return dao.getShops();
	}

	public List<Shop> findNearestShops(String custLat, String custLong) {
		List<Shop> nearestShops = new ArrayList<Shop>();
		List<Shop> allShops = dao.getShops();
		List<Double> distanceToCustLoc = distanceMatrixApiService
				.getDistanceInMiles(allShops, new Double(custLat), new Double(
						custLong));
		Double radius = Double.parseDouble(env.getProperty(RADIUS_KEY_NAME));

		for (int i = 0; i < allShops.size(); i++) {
			if (distanceToCustLoc.get(i) >= 0
					&& distanceToCustLoc.get(i) <= radius) {
				nearestShops.add(allShops.get(i));
			}
		}
		return nearestShops;
	}

	@PreAuthorize("hasRole('ADMIN')")
	public boolean ensureAdmin() {
		return true;
	}

}
