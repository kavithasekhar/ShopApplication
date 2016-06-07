package com.business.retail.application;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.business.retail.application.domain.Shop;
import com.business.retail.application.exceptions.ApplicationException;
import com.business.retail.application.exceptions.InvalidRequestException;
import com.business.retail.application.exceptions.MapLocationNotFoundException;
import com.business.retail.application.services.ApplicationService;

/**
 * REST controller for adding shops and finding nearest shops to customer
 * location
 * 
 * @author Kavitha
 *
 */
@Controller
@RequestMapping("/retailshops")
public class ApplicationController {
	static final String ADMIN_INDEX_MSG = "Welcome Retail Manager";
	static final String CUSTOMER_INDEX_MSG = "Welcome Customer";
	@Autowired
	private ApplicationService service;

	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public @ResponseBody String admin_index() {
		service.ensureAdmin();
		return ADMIN_INDEX_MSG;
	}

	@RequestMapping(value = "/customer", method = RequestMethod.GET)
	public @ResponseBody String customer_index() {
		return CUSTOMER_INDEX_MSG;
	}

	@RequestMapping(value = "/admin/addShop", method = RequestMethod.POST)
	public @ResponseBody Shop addShop(@RequestBody Shop shop) {
		service.ensureAdmin();
		return service.addShop(shop);
	}

	@RequestMapping(value = "/customer/findNearestShops", method = RequestMethod.GET)
	public @ResponseBody List<Shop> findNearestShops(
			@RequestParam(value = "custLat") String custLat,
			@RequestParam(value = "custLong") String custLong) {
		return service.findNearestShops(custLat, custLong);
	}

	@ExceptionHandler(ApplicationException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public void handleGenericExceptions(HttpServletResponse response,
			ApplicationException exception) throws IOException {
		response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
				exception.toString());
	}

	@ExceptionHandler({ InvalidRequestException.class,
			MapLocationNotFoundException.class })
	@ResponseBody
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public void handleBadRequests(HttpServletResponse response,
			ApplicationException exception) throws IOException {
		response.sendError(HttpServletResponse.SC_BAD_REQUEST,
				exception.toString());
	}
}
