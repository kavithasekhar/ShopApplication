package com.business.retail.application.services;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.business.retail.application.domain.Shop;

/**
 * Service that access google's geocoding api service. 
 * 
 * @author Kavitha
 *
 */
@Service
public class GeocodeApiService {
	
	@Autowired
	MapApiService mapService;

	public Shop populateLatLong(Shop shop) {
		String apiKey = mapService.getEnv().getProperty(MapApiService.API_KEY_NAME);
		String jsonStr = getGeocodeApiResults(apiKey, shop.getShopAddress()
				.toString());
		JSONObject jsonObj = new JSONObject(jsonStr);
		mapService.checkStatus(jsonObj);
		JSONObject locationObj = getLocationObject(jsonObj);
		return new Shop(shop.getShopName(), shop.getShopAddress(),
				locationObj.getDouble("lat"), locationObj.getDouble("lng"));
	}

	private String getGeocodeApiResults(String apiKey, String address) {
		RestTemplate template = mapService.getTemplate();
		String jsonStr = template.getForObject(
				"https://maps.googleapis.com/maps/api/geocode/json?address="
						+ address + "&key=" + apiKey, String.class);
		return jsonStr;
	}

	private JSONObject getLocationObject(JSONObject jsonObj) {
		JSONObject resultsObj = jsonObj.getJSONArray("results")
				.getJSONObject(0);
		JSONObject locationObj = resultsObj.getJSONObject("geometry")
				.getJSONObject("location");
		return locationObj;
	}
}
