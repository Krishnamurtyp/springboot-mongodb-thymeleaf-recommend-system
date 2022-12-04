package com.bigdata.mongodb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
//@EnableAutoConfiguration(exclude = {MongoAutoConfiguration.class})
@EnableScheduling
public class MongodbApplication {

	public static void main(String[] args) {
		SpringApplication.run(MongodbApplication.class, args);
	}

}
