package org.sberinsur.tm.util.web;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.sberinsur.tm.beans.exceptions.TMRuntimeException;
import org.slf4j.LoggerFactory;

import static org.sberinsur.tm.beans.constants.Markers.BUSINESS_MARKER;
import static org.sberinsur.tm.util.security.SecurityUtils.getLoggedUserName;

/**
 * Относится к web слою.
 * Здесь подвязываем полезные утилиты для части пользовательского интерфейса.
 * @author Софронов И.Е.
 */
public class WebUtils {
    /**
     * Метод создания массива ошибок путём (например для валидации) их подавления к одной.
     * Используем там, где необходимо разом проверить множество данных пользователя и разом вывести все ошибки.
     * @param exception ошибка, к которой цепляются все остальные ошибки.
     * @param message сообщение об ошибке.
     * @return TMRuntimeException если переменная exception была null - то новое исключение с сообщением message
     *                             если переменная exception была не null - то к ней подавляется новое исключени
     */
    public static TMRuntimeException addException(TMRuntimeException exception, String message) {
        if (exception != null) {
            exception.addSuppressed(new TMRuntimeException(message));
        } else {
            exception = new TMRuntimeException(message);
        }
        return exception;
    }

    /**
     * В диалогах на страницах в конце есть две кнопки: обновить данные и скрыть диалог.
     * Чтобы избежать дублирования кода, была создана данная утилита. По окончанию формаирования диалога он открывается на экране.
     * @param dialog диалог, к которому добавлются конечные кнопки.
     * @param verticalLayout компонент размещения двух кнопок.
     * @param upsertPathButton кнопка обновления данных.
     */
    public static void createEndOfDialog(Dialog dialog, VerticalLayout verticalLayout, Button upsertPathButton, Class<?> callingClass) {
        Div div = new Div();
        if (upsertPathButton != null) {
            div.add(upsertPathButton);
        }
        div.add(new Button("Cancel", new Icon(VaadinIcon.BAN), event -> {
            dialog.close();
            LoggerFactory.getLogger(callingClass).info(BUSINESS_MARKER, "Пользователь " + getLoggedUserName() +
                    " закрыл диалог изменения/добавления/просмотра свойства");
        }));

        verticalLayout.add(div);
        dialog.add(verticalLayout);
        dialog.open();
    }
}
