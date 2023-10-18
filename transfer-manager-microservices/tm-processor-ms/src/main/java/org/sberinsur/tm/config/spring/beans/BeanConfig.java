package org.sberinsur.tm.config.spring.beans;

import org.sberinsur.tm.beans.constants.TMResources;
import org.sberinsur.tm.beans.rest.TMResourcesDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

/**
 * Здесь подвязываем спринговые бины.
 * @author Ненароков П.Ю.
 * @author Софронов И.Е.
 */
@Configuration
@EnableScheduling
@ConditionalOnProperty(name= "scheduler.enabled", matchIfMissing = true)
public class BeanConfig {

    final static Logger log = LoggerFactory.getLogger(BeanConfig.class);

    @Bean
    public Thread stupidThread() {
        Thread stupidThread = new Thread(() -> {
            boolean check = false;
            TMResourcesDTO tmResourcesInner;
            while (!check) {
                try {
                    tmResourcesInner = restTemplate().getForObject("http://tm-dao-ms/tm_resources", TMResourcesDTO.class);
                    TMResources.setAllProperties(tmResourcesInner);
                    check = true;
                } catch (Exception ex) {
                    check = false;
                }
            }
        });
        stupidThread.start();
        return stupidThread;
    }


    /**
     * Получаем бин для общения с другоими микрорсервисами.
     * @return RestTemplate объект общения с другими микросервисами.
     */
    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
