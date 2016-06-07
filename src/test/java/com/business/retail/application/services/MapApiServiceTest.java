package com.business.retail.application.services;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.Environment;

import com.business.retail.application.domain.Shop;
import com.business.retail.application.domain.ShopAddress;

/**
 * 
 * Integration Test covering the google api json result parsing
 * 
 * @author Kavitha
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class MapApiServiceTest {

	private static final String testApiKey = "AIzaSyDh0Mk09HWROLsphUuCKd8_BDxRHTBYgkc";
	
	@InjectMocks
	GeocodeApiService geocodeApiService;

	@InjectMocks
	DistanceMatrixApiService distanceMatrixApiService;

	@Mock
	Environment env;

	@Before
	public void setUp() {
		when(env.getProperty(GeocodeApiService.API_KEY_NAME)).thenReturn(
				testApiKey);
	}

	@Test
	public void testPopulateLatLong() {
		String shopName = "Tesco";
		ShopAddress address = new ShopAddress("209 Lewisham Road", "SE13 7PY");
		Shop shop = new Shop(shopName, address, null, null);
		Shop shopWithLatLong = geocodeApiService.populateLatLong(shop);
		Shop expectedShopWithLatLong = new Shop(shopName, address, new Double(51.4664441),
				new Double(-0.0124755));
		assertTrue(expectedShopWithLatLong.equals(shopWithLatLong));
	}

	@Test
	public void testGetDistanceInMiles() {
		String shopName = "Tesco";
		ShopAddress address1 = new ShopAddress("209 Lewisham Road", "SE13 7PY");
		Shop shop1 = new Shop(shopName, address1, new Double(51.4664441),
				new Double(-0.0124755));
		
		ShopAddress address2 = new ShopAddress("23 Falcon Road", "SW11 2PJ");
		Shop shop2 = new Shop(shopName, address2, new Double(51.4694035),
				new Double(-0.1711022));

		List<Shop> allShops = new ArrayList<Shop>();
		allShops.add(shop1);
		allShops.add(shop2);

		Double custLat = new Double(51.46739299999999);
		Double custLong = new Double(-0.0222004);
		
		List<Double> distancesToCustLoc = distanceMatrixApiService
				.getDistanceInMiles(allShops, custLat, custLong);
		
		assertTrue(0.7==distancesToCustLoc.get(0).doubleValue());
		assertTrue(7.5==distancesToCustLoc.get(1).doubleValue());
	}

}
