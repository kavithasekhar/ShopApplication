package com.business.retail.application;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.business.retail.application.domain.Shop;
import com.business.retail.application.domain.ShopAddress;

/**
 * Integration Test for positive scenarios
 * 
 * @author Kavitha
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(ApplicationRunner.class)
@WebIntegrationTest
public class ApplicationControllerIT extends BaseTest {

	@Test
	public void testAdminIndex() {
		ResponseEntity<String> response = template.exchange(baseUrl + "/admin",
				HttpMethod.GET, new HttpEntity(adminHeaders), String.class);
		assertEquals(ApplicationController.ADMIN_INDEX_MSG, response.getBody());
	}

	@Test
	public void testCustomerIndex() {
		ResponseEntity<String> response = template.exchange(baseUrl
				+ "/customer", HttpMethod.GET, new HttpEntity(userHeaders),
				String.class);
		assertEquals(ApplicationController.CUSTOMER_INDEX_MSG,
				response.getBody());
	}

	@Test
	public void testAddShop() {
		String shopName = "Tesco";
		ShopAddress address = new ShopAddress("209 Lewisham Road", "SE13 7PY");
		Shop expectedShop = new Shop(shopName, address, new Double(51),
				new Double(-0.012));
		Mockito.when(geocodeApiService.populateLatLong(Mockito.any(Shop.class)))
				.thenReturn(expectedShop);

		Shop shop = new Shop(shopName, address, null, null);
		ResponseEntity<Shop> response = hitAddShopUrl(shop);
		assertTrue(expectedShop.equals(response.getBody()));
		assertTrue(service.getShops().size() == 1);
	}

	@Test
	public void testFindNearestShops() {
		String shopName = "Tesco";
		ShopAddress address1 = new ShopAddress("209 Lewisham Road", "SE13 7PY");
		Shop expectedShop1 = new Shop(shopName, address1,
				new Double(51.4664441), new Double(-0.0124755));
		Mockito.when(geocodeApiService.populateLatLong(Mockito.any(Shop.class)))
				.thenReturn(expectedShop1);
		Shop shop1 = new Shop(shopName, address1, null, null);
		ResponseEntity<Shop> response1 = hitAddShopUrl(shop1);
		assertTrue(expectedShop1.equals(response1.getBody()));
		assertTrue(service.getShops().size() == 1);

		ShopAddress address2 = new ShopAddress("23 Falcon Road", "SW11 2PJ");
		Shop expectedShop2 = new Shop(shopName, address2,
				new Double(51.4694035), new Double(-0.1711022));
		Mockito.when(geocodeApiService.populateLatLong(Mockito.any(Shop.class)))
				.thenReturn(expectedShop2);
		Shop shop2 = new Shop(shopName, address2, null, null);
		ResponseEntity<Shop> response2 = hitAddShopUrl(shop2);
		assertTrue(expectedShop2.equals(response2.getBody()));
		assertTrue(service.getShops().size() == 2);

		String custLat = "51.46739299999999";
		String custLong = "-0.0222004";
		List<Double> mockDistanceValues = new ArrayList<Double>(Arrays.asList(
				0.7, 7.5));
		Mockito.when(
				distanceMatrixApiService.getDistanceInMiles(
						Mockito.any(List.class), Mockito.anyDouble(),
						Mockito.anyDouble())).thenReturn(mockDistanceValues);
		ResponseEntity<Shop[]> response = hitFindNearestShopsUrl(custLat,
				custLong);

		Shop[] nearestShopsArr = response.getBody();
		List<Shop> nearestShops = Arrays.asList(nearestShopsArr);
		assertNotNull(nearestShops);
		assertTrue(1 == nearestShops.size());
		assertTrue(expectedShop1.equals(nearestShops.get(0)));
	}
}