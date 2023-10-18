package org.sberinsur.tm.config.spring.beans;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

/**
 * Здесь подвязываем спринговые бины.
 * @author Ненароков П.Ю.
 * @author Софронов И.Е.
 */
@Configuration
public class BeanConfig {

    /**
     * Получаем бин для общения с другоими микрорсервисами.
     * @return RestTemplate объект общения с другими микросервисами.
     */
    @LoadBalanced
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * Метод создаёт объект для шифрования паролей.
     * @return PasswordEncoder кодер для паролей, при их помещении в БД.
     *                         Так же нужен при проверке пароля, введённого пользователем,
     *                         так как пароль нельзя расшифровать.
     */
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(16);
    }
}
