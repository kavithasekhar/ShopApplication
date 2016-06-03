package com.business.retail.application;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(ApplicationRunner.class)
@WebIntegrationTest
public class ApplicationControllerTest {

	private String baseUrl;
	private RestTemplate template = new TestRestTemplate();
	private HttpHeaders headers = new HttpHeaders();

	@Autowired
	ApplicationService service;

	@Autowired
	Environment env;

	@Before
	public void setUp() {
		String host = env.getProperty("server.name");
		int port = Integer.parseInt(env.getProperty("server.port"));

		baseUrl = new StringBuilder().append("http://").append(host)
				.append(":").append(port).append("/retailshops").toString();
	}

	@Test
	public void testAdminIndex() throws Exception {
		ResponseEntity<String> response = template.exchange(baseUrl + "/admin",
				HttpMethod.GET, new HttpEntity(headers), String.class);
		assertEquals(ApplicationController.ADMIN_INDEX_MSG, response.getBody());
	}

	@Test
	public void testCustomerIndex() throws Exception {
		ResponseEntity<String> response = template.exchange(baseUrl + "/customer",
				HttpMethod.GET, new HttpEntity(headers), String.class);
		assertEquals(ApplicationController.CUSTOMER_INDEX_MSG, response.getBody());
	}

}