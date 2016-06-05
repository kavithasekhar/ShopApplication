package com.business.retail.application.services;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.business.retail.application.domain.Shop;

@Service
public class GeocodeApiService {
	public static final Logger LOGGER = Logger.getLogger(GeocodeApiService.class.getName());
	static final String API_KEY_NAME = "google.geo.api.key";
	private RestTemplate template = new RestTemplate();
	@Autowired
	private Environment env;
	
	public Shop populateLatLong(Shop shop) {
		String apiKey = env.getProperty(API_KEY_NAME);
		String jsonStr = template.getForObject(
				"https://maps.googleapis.com/maps/api/geocode/json?address="
						+ shop.getShopAddress().toString()
						+ "&key="+apiKey,
				String.class);
		LOGGER.log(Level.INFO, "JsonString: " + jsonStr);
		JSONObject jsonObj = new JSONObject(jsonStr);
		JSONObject resultsObj = jsonObj.getJSONArray("results").getJSONObject(0);
		JSONObject location = resultsObj.getJSONObject("geometry").getJSONObject(
				"location");
		return new Shop(shop.getShopName(), shop.getShopAddress(),
				location.getDouble("lat"), location.getDouble("lng"));
	}
}
