package org.sberinsur.tm.beans.spring;

import org.sberinsur.tm.beans.constants.TMResources;
import org.sberinsur.tm.beans.rest.TMResourcesDTO;
import org.sberinsur.tm.cleaner.FileCleaner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;


/**
 * Класс конфигурации бинов
 * @author ioromanov
 */
@Configuration
public class BeanConfig {

    @Autowired
    FileCleaner fileCleaner;

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
                    fileCleaner.clean();
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
