package com.business.retail.application.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.business.retail.application.dao.ShopDao;
import com.business.retail.application.domain.Shop;
import com.business.retail.application.exceptions.DuplicateException;

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
		if (isShopAlreadyExists(shop)) {
			throw new DuplicateException("Shop already added");
		}
		Shop shopWithLatLong = geocodeApiService.populateLatLong(shop);
		dao.addShop(shopWithLatLong);
		return shopWithLatLong;
	}

	private boolean isShopAlreadyExists(Shop shop) {
		List<Shop> shops = dao.getShops();
		Optional<Shop> matchedShops = shops
				.stream()
				.filter(s -> s.getShopName().equals(shop.getShopName())
						&& s.getShopAddress().equals(shop.getShopAddress()))
				.findFirst();
		return matchedShops.isPresent();
	}

	public List<Shop> getShops() {
		return dao.getShops();
	}

	public List<Shop> findNearestShops(Double latitude, Double longitude) {
		List<Shop> nearestShops = new ArrayList<Shop>();

		List<Shop> allShops = dao.getShops();
		List<Double> distanceToCustLoc = distanceMatrixApiService
				.getDistanceInMiles(allShops, latitude, longitude);
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

	public void setGeocodeApiService(GeocodeApiService geocodeApiService) {
		this.geocodeApiService = geocodeApiService;
	}

	public void setDistanceMatrixApiService(
			DistanceMatrixApiService distanceMatrixApiService) {
		this.distanceMatrixApiService = distanceMatrixApiService;
	}

}
