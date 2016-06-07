package com.business.retail.application.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Enabling Basic Http Authentication
 * 
 * @author Kavitha
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	Environment env;
	public static final String ADMIN_USERNAME_KEY = "admin.username";
	public static final String ADMIN_PASSWORD_KEY = "admin.password";
	public static final String USER_USERNAME_KEY = "user.username";
	public static final String USER_PASSWORD_KEY = "user.password";
	private static final String ADMIN = "ADMIN";
	private static final String USER = "USER";

	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().anyRequest().fullyAuthenticated();
		http.httpBasic();
		http.csrf().disable();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.inMemoryAuthentication()
				.withUser(env.getProperty(ADMIN_USERNAME_KEY)).roles(ADMIN)
				.password(env.getProperty(ADMIN_PASSWORD_KEY)).and()
				.withUser(env.getProperty(USER_USERNAME_KEY)).roles(USER)
				.password(env.getProperty(USER_PASSWORD_KEY));
	}
}
