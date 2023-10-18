package org.sberinsur.tm.ui.main;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.sberinsur.tm.ui.components.navbar.Navbar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.sberinsur.tm.beans.constants.Markers.BUSINESS_MARKER;
import static org.sberinsur.tm.util.security.SecurityUtils.getLoggedUserName;

/**
 * Относится к web слою.
 * Изначально - просто страница для тестирования, доступная всем пользателям, попавшим на сайт.
 * В дальнейшем, при необходимости, можно будет оформить, например, как главную страницу сайта.
 * @author Софронов И.Е.
 */
@Route(value = "main")
@PageTitle("Main Page")
public class MainView extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(MainView.class);

    /**
     * Здесь размещаются элементы пользовательского интерфейса.
     */
    public MainView() {
        log.info(BUSINESS_MARKER, "Пользователь " + getLoggedUserName() + " просматривает главную страницу");
        add(new Navbar());
        add(new Label("Hello from Main page!"));
    }
}
