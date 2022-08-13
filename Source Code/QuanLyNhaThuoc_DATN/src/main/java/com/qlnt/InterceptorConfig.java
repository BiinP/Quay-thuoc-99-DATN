package com.qlnt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.qlnt.interceptor.GlobalInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer{
	@Autowired 
	private GlobalInterceptor globalInterceptor;
//	@Autowired 

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(globalInterceptor)
			.addPathPatterns("/**")
			.excludePathPatterns("/admin/**","/assets/**","/api/**");
//		registry.addInterceptor(auth)
//			.addPathPatterns("/admin/**", "/order/**");
	}
}
