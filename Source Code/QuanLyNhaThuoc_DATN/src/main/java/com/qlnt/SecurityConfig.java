package com.qlnt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.qlnt.service.AccountService;
import com.qlnt.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	@Autowired
	private AccountService aService;
	@Autowired 
	private UserService uService;
	
	@Bean
	public BCryptPasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(uService);
	}
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests()
		.antMatchers("/cart/order/**").authenticated()
		.antMatchers("/account/**").authenticated()
		.antMatchers("/wishList/**").authenticated()
		.antMatchers("/api/**").hasRole("admin")
		.antMatchers("/admin/**").hasRole("admin")
		.anyRequest().permitAll();
		
		http.formLogin()
			.loginPage("/login")
			.loginProcessingUrl("/process-login")
			.defaultSuccessUrl("/login/success",false)
			.failureUrl("/login/error");
		
		http.rememberMe()
			.tokenValiditySeconds(86400);
		
		http.exceptionHandling()	
			.accessDeniedPage("/login/denied");
		
		http.logout()
			.logoutUrl("/logout")
			.logoutSuccessUrl("/logout/success");
	}
}
