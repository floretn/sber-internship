package org.sberinsur.tm.services.web.settings.entity;

import org.sberinsur.tm.beans.constants.TMResources;
import org.sberinsur.tm.beans.exceptions.TMRuntimeException;
import org.sberinsur.tm.dao.entity.test.EntityEntity;
import org.sberinsur.tm.dao.repository.test.ICommonSettingsRepository;
import org.sberinsur.tm.dao.repository.test.IEntityRepository;
import org.sberinsur.tm.services.director.ITMDirector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.sberinsur.tm.beans.constants.Markers.BUSINESS_MARKER;
import static org.sberinsur.tm.beans.constants.EntityConstants.*;
import static org.sberinsur.tm.util.security.SecurityUtils.getLoggedUserName;
import static org.sberinsur.tm.util.web.WebUtils.addException;

/**
 * Относится к web слою.
 * Сервис для настройки новой сущности маски в системе.
 * @author Софронов И.Е.
 */
@Service
public class EntitySettingsService {

    private static final Logger log = LoggerFactory.getLogger(EntitySettingsService.class);

    /**Константа, содержащая обязательный префикс у сущности маски*/
    private static final String CSV_PREFIX = ".csv";

    @Autowired
    private IEntityRepository entityRepository;
    private ICommonSettingsRepository commonSettingsRepository;
    @Autowired
    private TMResources tmResources;

    private final ITMDirector director;

    @Autowired
    public EntitySettingsService(ITMDirector director) {
        this.director = director;
    }

    /**
     * Метод возвращения всех сущностей масок из базы данных.
     * @return List список всех entity масок в БД.
     */
    public List<EntityEntity> showAll() {
        return entityRepository.findAll();
    }

    /**
     * Метод проверки и добавления/обновления новой сущности маски в системе.
     * @param entity информация о новой entity маски, полученная от пользователя.
     * @param newEntityFlag флаг, показывающий создаётся ли новая entity маски (true), или обновляется уже существующая.
     */
    public void upsertMask(EntityEntity entity, boolean newEntityFlag) {
        validateMask(entity, newEntityFlag);
        entityRepository.save(entity);
        refreshEntityInSystem();
    }

    /**
     * Метод остановки работы сущности маски.
     * @param entity entity маски, которую нужно удалить.
     */
    public void refreshEntityStatus(EntityEntity entity) {
        director.stopProcessing();
        if (entity.getStatus() == WORKED_ENTITY_STATUS) {
            entity.setStatus(STOPPED_ENTITY_STATUS);
            log.info(BUSINESS_MARKER, "Пользователь " + getLoggedUserName() + " успешно остановил работу сущности маски " + entity);
        } else {
            entity.setStatus(WORKED_ENTITY_STATUS);
            log.info(BUSINESS_MARKER, "Пользователь " + getLoggedUserName() + " успешно возобновил работу сущности маски " + entity);
        }
        entityRepository.save(entity);
        refreshEntityInSystem();
    }

    /**
     * Метод, синхронизированно обновляющий данные о рабочих сущностях в системе из БД.
     */
    private void refreshEntityInSystem() {
        synchronized (director) {
            director.stopProcessing();
            tmResources.setEtalonMasksEntities();
            director.startProcessing();
        }
    }

    /**
     * Метод проверки новой сущности маски на валидность.
     * @param entity информация о новой сущности маски, полученная от пользователя.
     * @throws TMRuntimeException если были найдены ошибки валидации.
     *                            Если ошибок несколько - то будет возвращена первая найденная ошибка,
     *                            к которой будут в массив подавлены все остальные
     */
    private void validateMask(EntityEntity entity, boolean newEntityFlag) {
        TMRuntimeException exception = null;
        if (entity.getEntityName() == null || entity.getEntityName().isEmpty()
                || entity.getMask() == null || entity.getMask().isEmpty()
                || entity.getColumns() == null || entity.getColumns().isEmpty()
                || entity.getSeparator() == null || entity.getDbConnectionKey() == null) {
            exception = addException(null, "Все поля должны быть заполнены!");
            log.info(BUSINESS_MARKER, "Пользователь " + getLoggedUserName() + " оставил пустыми поля свойств");
        }

        if (entity.getEntityName() != null && entity.getEntityName().length() > 255) {
            exception = addException(exception, "Превышена длинна имени маски");
            log.info(BUSINESS_MARKER, "Пользователь " + getLoggedUserName() + " превысил длину имени маски");
        }

        if (entity.getMask() != null && entity.getMask().length() > 255) {
            exception = addException(exception, "Превышена длина маски");
            log.info(BUSINESS_MARKER, "Пользователь " + getLoggedUserName() + " превысил длину маски");
        }

        if (entity.getMask() != null && !entity.getMask().endsWith(CSV_PREFIX)) {
            exception = addException(exception, "Префикс маски должен быть " + CSV_PREFIX);
            log.info(BUSINESS_MARKER, "Пользователь " + getLoggedUserName() + " ввёл некорректный префикс маски");
        }

        if (entity.getDescription() != null && entity.getDescription().length() > 255) {
            exception = addException(exception, "Превышена длина описания сущности!");
            log.info(BUSINESS_MARKER, "Пользователь " + getLoggedUserName() + " превысил длину описания сущности");
        }

        if (entity.getValidDateFrom().compareTo(entity.getValidDateBefore()) >= 0)   {
            exception = addException(exception, "Дата после должна быть строго меньше даты до!");
            log.info(BUSINESS_MARKER, "Пользователь " + getLoggedUserName() +
                    " ввёл некорректные данные даты до и даты после (дата до больше даты после");
        }

        try {
            Date testDate = new Date();
            new SimpleDateFormat(entity.getInnerDateFormat()).format(testDate);
            new SimpleDateFormat(entity.getFileDateFormat()).format(testDate);
        } catch (Exception e) {
            exception = addException(exception, "Недопустимый формат даты!");
            log.info(BUSINESS_MARKER, "Пользователь " + getLoggedUserName() + " ввёл некорктные форматы для дат");
        }

        if (entity.getColumns() != null && !validateColumns(entity.getColumns())) {
            exception = addException(exception, "Перечисление столбцов должно начинаться с #, имена столбцов могут содержать только " +
                    "английские прописные буквы и знак '_', разделяться '/' и не должны повторяться!");
            log.info(BUSINESS_MARKER, "Пользователь " + getLoggedUserName() + " ввёл столбцы в некорректном формате");
        }

        if (newEntityFlag && entity.getEntityName() != null && entityRepository.findByEntityName(entity.getEntityName()) != null) {
            exception = addException(exception, "Маска с таким именем уже есть в базе!");
            log.info(BUSINESS_MARKER, "Пользователь " + getLoggedUserName() + " ввёл имя маски, которое уже есть в базе");
        }

        if (newEntityFlag && entity.getMask() != null && entityRepository.findByMask(entity.getMask()) != null) {
            exception = addException(exception, "Маска с такой маской уже есть в базе!");
            log.info(BUSINESS_MARKER, "Пользователь " + getLoggedUserName() + " ввёл маску, которая уже есть в базе");
        }

        if (exception != null) {
            throw exception;
        }
    }

    /**
     * Метод проверки поля columns новой сущности маски на валидность.
     * @param columns информация о поле columns новой сущности маски, полученная от пользователя.
     * @throws TMRuntimeException если были найдены ошибки валидации.
     */
    private boolean validateColumns(String columns) {
        boolean check = columns.startsWith("#") && columns.length() > 1;

        String[] columnsArr = null;
        if (columns.length() > 1) {
            columnsArr = columns.substring(1).split("/");
        }

        if (columnsArr != null) {
            for (int i = 0; i < columnsArr.length; i++) {
                if (!check) {
                    break;
                }
                for (int j = 0; j < columnsArr.length; j++) {
                    if (i != j) {
                        if (columnsArr[i].equals(columnsArr[j])) {
                            check = false;
                            break;
                        }
                    }
                }

                if (!columnsArr[i].matches("[a-z0-9_]+")) {
                    check = false;
                }
            }
        }
        return check;
    }
}


