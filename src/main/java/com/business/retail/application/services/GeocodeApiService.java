package com.business.retail.application.services;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.business.retail.application.domain.Shop;
import com.business.retail.application.exceptions.ApplicationException;
import com.business.retail.application.exceptions.InvalidRequestException;
import com.business.retail.application.exceptions.MapLocationNotFoundException;
import com.business.retail.application.geoservice.enums.StatusCode;

@Service
public class GeocodeApiService {
	public static final Logger LOGGER = Logger
			.getLogger(GeocodeApiService.class.getName());
	static final String API_KEY_NAME = "google.geo.api.key";
	private RestTemplate template = new RestTemplate();
	@Autowired
	private Environment env;

	public Shop populateLatLong(Shop shop) {
		String apiKey = env.getProperty(API_KEY_NAME);
		String jsonStr = template.getForObject(
				"https://maps.googleapis.com/maps/api/geocode/json?address="
						+ shop.getShopAddress().toString() + "&key=" + apiKey,
				String.class);
		LOGGER.log(Level.INFO, "JsonString: " + jsonStr);
		JSONObject jsonObj = new JSONObject(jsonStr);
		checkStatus(jsonObj);
		JSONObject resultsObj = jsonObj.getJSONArray("results")
				.getJSONObject(0);
		JSONObject location = resultsObj.getJSONObject("geometry")
				.getJSONObject("location");
		return new Shop(shop.getShopName(), shop.getShopAddress(),
				location.getDouble("lat"), location.getDouble("lng"));
	}

	private void checkStatus(JSONObject jsonObj) {
		String status = jsonObj.getString("status");
		if (StatusCode.ZERO_RESULTS.name().equals(status)) {
			throw new MapLocationNotFoundException(
					"Given address cannot be located on Google Maps. Please check the inputs!");
		}
		if (StatusCode.INVALID_REQUEST.name().equals(status)) {
			throw new InvalidRequestException(
					"Input Request url is invalid. Please check!");
		}
		if (StatusCode.REQUEST_DENIED.name().equals(status)) {
			LOGGER.log(Level.SEVERE,
					"Google Geocoding API key may not be valid anymore. Please check!");
		}
		if (StatusCode.MAX_ELEMENTS_EXCEEDED.name().equals(status)
				|| StatusCode.OVER_QUERY_LIMIT.name().equals(status)) {
			LOGGER.log(Level.SEVERE,
					"Google API query limits may have been exceeded. Please check!");
		}
		if (StatusCode.UNKNOWN_ERROR.name().equals(status)) {
			LOGGER.log(Level.SEVERE,
					"Unknown error. May be Google API is down!");
		}
		if (!StatusCode.OK.name().equals(status)) {
			throw new ApplicationException(
					"Failed to process your request. Please try again later");
		}
	}

	public List<Double> getDistanceInMiles(List<Shop> allShops, Double custLat,
			Double custLong) {
		List<Double> distanceToCustLoc = new ArrayList<Double>();
		StringBuilder origin = new StringBuilder().append(custLat).append(",")
				.append(custLong);
		String destinations = getDestinations(allShops);
		String apiKey = env.getProperty(API_KEY_NAME);
		String jsonStr = template
				.getForObject(
						"https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins="
								+ origin.toString()
								+ "&destinations="
								+ destinations + "&key=" + apiKey, String.class);
		LOGGER.log(Level.INFO, "JsonString: " + jsonStr);
		JSONObject jsonObj = new JSONObject(jsonStr);
		checkStatus(jsonObj);
		JSONObject rowObj = jsonObj.getJSONArray("rows").getJSONObject(0);
		JSONArray elementsArray = rowObj.getJSONArray("elements");

		for (int i = 0; i < allShops.size(); i++) {
			Double distanceInMiles = new Double(-1);
			JSONObject elementsObj = elementsArray.getJSONObject(i);
			String status = elementsObj.getString("status");
			if (StatusCode.OK.name().equals(status)) {
				String distanceStr = elementsObj.getJSONObject("distance")
						.getString("text");
				distanceStr = distanceStr
						.substring(0, distanceStr.length() - 3);
				distanceInMiles = Double.parseDouble(distanceStr);
			}
			distanceToCustLoc.add(distanceInMiles);
		}
		return distanceToCustLoc;

	}

	private String getDestinations(List<Shop> allShops) {
		StringBuilder destinationsBuilder = new StringBuilder();
		for (Shop shop : allShops) {
			destinationsBuilder.append(shop.getShopLatitude()).append(",")
					.append(shop.getShopLongitude()).append("|");
		}
		String destinations = null;
		if (destinationsBuilder.length() > 0) {
			destinations = destinationsBuilder.substring(0,
					destinationsBuilder.length() - 1);
		}
		return destinations;
	}
}
