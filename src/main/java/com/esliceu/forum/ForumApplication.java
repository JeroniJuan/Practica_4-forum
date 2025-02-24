package com.esliceu.forum;

import com.esliceu.forum.interceptors.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.*;

@SpringBootApplication
public class ForumApplication implements WebMvcConfigurer {

	@Autowired
	TokenInterceptor tokenInterceptor;

	public static void main(String[] args) {
		SpringApplication.run(ForumApplication.class, args);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(tokenInterceptor)
				.addPathPatterns("/profile");
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("http://localhost:8080")
				.allowedMethods("GET", "POST", "PUT", "DELETE")
				.allowedHeaders("Content-Type", "Authorization");
	}
}