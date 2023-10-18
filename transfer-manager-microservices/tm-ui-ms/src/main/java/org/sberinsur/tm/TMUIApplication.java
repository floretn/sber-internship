package org.sberinsur.tm;

import static org.sberinsur.tm.beans.constants.Markers.BUSINESS_MARKER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Точка входа в приложение.
 * @author Ненароков П.Ю.
 */
@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
@EnableDiscoveryClient
public class TMUIApplication {

	/**
	 * До старта системы нужно добавить переменную среды:
	 * proxy=http://your_name:your_password@10.3.3.22:8080
	 */

	private static final Logger log = LoggerFactory.getLogger(TMUIApplication.class);

	public static void main(String[] args) {
		log.info(BUSINESS_MARKER, "Application Start!");
		SpringApplication.run(TMUIApplication.class, args);
	}
}
