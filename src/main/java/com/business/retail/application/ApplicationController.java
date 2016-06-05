package com.business.retail.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.business.retail.application.domain.Shop;
import com.business.retail.application.services.ApplicationService;

@Controller
@RequestMapping("/retailshops")
public class ApplicationController {
	static final String ADMIN_INDEX_MSG = "Welcome Retail Manager";
	static final String CUSTOMER_INDEX_MSG = "Welcome Customer";
	@Autowired
	ApplicationService service;

	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> admin_index() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		ResponseEntity<String> response = new ResponseEntity<String>(
				ADMIN_INDEX_MSG, headers, HttpStatus.OK);
		return response;
	}

	@RequestMapping(value = "/customer", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> customer_index() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		ResponseEntity<String> response = new ResponseEntity<String>(
				CUSTOMER_INDEX_MSG, headers, HttpStatus.OK);
		return response;
	}

	@RequestMapping(value = "/admin/addShop", method = RequestMethod.POST)
	public @ResponseBody Shop addShop(@RequestBody Shop shop) {
		return service.addShop(shop);
	}

}
