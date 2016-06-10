package com.business.retail.application.services;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
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
@Service
public class MapApiService {
	protected static final String API_KEY_NAME = "google.geo.api.key";
	public static final String MAP_LOCATION_ERROR_KEY = "map_location_not_found.error_msg";
	public static final String INVALID_REQUEST_ERROR_KEY = "invalid_request.error_msg";
	public static final String ALL_OTHER_ERROR_KEY = "all_others.error_msg";
	private static final Logger LOGGER = Logger.getLogger(MapApiService.class
			.getName());
	protected RestTemplate template = new RestTemplate();
	@Autowired
	protected Environment env;

    void checkStatus(JSONObject jsonObj) {
		String status = jsonObj.getString("status");
		String errorMessage = null; 
		if (StatusCode.ZERO_RESULTS.name().equals(status)) {
			throw new MapLocationNotFoundException(
					env.getProperty(MAP_LOCATION_ERROR_KEY));
		}
		if (StatusCode.INVALID_REQUEST.name().equals(status)) {
			errorMessage = jsonObj.getString("error_message");
			LOGGER.log(Level.SEVERE, errorMessage);
			throw new InvalidRequestException(env.getProperty(INVALID_REQUEST_ERROR_KEY));
		}
		if (!StatusCode.OK.name().equals(status)) {
			errorMessage = jsonObj.getString("error_message");
			LOGGER.log(Level.SEVERE, errorMessage);
			throw new ApplicationException(env.getProperty(ALL_OTHER_ERROR_KEY));
		}
	}

    RestTemplate getTemplate() {
		return template;
	}

	Environment getEnv() {
		return env;
	}
}
