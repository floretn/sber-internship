package org.sberinsur.tm.config.spring.security;

import org.sberinsur.tm.services.web.auth.AuthService;
import org.sberinsur.tm.util.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Относится к web слою.
 * Конфигурация защиты приложения от неавторизованного доступа.
 * @author Софронов И.Е.
 */
@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**URL страница входа*/
    private static final String LOGIN_URL = "/auth/login";
    /**URL дефолтной страницы после авторизации*/
    private static final String LOGIN_SUCCESS_URL = "/main";
    /**
     * URL страниц, доступных всем пользователям, независимо от авторизации.
     * TODO: не забрыть убрать лишние страницы.
     */
    private static final String[] PERMIT_ALL_URL = new String[] {"/main", "/settings/common", "/settings/entity", "/dump"};

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthService authService;

    /**
     * Здесь настраиваем авторизацию (страница входа/выхода, разрешённые страницы по Ролям (если такое будет нужно),
     * разрешённые страницы не авторизованным пользователям (например стр. main) и тд.).
     * @param http объект создания фильтра для авторизованного доступа к страницам.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                    .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()
                    .antMatchers(PERMIT_ALL_URL).permitAll()
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage(LOGIN_URL)
                    .permitAll()
                    .failureUrl(LOGIN_URL + "?error")
                    .defaultSuccessUrl(LOGIN_SUCCESS_URL)
                .and()
                    .logout()
                    .permitAll();
    }

    /**
     * Доступ к статическим ресурсам Vaadin. Это нужно для его нормальной работы.
     * Если это убрать, то страница входа просто не отобразится, потому что Spring Security запретит доступ.
     * @param web объект создания web фильтра для авторизованного доступа к ресурсам.
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                // Vaadin Flow static resources
                "/VAADIN/**",
                // the standard favicon URI
                "/favicon.ico",
                // the robots exclusion standard
                "/robots.txt",
                // web application manifest
                "/manifest.webmanifest",
                "/sw.js",
                "/offline-page.html",
                // icons and images
                "/icons/**",
                "/images/**",
                // (development mode) static resources
                "/frontend/**",
                // (development mode) webjars
                "/webjars/**",
                // (development mode) H2 debugging console
                "/h2-console/**",
                // (production mode) static resources
                "/frontend-es5/**", "/frontend-es6/**");
    }

    /**
     * Метод проверки корректности введённых пользователем данных.
     * @param auth объект для проверки корректности введённых данных пользователем.
     *             Содержит сервис для загрузки данных о пользователе по логину и кодер для пароля.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(authService).passwordEncoder(passwordEncoder);
    }
}
