package com.business.retail.application.services;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.Environment;

import com.business.retail.application.domain.Shop;
import com.business.retail.application.domain.ShopAddress;

@RunWith(MockitoJUnitRunner.class)
public class GeocodeApiServiceTest {
	@InjectMocks
	GeocodeApiService geocodeApiService;
	private static final String testApiKey = "AIzaSyButDkiIyal_JAtixC-hRjxU1JfooFjIKE";

	@Mock
	Environment env;

	@Before
	public void setUp() {
		when(env.getProperty(GeocodeApiService.API_KEY_NAME)).thenReturn(testApiKey);
	}

	@Test
	public void testPopulateLatLong() {
		String shopName = "Tesco";
		ShopAddress address = new ShopAddress("209", "SE13 7PY");
		Shop shop = new Shop(shopName, address, null, null);
		Shop shopWithLatLong = geocodeApiService.populateLatLong(shop);
		Shop expectedShopWithLatLong = new Shop(shopName, address, new Double(
				51.4656697), new Double(-0.0106995));
		assertTrue(expectedShopWithLatLong.equals(shopWithLatLong));
	}

}
