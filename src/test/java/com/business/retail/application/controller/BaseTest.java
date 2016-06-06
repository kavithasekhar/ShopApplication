package com.business.retail.application.controller;

import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.business.retail.application.domain.Shop;
import com.business.retail.application.services.ApplicationService;

public class BaseTest {
	protected String baseUrl;
	protected RestTemplate template = new TestRestTemplate();
	protected HttpHeaders headers = new HttpHeaders();

	@Autowired
	protected ApplicationService service;

	@Autowired
	protected Environment env;

	@Before
	public void setUp() {
		String host = env.getProperty("server.name");
		int port = Integer.parseInt(env.getProperty("server.port"));

		baseUrl = new StringBuilder().append("http://").append(host)
				.append(":").append(port).append("/retailshops").toString();
	}
	
	@After
	public void cleanUp() {
		service.getShops().clear();
	}
	
	protected ResponseEntity<Shop> hitAddShopUrl(Shop shop) {
		ResponseEntity<Shop> response = template.exchange(baseUrl
				+ "/admin/addShop", HttpMethod.POST, new HttpEntity<Shop>(
				shop, headers), Shop.class);
		return response;
	}
	
	protected ResponseEntity<Shop[]> hitFindNearestShopsUrl(Double custLat, Double custLong) {
		ResponseEntity<Shop[]> response = template.exchange(baseUrl
				+ "/customer/findNearestShops?custLat=" + custLat
				+ "&custLong=" + custLong, HttpMethod.GET, new HttpEntity(
				headers), Shop[].class);
		return response;
	}
}
