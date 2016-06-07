package com.business.retail.application.services;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import com.business.retail.application.exceptions.ApplicationException;
import com.business.retail.application.exceptions.InvalidRequestException;
import com.business.retail.application.exceptions.MapLocationNotFoundException;
import com.business.retail.application.geoservice.enums.StatusCode;

/**
 * Service class for any google api related services
 *
 * Pre-requisite for making any google map api request: You need to generate api
 * key from Google Developers Console by creating a project and by specifying
 * the ipaddress from where requests will be made
 * 
 * @author Kavitha
 *
 */
public class MapApiService {
	protected static final String API_KEY_NAME = "google.geo.api.key";
	private static final Logger LOGGER = Logger.getLogger(MapApiService.class
			.getName());
	protected RestTemplate template = new RestTemplate();
	@Autowired
	protected Environment env;

	protected void checkStatus(JSONObject jsonObj) {
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

}
