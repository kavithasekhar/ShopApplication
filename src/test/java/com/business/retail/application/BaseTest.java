package com.business.retail.application;

import org.apache.tomcat.util.codec.binary.Base64;
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
import com.business.retail.application.security.SecurityConfig;
import com.business.retail.application.services.ApplicationService;

/**
 * Common code for controller testing
 * 
 * @author Kavitha
 *
 */
public class BaseTest {
	protected String baseUrl;
	protected RestTemplate template = new TestRestTemplate();
	protected HttpHeaders adminHeaders = new HttpHeaders();
	protected HttpHeaders userHeaders = new HttpHeaders();

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
		
		String adminPlainCreds = env.getProperty(SecurityConfig.ADMIN_USERNAME_KEY) + ":" +  env.getProperty(SecurityConfig.ADMIN_PASSWORD_KEY);
        byte[] adminPlainCredsBytes = adminPlainCreds.getBytes();
        byte[] adminBase64CredsBytes = Base64.encodeBase64(adminPlainCredsBytes);
        String adminBase64Creds = new String(adminBase64CredsBytes);

        adminHeaders.add("Authorization", "Basic " + adminBase64Creds);
        
        String userPlainCreds = env.getProperty(SecurityConfig.USER_USERNAME_KEY) + ":" +  env.getProperty(SecurityConfig.USER_PASSWORD_KEY);
        byte[] userPlainCredsBytes = userPlainCreds.getBytes();
        byte[] userBase64CredsBytes = Base64.encodeBase64(userPlainCredsBytes);
        String userBase64Creds = new String(userBase64CredsBytes);

        userHeaders.add("Authorization", "Basic " + userBase64Creds);
	}
	
	@After
	public void cleanUp() {
		service.getShops().clear();
	}
	
	protected ResponseEntity<Shop> hitAddShopUrl(Shop shop) {
		ResponseEntity<Shop> response = template.exchange(baseUrl
				+ "/admin/addShop", HttpMethod.POST, new HttpEntity<Shop>(
				shop, adminHeaders), Shop.class);
		return response;
	}
	
	protected ResponseEntity<Shop[]> hitFindNearestShopsUrl(Double custLat, Double custLong) {
		ResponseEntity<Shop[]> response = template.exchange(baseUrl
				+ "/customer/findNearestShops?custLat=" + custLat
				+ "&custLong=" + custLong, HttpMethod.GET, new HttpEntity(
				userHeaders), Shop[].class);
		return response;
	}
}
