package org.sberinsur.tm.ui.auth;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.shared.Registration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.sberinsur.tm.beans.constants.Markers.BUSINESS_MARKER;
import static org.sberinsur.tm.util.security.SecurityUtils.getLoggedUserName;
import static org.sberinsur.tm.util.security.SecurityUtils.isUserLoggedIn;

/**
 * Относится к web слою.
 * Страница авторизации.
 * @author Софронов И.Е.
 */
@Route(value = "auth/login")
@PageTitle("Login")
public class LoginView extends LoginOverlay implements BeforeEnterObserver {

    private static final Logger log = LoggerFactory.getLogger(LoginView.class);

    /**
     * Переменная с именем атрибута логина пользователя для его добавления в сессию.
     * Если пользователь неавторизован и не жмякнул кнопку "Войти", атрибут будет null.
     */
    private static final String ATTRIBUTE_USERNAME = "username";
    /**http-сессия пользователя, обёрнутая в сессию Vaadin*/
    private final VaadinSession session = VaadinSession.getCurrent();

    /**
     * Здесь размещаются элементы пользовательского интерфейса.
     */
    public LoginView() {
        log.info(BUSINESS_MARKER, "Пользователь " + getLoggedUserName() + " зашёл на страницу авторизации");
        setAction("auth/login");
        setOpened(true);
        setTitle("File mover");
        setDescription("Let's go to rape files!");
        setForgotPasswordButtonVisible(false);
    }

    /**
     * Метод проверки наличия переменных error или logout в URI запроса. Нужно для вывода соответствующих сообщений пользователю.
     * @param event событие перехода на страницу авторизации.
     */
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String username = (String) session.getAttribute(ATTRIBUTE_USERNAME);
        if (event.getLocation().getQueryParameters().getParameters().containsKey("error") &&
                session.getAttribute(ATTRIBUTE_USERNAME) != null) {
            log.info(BUSINESS_MARKER, "Ошибка авторизации пользователя под логином " + username);
            setError(true);
            session.setAttribute(ATTRIBUTE_USERNAME, null);
        }
        if (event.getLocation().getQueryParameters().getParameters().containsKey("logout") &&
                !isUserLoggedIn()) {
            Notification.show("Вы вышли из аккаунта").setPosition(Notification.Position.TOP_END);
        }
    }

    /**
     * Переопределённый метод для логирования авторизации.
     * @param listener "слушатель" на форму авторизации, сообщающий о нажатии кнопки "Войти".
     */
    @Override
    public Registration addLoginListener(ComponentEventListener<LoginEvent> listener) {
        return super.addLoginListener(loginEvent -> {
            String tryUsername = loginEvent.getUsername();
            session.setAttribute(ATTRIBUTE_USERNAME, tryUsername);
            log.info(BUSINESS_MARKER, "Пользователь авторизуется под логином " + tryUsername);
            listener.onComponentEvent(loginEvent);
        });
    }
}
