package org.sberinsur.tm.util.security;

import com.vaadin.flow.server.ServletHelper.RequestType;
import com.vaadin.flow.shared.ApplicationConstants;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Stream;

/**
 * Сюда подвязываем полезные утилиты для работы со Spring Security.
 * @author Софронов И.Е.
 */
public final class SecurityUtils {

    private SecurityUtils() {
    }

    /**
     * Проверка на то, является ли запрос внутренним запросом фреймворка Vaadin.
     * @param request {@link HttpServletRequest} - проверяемый запрос
     * @return true если это внутренний запрос фреймфорка.
     */
    public static boolean isFrameworkInternalRequest(HttpServletRequest request) {
        final String parameterValue = request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER);
        return parameterValue != null
                && Stream.of(RequestType.values()).anyMatch(r -> r.getIdentifier().equals(parameterValue));
    }

    /**
     * Метод возвращает имя авторизованного пользователя.
     * @return String имя авторизованного пользователя.
     *                  Если пользователь не авторизован - "неавторизованный пользователь".
     */
    public static String getLoggedUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null &&
                !(authentication instanceof AnonymousAuthenticationToken) &&
                authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return "'неавторизованный пользователь'";
    }

    /**
     * Проверяет авторизован ли пользователь в системе.
     * @return true если пользователь авторизован.
     */
    public static boolean isUserLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null
                && !(authentication instanceof AnonymousAuthenticationToken)
                && authentication.isAuthenticated();
    }
}
