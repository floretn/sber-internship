package org.sberinsur.tm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

import static org.sberinsur.tm.beans.constants.Markers.BUSINESS_MARKER;

/**
 * Точка входа в приложение.
 * @author Ненароков П.Ю.
 */
@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
public class TMDaoApplication {

	private static final Logger log = LoggerFactory.getLogger(TMDaoApplication.class);

	public static void main(String[] args) {
		log.info(BUSINESS_MARKER, "Application Start!");
		SpringApplication.run(TMDaoApplication.class, args);
	}
}
