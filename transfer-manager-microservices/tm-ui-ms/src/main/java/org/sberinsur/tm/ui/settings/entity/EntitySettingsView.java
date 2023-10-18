package org.sberinsur.tm.ui.settings.entity;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.sberinsur.tm.beans.exceptions.TMRuntimeException;
import org.sberinsur.tm.dto.entity.test.CommonSettingsEntity;
import org.sberinsur.tm.dto.entity.test.EntityEntity;
import org.sberinsur.tm.services.web.settings.common.CommonSettingsService;
import org.sberinsur.tm.services.web.settings.entity.EntitySettingsService;
import org.sberinsur.tm.beans.enums.Separator;
import org.sberinsur.tm.ui.components.navbar.Navbar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.stream.Collectors;

import static org.sberinsur.tm.beans.constants.Markers.BUSINESS_MARKER;
import static org.sberinsur.tm.beans.constants.EntityConstants.*;
import static org.sberinsur.tm.beans.constants.URLConstants.PREFIX_FOR_COMMON_SETTINGS_FOR_URL;
import static org.sberinsur.tm.beans.constants.WebConstants.DURATION_FOR_NOTIFICATION;
import static org.sberinsur.tm.util.security.SecurityUtils.getLoggedUserName;
import static org.sberinsur.tm.util.web.WebUtils.createEndOfDialog;

/**
 * Относится к web слою.
 * UI страничка настройки новых масок пользователем.
 * Доступна только авторизованным пользователям.
 * @author Софронов И.Е.
 */
@Route("settings/entity")
@PageTitle("Entity Settings")
public class EntitySettingsView extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(EntitySettingsView.class);

    /**Переменная сущности, в которую вносятся изменения пользователя.*/
    private EntityEntity workEntity = new EntityEntity();
    /**Сервис работы с сущностями. Инжектится через переменную конструктора.*/
    private final EntitySettingsService entityService;
    /**Сервис работы с общими настройками системы. Инжектится через переменную конструктора.*/
    private final CommonSettingsService commonSettingsService;

    /**Таблица со всеми сущностями*/
    private final Grid<EntityEntity> filePropsGrid = new Grid<>();

    /**
     * Здесь размещаются элементы пользовательского интерфейса.
     */
    public EntitySettingsView(@Autowired EntitySettingsService maskService, @Autowired CommonSettingsService commonSettingsService) {
        log.info(BUSINESS_MARKER, "Пользователь " + getLoggedUserName() + " просматривает страницу настроек сущностей");
        this.entityService = maskService;
        this.commonSettingsService = commonSettingsService;

        filePropsGrid.addColumn(EntityEntity::getId).setHeader("ID");
        filePropsGrid.addColumn(EntityEntity::getEntityName).setHeader("Entity Name");
        filePropsGrid.addColumn(EntityEntity::getMask).setHeader("Mask");
        filePropsGrid.addColumn(EntityEntity::getStatus).setHeader("Status");
        filePropsGrid.addColumn(EntityEntity::getValidDateFrom).setHeader("Date After");
        filePropsGrid.addColumn(EntityEntity::getValidDateBefore).setHeader("Date Before");
        filePropsGrid.addColumn(EntityEntity::getFileDateFormat).setHeader("Date Format");
        filePropsGrid.addColumn(EntityEntity::getSeparator).setHeader("Separator");
        filePropsGrid.addColumn(EntityEntity::getColumns).setHeader("Columns");
        filePropsGrid.addColumn(EntityEntity::getDescription).setHeader("Description");
        filePropsGrid.addColumn(EntityEntity::getDbConnectionKey).setHeader("DB Connection Key");
        filePropsGrid.addComponentColumn(entity -> {Div div = new Div();
            div.add(new Button("", new Icon(VaadinIcon.PENCIL), (event) -> {
                showUpsertDialog(entity, false);
                log.info(BUSINESS_MARKER, "Пользователь " + getLoggedUserName() + " открыл диалог для изменения сущности " +
                        entity);
            }));
            Icon icon;
            if (entity.getStatus() == WORKED_ENTITY_STATUS) {
                icon = new Icon(VaadinIcon.STOP);
            } else {
                icon = new Icon(VaadinIcon.REFRESH);
            }
            div.add(new Button("", icon, (event) -> {
                showRefreshStatusDialog(entity, icon);
                log.info(BUSINESS_MARKER, "Пользователь " + getLoggedUserName() + " собирается изменить статус сущность " +
                        entity + ".");
            }));
            return div;
        }).setHeader("Actions");
        filePropsGrid.setItems(maskService.showAll());

        add(new Navbar());
        add(new Button("Create new Entity", new Icon(VaadinIcon.PLUS), event -> {
            showUpsertDialog(null, true);
            //Обновление таблицы после изменения строки.
            filePropsGrid.setItems(maskService.showAll());
            log.info(BUSINESS_MARKER, "Пользователь " + getLoggedUserName() + " открыл диалог для добавления новой" +
                    " сущности");
        }));
        add(filePropsGrid);
    }

    /**
     * Метод формирования и вывода диалога на экран пользователя с определённой сущностью маски, которую нужно изменить/добавить.
     * @param entity сущность маски, которую выбрал пользователь для изменения.
     *                  Если пользователь добавляет новую сущность, то это объект с пустыми полями.
     * @param newMaskFlag флаг, показывающий добавляется новая сущность маски в систему (true),
     *                 или обновляется уже существующая сущность маски (false).
     */
    private void showUpsertDialog(EntityEntity entity, boolean newMaskFlag) {
        Dialog dialog = new Dialog();
        VerticalLayout verticalLayout = new VerticalLayout();
        Button upsertMaskButton = new Button(new Icon(VaadinIcon.CHECK));
        upsertMaskButton.addClickListener((event -> {
            log.info(BUSINESS_MARKER, "Пользователь " + getLoggedUserName() + " изменяет сущность " + entity + " на " + workEntity);
            upsertMask(newMaskFlag);
            filePropsGrid.setItems(entityService.showAll());
        }));

        if (newMaskFlag) {
            workEntity = new EntityEntity(0, "", "", 'y', LocalDate.now(), LocalDate.now(), "", "", null, "", "", null);
            verticalLayout.add(new H3("Create new Entity"));
            upsertMaskButton.setText("Add new Entity");
        } else {
            workEntity = new EntityEntity(entity);
            verticalLayout.add(new H3("Update Entity"));
            upsertMaskButton.setText("Update Entity");
        }

        TextField newNameMaskTextField = new TextField("Create new entity name", workEntity.getEntityName(), "");
        newNameMaskTextField.addValueChangeListener(event -> workEntity.setEntityName(event.getValue()));
        verticalLayout.add(newNameMaskTextField);

        TextField newMaskTextField = new TextField("Create new mask", workEntity.getMask(), "");
        newMaskTextField.addValueChangeListener(event -> workEntity.setMask(event.getValue()));
        verticalLayout.add(newMaskTextField);

        DatePicker valueDateAfterPicker = new DatePicker("Create new date after", workEntity.getValidDateFrom());
        valueDateAfterPicker.addValueChangeListener(event ->
                workEntity.setValidDateFrom(event.getValue()));
        verticalLayout.add(valueDateAfterPicker);

        DatePicker valueDateBeforePicker = new DatePicker("Create new date before", workEntity.getValidDateBefore());
        valueDateBeforePicker.addValueChangeListener(event ->
                workEntity.setValidDateBefore(event.getValue()));
        verticalLayout.add(valueDateBeforePicker);

        TextField newInlineDateFormatTextField = new TextField("Create new inline date format",
                workEntity.getInnerDateFormat(), "");
        newInlineDateFormatTextField.addValueChangeListener(event -> workEntity.setInnerDateFormat(event.getValue()));
        verticalLayout.add(newInlineDateFormatTextField);

        TextField newDateFormatTextField = new TextField("Create new date format",
                workEntity.getFileDateFormat(), "");
        newDateFormatTextField.addValueChangeListener(event -> workEntity.setFileDateFormat(event.getValue()));
        verticalLayout.add(newDateFormatTextField);

        MenuBar separatorMenuBar = new MenuBar();
        Div separatorDiv = new Div(new Label("Choose new separator: "), separatorMenuBar);
        separatorDiv.getStyle().set("display", "inline-block");
        MenuItem separatorItem;
        if (newMaskFlag) {
            separatorItem = separatorMenuBar.addItem("Click here!");
        } else {
            separatorItem = separatorMenuBar.addItem(workEntity.getSeparator().name());
        }
        for (Separator separator : Separator.values()) {
            separatorItem.getSubMenu().addItem(separator.name(), e -> setSeparator(separator, separatorItem));
        }
        verticalLayout.add(separatorDiv);

        TextField newColumnsMaskTextField = new TextField("Create new columns", workEntity.getColumns(), "");
        newColumnsMaskTextField.addValueChangeListener(event -> workEntity.setColumns(event.getValue()));
        verticalLayout.add(newColumnsMaskTextField);

        TextField descriptionField = new TextField("Create new description",
                workEntity.getDescription(), "");
        descriptionField.addValueChangeListener(event -> workEntity.setDescription(event.getValue()));
        verticalLayout.add(descriptionField);

        MenuBar urlMenuBar = new MenuBar();
        Div urlDiv = new Div(new Label("Choose new DB Connection Key: "), urlMenuBar);
        urlDiv.getStyle().set("display", "inline-block");
        MenuItem urlItem;
        if (newMaskFlag) {
            urlItem = urlMenuBar.addItem("Click here!");
        } else {
            urlItem = urlMenuBar.addItem(workEntity.getDbConnectionKey());
        }
        for (CommonSettingsEntity setting : commonSettingsService.showAll().parallelStream().filter(
                s -> s.getKey().endsWith(PREFIX_FOR_COMMON_SETTINGS_FOR_URL)).collect(Collectors.toList())) {
            urlItem.getSubMenu().addItem(setting.getKey(), e -> setDBConnectionKey(setting.getKey(), urlItem));
        }
        verticalLayout.add(urlDiv);

        createEndOfDialog(dialog, verticalLayout, upsertMaskButton, this.getClass());
    }

    /**
     * Метод внесения изменений сепаратора в новую/изменяемую сущность маски.
     * @param separator новый сепаратор.
     * @param separatorItem элемент меню, который нужно переименовать в соответствии с выбранным сепаратором.
     */
    private void setSeparator(Separator separator, MenuItem separatorItem) {
        workEntity.setSeparator(separator);
        separatorItem.setText(separator.name());
    }

    /**
     * Метод внесения изменений ключа для соединения с БД в новую/изменяемую сущность маски.
     * @param key новый ключ.
     * @param keyItem элемент меню, который нужно переименовать в соответствии с выбранным ключом.
     */
    private void setDBConnectionKey(String key, MenuItem keyItem) {
        workEntity.setDbConnectionKey(key);
        keyItem.setText(key);
    }

    /**
     * Метод формирования и вывода диалога на экран пользователя с определённой сущностью маски, у которой нужно изменить статус.
     * @param entity сущность маски, которую выбрал пользователь для изменения.
     * @param icon иконка для кнопки (в зависимости от действия либо стоп, либо обновление).
     */
    private void showRefreshStatusDialog(EntityEntity entity, Icon icon) {
        Dialog dialog = new Dialog();
        VerticalLayout verticalLayout = new VerticalLayout();
        Label checkLabel;
        if (entity.getStatus() == WORKED_ENTITY_STATUS) {
            checkLabel = new Label("Вы уверены, что собираетесь ОСТАНОВИТЬ сущность с именем " +
                    entity.getEntityName() + "?");
        } else {
            checkLabel = new Label("Вы уверены, что собираетесь ЗАПУСТИТЬ сущность с именем " +
                    entity.getEntityName() + "?");
        }
        dialog.add(checkLabel);

        Button refreshMaskButton = new Button("Refresh", icon, (event) -> {
            entityService.refreshEntityStatus(entity);
            filePropsGrid.setItems(entityService.showAll());
            dialog.close();
            Notification.show("Статус сущности успешно обновлён!", DURATION_FOR_NOTIFICATION, Notification.Position.TOP_END);
            log.info(BUSINESS_MARKER, "Пользователь " + getLoggedUserName() + " обновил статус сущности " +
                    entity);
        });

        createEndOfDialog(dialog, verticalLayout, refreshMaskButton, this.getClass());
    }

    /**
     * Метод добавления/изменения сущности маски в системе.
     * @param newMaskFlag флаг, показывающий создаётся ли новая сущность маски (true), или обновляется уже существующая.
     */
    private void upsertMask(boolean newMaskFlag) {
        try {
            entityService.upsertMask(workEntity, newMaskFlag);
            if (newMaskFlag) {
                Notification.show("Маска успешно добавлена!", DURATION_FOR_NOTIFICATION, Notification.Position.TOP_END);
                log.info(BUSINESS_MARKER, "Пользователь " + getLoggedUserName() + " успешно добавил новую сущность маски " +
                        workEntity);
            } else {
                Notification.show("Маска успешно обновлена!", DURATION_FOR_NOTIFICATION, Notification.Position.TOP_END);
                log.info(BUSINESS_MARKER, "Пользователь " + getLoggedUserName() + " успешно обновил сущность маски на " +
                        workEntity);
            }
        } catch (TMRuntimeException e) {
            showErrorNotification(e.getMessage());
            for (Throwable ex : e.getSuppressed()) {
                showErrorNotification(ex.getMessage());
            }
        }
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