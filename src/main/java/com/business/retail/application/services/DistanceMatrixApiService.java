package com.business.retail.application.services;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.business.retail.application.domain.Shop;
import com.business.retail.application.geoservice.enums.StatusCode;

@Service
public class DistanceMatrixApiService extends MapApiService {

	public List<Double> getDistanceInMiles(List<Shop> allShops, Double custLat,
			Double custLong) {
		List<Double> distanceToCustLoc = new ArrayList<Double>();
		StringBuilder origin = new StringBuilder().append(custLat).append(",")
				.append(custLong);
		String destinations = getDestinations(allShops);
		String apiKey = env.getProperty(API_KEY_NAME);
		String jsonStr = getDistanceMatrixApiResults(origin.toString(),
				destinations, apiKey);
		JSONObject jsonObj = new JSONObject(jsonStr);
		checkStatus(jsonObj);
		parseAndPopulateDistances(jsonObj, allShops, distanceToCustLoc);
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

	private String getDistanceMatrixApiResults(String origin,
			String destinations, String apiKey) {
		String jsonStr = template
				.getForObject(
						"https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins="
								+ origin.toString()
								+ "&destinations="
								+ destinations + "&key=" + apiKey, String.class);
		return jsonStr;
	}

	private void parseAndPopulateDistances(JSONObject jsonObj,
			List<Shop> allShops, List<Double> distanceToCustLoc) {
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
	}
}
