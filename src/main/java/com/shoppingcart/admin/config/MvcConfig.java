package com.shoppingcart.admin.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer{
	//phải khai báo addResourceHandlers thì mới truy cập đến folder user-photos được
	//khai báo nhiều folder cùng 1 lúc
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		exposeDirectory("user-photos", registry);
		exposeDirectory("categories-images", registry);
		exposeDirectory("brand-logos", registry);
	}
	
	
	
	private void exposeDirectory(String pathPattern, ResourceHandlerRegistry registry) {
		Path path = Paths.get(pathPattern);
		
		String absolutePath = path.toFile().getAbsolutePath();
		
		String logicalPath = pathPattern + "/**";//user-photos/**
		
		registry.addResourceHandler(logicalPath).addResourceLocations("file:/"+absolutePath+"/");
		
	}
}
