package com.example.execute;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@ComponentScan(basePackages= {"com.example.configuration","com.example.controller"})
@EntityScan({"com.example.model"}) //Difference with component scan is that it specifically scans the @Entity class
@EnableJpaRepositories("com.example.repository") //Difference with component scan is that it specifically scans the configures the repository class whereas component scan just locates and autowires
@EnableRetry //allowing methods annotated with @Retryable to be retried automatically upon failure
@EnableScheduling // allowing the execution of scheduled tasks using annotations like @Scheduled
@EnableJpaAuditing //automatically sets the createdDate and lastModifiedDate fields based on the current system time.
public class Executor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Executor.class);

	public static void main(String[] args) {
		
		SpringApplication.run(Executor.class, args);
		LOGGER.info("application started successfully");
		
	}
}
