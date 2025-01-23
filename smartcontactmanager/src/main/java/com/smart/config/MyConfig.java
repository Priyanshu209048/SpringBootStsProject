package com.smart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class MyConfig {
	
	@Bean
	public UserDetailsService getUserDetailService() {
		
		return  new UserDetailsServiceImpl();
		
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		
		return new BCryptPasswordEncoder();
		
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		
		daoAuthenticationProvider.setUserDetailsService(this.getUserDetailService());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		
		return daoAuthenticationProvider;
		
	}
	
	
	//Configure Method...
	@Bean
	protected AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
		
		return auth.getAuthenticationManager();
		
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	       http.csrf(AbstractHttpConfigurer::disable)
	       		.authorizeHttpRequests(authorizeRequests -> authorizeRequests
	       			.requestMatchers("/admin/**")
	       			.hasRole("ADMIN")
	       			.requestMatchers("/user/**")
	       			.hasRole("USER")
	       			.requestMatchers("/**").permitAll());

	       		http.formLogin(formLogin -> formLogin.
						loginPage("/signin")
	       			.loginProcessingUrl("/dologin")
	       			.defaultSuccessUrl("/user/index"));
	       //.failureUrl("/login-fail") we can also add this for error
			http.authenticationProvider(authenticationProvider());

		return http.build();

	}
	

}
