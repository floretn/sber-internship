package org.sberinsur.tm.ui.settings.common;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.sberinsur.tm.beans.exceptions.TMRuntimeException;
import org.sberinsur.tm.dao.entity.test.CommonSettingsEntity;
import org.sberinsur.tm.services.web.settings.common.CommonSettingsService;
import org.sberinsur.tm.ui.components.navbar.Navbar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

import static org.sberinsur.tm.beans.constants.Markers.BUSINESS_MARKER;
import static org.sberinsur.tm.beans.constants.URLConstants.PREFIX_FOR_COMMON_SETTINGS_FOR_URL;
import static org.sberinsur.tm.beans.constants.WebConstants.DURATION_FOR_NOTIFICATION;
import static org.sberinsur.tm.util.security.SecurityUtils.getLoggedUserName;
import static org.sberinsur.tm.util.web.WebUtils.createEndOfDialog;

/**
 * Относится к web слою.
 * UI страничка для настройки новой дирректори получения файлов.
 * Доступна только авторизованным пользователям.
 * @author Софронов И.Е.
 */
@Route("settings/common")
@PageTitle("Common Settings")
public class CommonSettingsView extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(CommonSettingsView.class);

    /**Сервис внесения изменений в настройки системы. Инжектится через переменную конструктора.*/
    private final CommonSettingsService commonSettingsService;

    /**Настройки на странице пользователя, которые он изменил*/
    private final Set<CommonSettingsEntity> changedSettings = new HashSet<>();
    /***/
    private final Grid<CommonSettingsEntity> setupValuesGrid;

    /**
     * Здесь размещаются элементы пользовательского интерфейса.
     */
    public CommonSettingsView(@Autowired CommonSettingsService commonSettingsService) {
        log.info(BUSINESS_MARKER, "Пользователь " + getLoggedUserName() + " просматривает страницу с общими настройками");
        this.commonSettingsService = commonSettingsService;

        setupValuesGrid = new Grid<>();
        setupValuesGrid.addColumn(CommonSettingsEntity::getId).setHeader("ID");
        setupValuesGrid.addColumn(CommonSettingsEntity::getKey).setHeader("Key");
        setupValuesGrid.addComponentColumn(setupValue -> {
            TextField newContentTextField = new TextField("", setupValue.getValue(), "");
            newContentTextField.addValueChangeListener(event -> {
                log.info(BUSINESS_MARKER, "Пользователь " + getLoggedUserName() + " меняет значение настройки " + setupValue.getKey() +
                        " с " + event.getOldValue() + " на " + event.getValue());
                changedSettings.add(setupValue);
                setupValue.setValue(event.getValue());
            });
            return newContentTextField;
        }).setHeader("Value");
        setupValuesGrid.addComponentColumn(setupValue -> {
            Button deleteButton = new Button("", new Icon(VaadinIcon.TRASH), (event) -> {
                try {
                    log.info(BUSINESS_MARKER, "Пользователь " + getLoggedUserName() + " удаляет настройку " + setupValue);
                    commonSettingsService.deleteDBConnectionURL(setupValue);
                    Notification.show("Настройка успешно удалена!", DURATION_FOR_NOTIFICATION, Notification.Position.TOP_END);
                    setupValuesGrid.setItems(commonSettingsService.showAll());
                    log.info(BUSINESS_MARKER, "Пользователь " + getLoggedUserName() + " успешно удалил настройку " + setupValue);
                } catch (TMRuntimeException ex) {
                    showErrorNotification(ex.getMessage());
                }
            });
            if (!setupValue.getKey().endsWith(PREFIX_FOR_COMMON_SETTINGS_FOR_URL)) {
                deleteButton.setVisible(false);
            }
            return deleteButton;
        }).setHeader("Actions");
        setupValuesGrid.setItems(commonSettingsService.showAll());

        add(new Navbar());
        Div buttonsDiv = new Div();
        buttonsDiv.getStyle().set("display", "inline-block");
        buttonsDiv.add(new Button("Accept changes", new Icon(VaadinIcon.CHECK), (event) -> {
            log.info(BUSINESS_MARKER, "Пользователь " + getLoggedUserName() + " применяет новые настройки: " + changedSettings);
            updateSetting();
        }));
        buttonsDiv.add(new Button("Add DB Connection URL", new Icon(VaadinIcon.PLUS), (event) -> showInsertDialog()));
        add(buttonsDiv);
        add(setupValuesGrid);
    }

    /**
     * Метод добавления изменения настройки в системе.
     */
    private void updateSetting() {
        try {
            if (!changedSettings.isEmpty()) {
                commonSettingsService.updateSettings(changedSettings);
                changedSettings.clear();
            }
            Notification.show("Настройки успешно обновлены!", DURATION_FOR_NOTIFICATION, Notification.Position.TOP_END);
            log.info(BUSINESS_MARKER, "Пользователь " + getLoggedUserName() + " изменил некоторые настройки на: " +
                    changedSettings);
        } catch (TMRuntimeException e) {
            String message = e.getMessage();
            if (e.getCause() != null) {
                /*
                 * Эта ситуация возникает в том случае, если настройки удалось загрузить в базу (то есть они прошли валидацию)
                 * но не были загружены в системую
                 */
                message = message + " " + e.getCause().getMessage();
            }
            showErrorNotification(message);
            for (Throwable ex : e.getSuppressed()) {
                showErrorNotification(ex.getMessage());
            }
        }
    }

    /**
     * Метод формирования и вывода диалога на экран пользователя для ввода и сохранения данных о новой настройке.
     */
    private void showInsertDialog() {
        Dialog dialog = new Dialog();
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(new H3("Create new URL"));
        Button updateSettingButton = new Button("Add new URL", new Icon(VaadinIcon.CHECK));
        CommonSettingsEntity newSetting = new CommonSettingsEntity();
        updateSettingButton.addClickListener((event -> {
            log.info(BUSINESS_MARKER, "Пользователь " + getLoggedUserName() + " добавляет новую настройку " + newSetting);
            if (newSetting.getKey() != null && !newSetting.getKey().endsWith(PREFIX_FOR_COMMON_SETTINGS_FOR_URL)) {
                Notification.show("Свойство URL должно заканчиваться на '_url'",
                                DURATION_FOR_NOTIFICATION, Notification.Position.TOP_END).
                                addThemeVariants(NotificationVariant.LUMO_ERROR);
                log.info(BUSINESS_MARKER, "Пользователь " + getLoggedUserName() + " ввёл ключ настройки без префикса " +
                        PREFIX_FOR_COMMON_SETTINGS_FOR_URL);
                return;
            }
            try {
                commonSettingsService.insertDBConnectionURL(newSetting);
                setupValuesGrid.setItems(commonSettingsService.showAll());
                Notification.show("URL соединения с БД успешно добавлен!", DURATION_FOR_NOTIFICATION,
                        Notification.Position.TOP_END);
                log.info(BUSINESS_MARKER, "Пользователь " + getLoggedUserName() + " успешно добавил настройку " + newSetting);
                dialog.close();
            } catch (TMRuntimeException e) {
                showErrorNotification(e.getMessage());
                for (Throwable ex : e.getSuppressed()) {
                    showErrorNotification(ex.getMessage());
                }
            }
        }));

        TextField keyTextField = new TextField("Create new key", "", "");
        keyTextField.addValueChangeListener(event -> newSetting.setKey(event.getValue()));
        verticalLayout.add(keyTextField);

        TextField valueField = new TextField("Create new value (DB Connection URL)", "", "");
        valueField.addValueChangeListener(event -> newSetting.setValue(event.getValue()));
        verticalLayout.add(valueField);

        createEndOfDialog(dialog, verticalLayout, updateSettingButton, this.getClass());
    }

    /**
     * Метод вывода оповещения об ошибке пользователю на экран.
     * @param message сообщение об ошибке.
     */
    private void showErrorNotification(String message) {
        Notification.show(message, DURATION_FOR_NOTIFICATION, Notification.Position.TOP_END).
                addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
}