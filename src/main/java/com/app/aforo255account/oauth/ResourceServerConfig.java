package com.app.aforo255account.oauth;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		// TODO Auto-generated method stub
		resources.tokenStore(tokenStore());
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		http.authorizeRequests().antMatchers("/api/security/oauth/**").permitAll()
		.antMatchers(HttpMethod.GET,"/api/account/listar","/api/account/ver/{id}","/api/historical/listar","/api/historical/transaction/{account_id}").hasAnyRole("ADMIN","USER")
		.antMatchers("/api/deposit/**").hasAnyRole("ADMIN","USER")
		.antMatchers("/api/withdrawal/**").hasAnyRole("ADMIN","USER")
		.anyRequest().authenticated()
		.and().cors().configurationSource(configurationSource());
	}
	
	
	private CorsConfigurationSource configurationSource() {
		CorsConfiguration corsCgf= new CorsConfiguration();
		corsCgf.setAllowedOrigins( Arrays.asList("*"));
		corsCgf.setAllowedMethods(Arrays.asList("*"));
		corsCgf.setAllowedHeaders(Arrays.asList("*"));
		corsCgf.setAllowCredentials(true);
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsCgf);
		return source;
	}
	
	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter(){
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>( new  CorsFilter(configurationSource()) );
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;
	}

	@Bean
	public JwtTokenStore tokenStore() {
		// TODO Auto-generated method stub
		return new JwtTokenStore(accessTokenConverter());
	}
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {

		JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
		jwtAccessTokenConverter.setSigningKey("aforo255");
		return jwtAccessTokenConverter;
	}
	
}
