package com.business.retail.application;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.business.retail.application.domain.Shop;
import com.business.retail.application.domain.ShopAddress;
import com.business.retail.application.exceptions.MapLocationNotFoundException;
import com.business.retail.application.services.MapApiService;

/**
 * 
 * Integration Tests for exception scenarios
 * 
 * @author Kavitha
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(ApplicationRunner.class)
@WebIntegrationTest
public class ApplicationControllerExceptionIT extends BaseTest {
	@Test
	public void testAddShop_WithInvalidAddress() {
		Mockito.when(geocodeApiService.populateLatLong(Mockito.any(Shop.class)))
				.thenThrow(
						new MapLocationNotFoundException(
								env.getProperty(MapApiService.MAP_LOCATION_ERROR_KEY)));

		String shopName = "Tesco";
		ShopAddress address = new ShopAddress("22", "SE13 7LL");
		Shop shop = new Shop(shopName, address, null, null);
		ResponseEntity<Shop> response = hitAddShopUrl(shop);
		assertTrue(HttpStatus.BAD_REQUEST.equals(response.getStatusCode()));
		assertTrue(service.getShops().size() == 0);
	}

	@Test
	public void testFindNearestShops_WithNoShopsInDB() {
		// No Shops in Database
		String custLat = "51.46739299999999";
		String custLong = "-0.0222004";
		Mockito.when(
				distanceMatrixApiService.getDistanceInMiles(
						Mockito.any(List.class), Mockito.anyDouble(),
						Mockito.anyDouble())).thenReturn(
				new ArrayList<Double>());

		ResponseEntity<Shop[]> response = hitFindNearestShopsUrl(custLat,
				custLong);

		Shop[] nearestShopsArr = response.getBody();
		List<Shop> nearestShops = Arrays.asList(nearestShopsArr);
		assertNotNull(nearestShops);
		assertTrue(0 == nearestShops.size());
	}

	@Test
	public void testAdminIndexWithUserCredentials() {
		ResponseEntity<String> response = template.exchange(baseUrl + "/admin",
				HttpMethod.GET, new HttpEntity(userHeaders), String.class);
		assertTrue(HttpStatus.FORBIDDEN.equals(response.getStatusCode()));
	}

	@Test
	public void testAddingSameShopMultipleTimes() {
		String shopName = "Tesco";
		ShopAddress address = new ShopAddress("23 Falcon Road", "SW11 2PJ");

		Shop mockShop = new Shop(shopName, address, new Double(51), new Double(
				-0.012));
		Mockito.when(geocodeApiService.populateLatLong(Mockito.any(Shop.class)))
				.thenReturn(mockShop);

		Shop shop = new Shop(shopName, address, null, null);
		ResponseEntity<Shop> response1 = hitAddShopUrl(shop);
		assertTrue(HttpStatus.OK.equals(response1.getStatusCode()));
		assertTrue(service.getShops().size() == 1);

		ResponseEntity<Shop> response2 = hitAddShopUrl(shop);
		assertTrue(HttpStatus.CONFLICT.equals(response2.getStatusCode()));
		assertTrue(service.getShops().size() == 1);
	}

	// Input Validation Test Scenarios
	@Test
	public void testAddShopWithEmptyShopName() {
		String shopName = "";
		ShopAddress address = new ShopAddress("23 Falcon Road", "SW11 2PJ");

		Shop shop = new Shop(shopName, address, null, null);
		ResponseEntity<Shop> response = hitAddShopUrl(shop);
		assertTrue(HttpStatus.BAD_REQUEST.equals(response.getStatusCode()));
		assertTrue(service.getShops().size() == 0);
	}

	@Test
	public void testAddShopWithNullShopAddress() {
		String shopName = "Tesco";
		ShopAddress address = null;

		Shop shop = new Shop(shopName, address, null, null);
		ResponseEntity<Shop> response = hitAddShopUrl(shop);
		assertTrue(HttpStatus.BAD_REQUEST.equals(response.getStatusCode()));
		assertTrue(service.getShops().size() == 0);
	}

	@Test
	public void testAddShopWithEmptyShopAddressNumber() {
		String shopName = "Tesco";
		ShopAddress address = new ShopAddress("", "SW11 2PJ");

		Shop shop = new Shop(shopName, address, null, null);
		ResponseEntity<Shop> response = hitAddShopUrl(shop);
		assertTrue(HttpStatus.BAD_REQUEST.equals(response.getStatusCode()));
		assertTrue(service.getShops().size() == 0);
	}

	@Test
	public void testAddShopWithEmptyShopAddressPostCode() {
		String shopName = "Tesco";
		ShopAddress address = new ShopAddress("23 Falcon Road", "");

		Shop shop = new Shop(shopName, address, null, null);
		ResponseEntity<Shop> response = hitAddShopUrl(shop);
		assertTrue(HttpStatus.BAD_REQUEST.equals(response.getStatusCode()));
		assertTrue(service.getShops().size() == 0);
	}
}
