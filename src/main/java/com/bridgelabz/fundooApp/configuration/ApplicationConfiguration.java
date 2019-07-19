package com.bridgelabz.fundooApp.configuration;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationConfiguration 
{
	@Bean
	public PasswordEncoder getPasswordEncoder() 
	{
		return new BCryptPasswordEncoder();
	}

	@Bean
	public ModelMapper getModelMapper() 
	{
		return new ModelMapper();
	}
	
	@Bean
	public RestHighLevelClient getrestRestHighLevelClient()
	{
		return new RestHighLevelClient(RestClient.builder(new HttpHost("localhost",9200,"http")));
	}
}
