package com.business.retail.application;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.business.retail.application.domain.Shop;
import com.business.retail.application.domain.ShopAddress;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(ApplicationRunner.class)
@WebIntegrationTest
public class ApplicationControllerExceptionIT extends BaseTest {
	@Test
	public void testAddShop_WithInvalidAddress() throws Exception {
		String shopName = "Tesco";
		ShopAddress address = new ShopAddress("22", "SE13 7LL");
		Shop shop = new Shop(shopName, address, null, null);
		ResponseEntity<Shop> response = hitAddShopUrl(shop);
		assertTrue(HttpStatus.BAD_REQUEST.equals(response.getStatusCode()));
		assertTrue(service.getShops().size() == 0);
	}
	
	@Test
	public void testFindNearestShops_WithNoShopsInDB() throws Exception {
		//No Shops in Database
		Double custLat = new Double(51.46739299999999);
		Double custLong = new Double(-0.0222004);
		ResponseEntity<Shop[]> response = hitFindNearestShopsUrl(custLat, custLong);

		Shop[] nearestShopsArr = response.getBody();
		List<Shop> nearestShops = Arrays.asList(nearestShopsArr);
		assertNotNull(nearestShops);
		assertTrue(0 == nearestShops.size());
	}
}
