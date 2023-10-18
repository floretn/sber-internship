package org.sberinsur.tm;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import static org.sberinsur.tm.config.logback.filters.markers.Markers.BUSINESS_MARKER;

@SpringBootApplication
@EnableEurekaClient
public class TMCleanerApplication {


    private static final Logger log = LoggerFactory.getLogger(TMCleanerApplication.class);

    public static void main(String[] args) {
        log.info(BUSINESS_MARKER, "Application Start!");
        SpringApplication.run(TMCleanerApplication.class, args);
    }
}
