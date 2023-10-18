package org.sberinsur.tm.ui.components.navbar;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.dom.ElementFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.sberinsur.tm.util.security.SecurityUtils.isUserLoggedIn;
import static org.sberinsur.tm.beans.constants.Markers.BUSINESS_MARKER;
import static org.sberinsur.tm.util.security.SecurityUtils.getLoggedUserName;

/**
 * Относится к web слою.
 * Компонент пользовательского интерфейса "Навигационная панель" для быстрого перемещения по сайту.
 * @author Софронов И.Е.
 */
public class Navbar extends AppLayout {

    private static final Logger log = LoggerFactory.getLogger(Navbar.class);

    /**
     * Здесь размещаются элементы пользовательского интерфейса.
     */
    public Navbar() {
        Element logInOutLink;
        if (isUserLoggedIn()) {
            logInOutLink = ElementFactory.createAnchor("logout", "Logout...");
            logInOutLink.addEventListener("click", event ->
                    log.info(BUSINESS_MARKER, "Пользователь с логином " + getLoggedUserName() +
                    " вышел из системы"));
        } else {
            logInOutLink = ElementFactory.createAnchor("auth/login", "Login");
        }
        Tab logInOut = new Tab();
        logInOut.getElement().appendChild(logInOutLink);
        logInOut.getStyle().
                set("font-size", "var(--lumo-font-size-l)").
                set("right", "var(--lumo-space-l)").
                set("margin", "0").
                set("position", "absolute");

        addToNavbar(createTabs());
        addToNavbar(logInOut);
    }

    /**
     * Метод формирования кнопок в левой части навигационной панели (все основные кнопки для перемещения по сайту).
     * @return Tab[] массив кнопок.
     */
    private Tab[] createTabs() {
        Element[] links = new Element[4];
        links[0] = ElementFactory.createAnchor("main", "FileMover");
        links[0].getStyle().set("font-weight", "bold");
        links[1] = ElementFactory.createAnchor("settings/entity", "Entity");
        links[2] = ElementFactory.createAnchor("settings/common", "Common");
        links[3] = ElementFactory.createAnchor("dump", "Dump");

        Tab[] tabs = new Tab[4];
        for (int i = 0; i < links.length; i++) {
            tabs[i] = new Tab();
            tabs[i].getElement().appendChild(links[i]);
        }
        return tabs;
    }
}
