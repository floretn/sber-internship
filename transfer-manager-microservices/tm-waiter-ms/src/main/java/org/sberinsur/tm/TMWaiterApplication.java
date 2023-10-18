package org.sberinsur.tm;

import static org.sberinsur.tm.beans.constants.Markers.BUSINESS_MARKER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

/**
 * Точка входа в приложение.
 * @author Ненароков П.Ю.
 */
@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
public class TMWaiterApplication {

	private static final Logger log = LoggerFactory.getLogger(TMWaiterApplication.class);

	public static void main(String[] args) {
		log.info(BUSINESS_MARKER, "Application Start!");
		SpringApplication.run(TMWaiterApplication.class, args);
	}
}
