package org.sberinsur.tm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class TMServerConfigApplication {
    public static void main(String[] args) {
        SpringApplication.run(TMServerConfigApplication.class, args);
    }
}
