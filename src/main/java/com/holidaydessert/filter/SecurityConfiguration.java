package com.holidaydessert.filter;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration {

	@Bean
	public UserDetailsService userDetailsService(BCryptPasswordEncoder bCryptPasswordEncoder) {
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		manager.createUser(User.withUsername("user")
					.password(bCryptPasswordEncoder.encode("userPass"))
					.roles("USER")
					.build());
		manager.createUser(User.withUsername("admin")
					.password(bCryptPasswordEncoder.encode("adminPass"))
					.roles("USER", "ADMIN")
					.build());
		return manager;
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http.csrf().disable()
			.authorizeRequests()
			.antMatchers(HttpMethod.DELETE)
			.hasRole("ADMIN")
			.antMatchers("/admin/**")
			.hasAnyRole("ADMIN")
			.antMatchers("/user/**")
			.hasAnyRole("USER", "ADMIN")
			.antMatchers("/login/**")
			.anonymous()
			.anyRequest()
			.authenticated()
			.and().httpBasic()
			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		return http.build();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().antMatchers("/images/**", "/css/**", "/js/**", "/webjars/**", "/lib/**", "/favicon.ico");
	}

}
