//package com.holidaydessert.filter;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
//public class SecurityConfiguration {
//
//	@Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//	
//	@Bean
//	public UserDetailsService userDetailsService(BCryptPasswordEncoder bCryptPasswordEncoder) {
//		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//		manager.createUser(User.withUsername("user")
//					.password(bCryptPasswordEncoder.encode("userPass"))
//					.roles("USER")
//					.build());
//		manager.createUser(User.withUsername("admin")
//					.password(bCryptPasswordEncoder.encode("adminPass"))
//					.roles("USER", "ADMIN")
//					.build());
//		return manager;
//	}
//	
//	@Bean
//	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
//		
//		httpSecurity.csrf().disable()
//			.authorizeRequests()
//			.antMatchers(HttpMethod.DELETE)
//			.hasRole("ADMIN")
//			.antMatchers("/admin/**")
//			.hasAnyRole("ADMIN")
//			.antMatchers("/user/**")
//			.hasAnyRole("USER", "ADMIN")
//			.antMatchers("/login/**")
//			.anonymous()
//			.anyRequest()
//			.authenticated()
//			.and().httpBasic()
//			.and()
//			.sessionManagement()
//			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//			.and()
//			.logout(logoutCustomizer -> logoutCustomizer
//				//注销登录请求url
//				.logoutUrl("/logout").permitAll()
//				//清除身份认证信息
//				.clearAuthentication(true)
//				//使 Session失效
//				.invalidateHttpSession(true).deleteCookies("token"));
//
//		return httpSecurity.build();
//	}
//
//	@Bean
//	public WebSecurityCustomizer webSecurityCustomizer() {
//		return (web) -> web.ignoring().antMatchers("/images/**", "/css/**", "/js/**", "/webjars/**", "/lib/**", "/favicon.ico");
//	}
//	
//	@Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//	
//}
