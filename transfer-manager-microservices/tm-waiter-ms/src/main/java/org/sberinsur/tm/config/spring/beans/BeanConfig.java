package org.sberinsur.tm.config.spring.beans;

import org.sberinsur.tm.beans.constants.TMResources;
import org.sberinsur.tm.services.waiter.FileWaiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.devtools.filewatch.FileChangeListener;
import org.springframework.boot.devtools.filewatch.FileSystemWatcher;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PreDestroy;
import java.nio.file.Paths;
import java.time.Duration;

import org.springframework.web.client.RestTemplate;

import static org.sberinsur.tm.beans.constants.Markers.BUSINESS_MARKER;

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

    /**Слушатель директорий*/
    @Autowired
    private FileWaiter fileWaiter;

    @Bean
    public Thread stupidThread() {
        Thread stupidThread = new Thread(() -> {
            boolean check = false;
            FileSystemWatcher fileSystemWatcher;
            TMResources tmResources;
            fileWaiter.setRestTemplate(restTemplate());
            while (!check) {
                try {
                    tmResources = restTemplate().getForObject("http://tm-dao-ms/tm_resources", TMResources.class);
                    fileSystemWatcher = new FileSystemWatcher(false, Duration.ofMillis(tmResources.checkPeriod),
                            Duration.ofMillis(tmResources.checkDuration));
                    fileSystemWatcher.addSourceDirectory(Paths.get(tmResources.LoadingHere).toFile());
                    fileSystemWatcher.addListener(fileWaiter);
                    fileSystemWatcher.start();
                    check = true;
                } catch (Exception ex) {
                    check = false;
                }
            }
        });
        stupidThread.start();
        return stupidThread;
    }

//    /**
//     * Назначение просматриваемых в ожидании файлов директорий и кастомизированного менеджера приема файлов.
//     * Прием файлов запускается в виде демона.
//     * @return возвращает наблюдаещего за директориями.
//     * @author Ненароков П.Ю.
//     */
//    @Bean
//    public FileSystemWatcher fileSystemWatcher() {
//        FileSystemWatcher fileSystemWatcher = new FileSystemWatcher(true, Duration.ofMillis(10000),
//                Duration.ofMillis(1000));
//        fileSystemWatcher.addSourceDirectory(Paths.get(tmResourcesBean().LoadingHere).toFile());
//        fileSystemWatcher.addListener(fileWaiter);
//        fileSystemWatcher.start();
//        log.info(BUSINESS_MARKER,"Система готова к приёму файлов!");
//        return fileSystemWatcher;
//    }

//    /**
//     * Остановка демона при остановке приложения
//     * @author Ненароков П.Ю.
//     */
//    @PreDestroy
//    public void onDestroy() {
//        fileSystemWatcher().stop();
//    }

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
