package com.business.retail.application.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.business.retail.application.ApplicationRunner;
import com.business.retail.application.controller.ApplicationController;
import com.business.retail.application.domain.Shop;
import com.business.retail.application.domain.ShopAddress;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(ApplicationRunner.class)
@WebIntegrationTest
public class ApplicationControllerIT extends BaseTest {
	@Test
	public void testAdminIndex() throws Exception {
		ResponseEntity<String> response = template.exchange(baseUrl + "/admin",
				HttpMethod.GET, new HttpEntity(headers), String.class);
		assertEquals(ApplicationController.ADMIN_INDEX_MSG, response.getBody());
	}

	@Test
	public void testCustomerIndex() throws Exception {
		ResponseEntity<String> response = template.exchange(baseUrl
				+ "/customer", HttpMethod.GET, new HttpEntity(headers),
				String.class);
		assertEquals(ApplicationController.CUSTOMER_INDEX_MSG,
				response.getBody());
	}

	@Test
	public void testAddShop() throws Exception {
		String shopName = "Tesco";
		ShopAddress address = new ShopAddress("209 Lewisham Road", "SE13 7PY");
		Shop shop = new Shop(shopName, address, null, null);
		ResponseEntity<Shop> response = hitAddShopUrl(shop);
		Shop expectedShop = new Shop(shopName, address, new Double(51.4664441),
				new Double(-0.0124755));
		assertTrue(expectedShop.equals(response.getBody()));
		assertTrue(service.getShops().size() == 1);
	}

	@Test
	public void testFindNearestShops() throws Exception {
		String shopName = "Tesco";
		ShopAddress address1 = new ShopAddress("209 Lewisham Road", "SE13 7PY");
		Shop shop1 = new Shop(shopName, address1, null, null);
		ResponseEntity<Shop> response1 = hitAddShopUrl(shop1);
		Shop expectedShop1 = new Shop(shopName, address1,
				new Double(51.4664441), new Double(-0.0124755));
		assertTrue(expectedShop1.equals(response1.getBody()));
		assertTrue(service.getShops().size() == 1);

		ShopAddress address2 = new ShopAddress("23 Falcon Road", "SW11 2PJ");
		Shop shop2 = new Shop(shopName, address2, null, null);
		ResponseEntity<Shop> response2 = hitAddShopUrl(shop2);
		Shop expectedShop2 = new Shop(shopName, address2,
				new Double(51.4694035), new Double(-0.1711022));
		assertTrue(expectedShop2.equals(response2.getBody()));
		assertTrue(service.getShops().size() == 2);

		Double custLat = new Double(51.46739299999999);
		Double custLong = new Double(-0.0222004);
		ResponseEntity<Shop[]> response = hitFindNearestShopsUrl(custLat,
				custLong);

		Shop[] nearestShopsArr = response.getBody();
		List<Shop> nearestShops = Arrays.asList(nearestShopsArr);
		assertNotNull(nearestShops);
		assertTrue(1 == nearestShops.size());
		assertTrue(expectedShop1.equals(nearestShops.get(0)));
	}
}