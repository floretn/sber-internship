package org.sberinsur.tm.services.web.settings.common;

import org.sberinsur.tm.beans.constants.TMResources;
import org.sberinsur.tm.beans.exceptions.TMRuntimeException;
import org.sberinsur.tm.dao.entity.test.CommonSettingsEntity;
import org.sberinsur.tm.dao.repository.test.ICommonSettingsRepository;
import org.sberinsur.tm.dao.repository.test.IEntityRepository;
import org.sberinsur.tm.services.director.ITMDirector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.sberinsur.tm.util.web.WebUtils.addException;
import static org.sberinsur.tm.beans.constants.Markers.BUSINESS_MARKER;
import static org.sberinsur.tm.util.security.SecurityUtils.getLoggedUserName;
import static org.sberinsur.tm.beans.constants.URLConstants.PREFIX_FOR_COMMON_SETTINGS_FOR_URL;

import static org.sberinsur.tm.beans.enums.NamesOfSetupValuesEnum.*;
import static org.sberinsur.tm.beans.constants.SystemPropertiesBorders.*;

/**
 * Относится к web слою.
 * Сервис для изменения общих настроек.
 * @author Софронов И.Е.
 */
@Service
public class CommonSettingsService {

    private static final Logger log = LoggerFactory.getLogger(CommonSettingsService.class);

    /**Переменная с сообщением об ошибке введения пользователем некорректного числа*/
    private static final String PARSE_ERROR_MESSAGE = "Некорректное значение для целого числа!";

    @Autowired
    private ICommonSettingsRepository commonSettingsRepository;
    @Autowired
    private IEntityRepository entityRepository;
    @Autowired
    private TMResources tmResources;

    private final ITMDirector director;

    @Autowired
    public CommonSettingsService(ITMDirector director) {
        this.director = director;
    }

    /**
     * Метод возвращения всех общих настроек из базы данных.
     * @return List список всех настроек в БД.
     */
    public List<CommonSettingsEntity> showAll() {
        return commonSettingsRepository.findAll();
    }

    /**
     * Метод удаления настройки из системы.
     * @throws TMRuntimeException в случае, когда свойство не может быть удалено (так как оно используется).
     */
    public void deleteDBConnectionURL(CommonSettingsEntity oldSetting) {
        if (!entityRepository.findByDbConnectionKey(oldSetting.getKey()).isEmpty()) {
            log.info(BUSINESS_MARKER, "Пользователь " + getLoggedUserName() +
                    " не смог удалить настройку так как она используется некоторыми сущностями.");
            throw new TMRuntimeException("Свойство не может быть удалено, так как оно до сих пор использутеся некоторыми сущностями!");
        }
        commonSettingsRepository.delete(oldSetting);
    }

    /**
     * Метод вставки в систему новой настройки, а именно - url соединения с БД.
     * @param newSetting новая настройка.
     * @throws TMRuntimeException если были найдены ошибки валидации.
     *                            Если ошибок несколько - то будет возвращена первая найденная ошибка,
     *                            к которой будут в массив подавлены все остальные.
     */
    public void insertDBConnectionURL(CommonSettingsEntity newSetting) {
        Set<CommonSettingsEntity> singleSet = Collections.singleton(newSetting);
        TMRuntimeException exception = null;
        if (commonSettingsRepository.findByKey(newSetting.getKey()) != null ||
                commonSettingsRepository.findByValue(newSetting.getValue()) != null) {
            exception = new TMRuntimeException("Свойство на может быть добавлено, так как в базе уже есть " +
                    "свойство с таким ключом или значением!");
            log.info(BUSINESS_MARKER, "");
        }
        validateSetting(singleSet, exception);
        commonSettingsRepository.save(newSetting);
    }

    /**
     * Метод проверки и обновления настройки в системе.
     * @param settings настройки, полученные от пользователяю.
     * @throws TMRuntimeException если были найдены ошибки валидации.
     *                            Если ошибок несколько - то будет возвращена первая найденная ошибка,
     *                            к которой будут в массив подавлены все остальные.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = TMRuntimeException.class)
    public void updateSettings(Set<CommonSettingsEntity> settings) {
        validateSetting(settings, null);
        commonSettingsRepository.saveAll(settings);
        synchronized (director) {
            director.stopProcessing();
            try {
                tmResources.setAllProperties();
            } catch (IOException | SQLException e) {
                log.error(BUSINESS_MARKER, "", e);
                throw new TMRuntimeException("Не удалось применить новые настройки!", e);
            }
            director.startProcessing();
        }
    }

    /**
     * Метод проверки изменённой настройки на валидность.
     * @param settings настройки, полученные от пользователя.
     * @param exception ошибка валидации, которая могла быть обнаружена до вызова метода.
     *                  Может быть null.
     * @throws TMRuntimeException если были найдены ошибки валидации.
     *                            Если ошибок несколько - то будет возвращена первая найденная ошибка,
     *                            к которой будут в массив подавлены все остальные.
     */
    private void validateSetting(Set<CommonSettingsEntity> settings, TMRuntimeException exception) {
        for (CommonSettingsEntity setting : settings) {
            if (setting.getKey() == null || setting.getKey().isEmpty()
                    || setting.getValue() == null || setting.getValue().isEmpty()) {
                exception = addException(exception, "Поля не должны быть пустыми! Ключ " + setting.getKey());
                log.info(BUSINESS_MARKER, "Пользователь " + getLoggedUserName() + " оставил пустыми поля свойств. " +
                        "Ключ " + setting.getKey());
                continue;
            }

            if (setting.getValue() != null && setting.getValue().length() > 255) {
                exception = addException(exception, "Превышена длина свойства. Ключ " + setting.getKey());
                log.info(BUSINESS_MARKER, "Пользователь " + getLoggedUserName() + " превысил допустимую длину свойства. " +
                        "Ключ " + setting.getKey());
                continue;
            }

            if (setting.getKey().equals(SUCCESS_DIRECTORY.name()) ||
                    setting.getKey().equals(ERROR_DIRECTORY.name()) ||
                    setting.getKey().equals(PROCESS_DIRECTORY.name()) ||
                    setting.getKey().equals(LOADING_HERE.name())) {

                CommonSettingsEntity setupValues = null;
                if (setting.getValue() != null) {
                    setupValues = commonSettingsRepository.findByValue(setting.getValue());
                }

                if (setupValues != null && !setupValues.getId().equals(setting.getId())) {
                    exception = addException(exception, "Такой путь уже есть в базе! Ключ " + setting.getKey());
                    log.info(BUSINESS_MARKER, "Пользователь " + getLoggedUserName() +
                            " ввёл дирректорию, которая уже используется в системе. Ключ " + setting.getKey());
                }
            } else if (setting.getKey().equals(CHECK_PERIOD.name())) {
                exception = checkNumberSetting(setting, exception, CHECK_PERIOD_LOW, CHECK_PERIOD_HIGH);
            } else if (setting.getKey().equals(CHECK_DURATION.name())) {
                exception = checkNumberSetting(setting, exception, CHECK_DURATION_LOW, CHECK_DURATION_HIGH);
            } else if (setting.getKey().equals(STORAGE_LIFE.name())) {
                exception = checkNumberSetting(setting, exception, STORAGE_LIFE_LOW, STORAGE_LIFE_HIGH);
            } else if (setting.getKey().equals(PAUSE.name())) {
                exception = checkNumberSetting(setting, exception, PAUSE_LOW, PAUSE_HIGH);
            } else if (setting.getKey().equals(SESSION_TIME.name())) {
                exception = checkNumberSetting(setting, exception,
                        SESSION_TIME_LOW, SESSION_TIME_HIGH);
            } else if (setting.getKey().equals(NUMBER_OF_SESSION.name())) {
                exception = checkNumberSetting(setting, exception,
                        NUMBER_OF_SESSION_LOW, NUMBER_OF_SESSION_HIGH);
            } else if (setting.getKey().equals(TIME_FOR_DELIVERY.name())) {
                exception = checkNumberSetting(setting, exception,
                        TIME_FOR_DELIVERY_LOW, TIME_FOR_DELIVERY_HIGH);
            } else if (setting.getKey().equals(NUMBER_OF_ROW_FOR_SINGLE_READ_WRITE.name())){
                    exception = checkNumberSetting(setting, exception, NUMBER_OF_ROW_FOR_SINGLE_READ_WRITE_LOW,
                            NUMBER_OF_ROW_FOR_SINGLE_READ_WRITE_HIGH);
            } else if (setting.getKey().equals(PERIOD_FOR_CLEANING.name())){
                exception = checkNumberSetting(setting, exception, TIME_FOR_CLEANING_LOW, TIME_FOR_CLEANING_HIGH);
            } else if (setting.getKey().endsWith(PREFIX_FOR_COMMON_SETTINGS_FOR_URL)) {
                try (Connection connection = DriverManager.getConnection(setting.getValue())) {
                } catch (SQLException sqlException) {
                    exception = addException(exception, "URL подключения к БД не работает! Ключ " + setting.getKey());
                    log.info(BUSINESS_MARKER, "Пользователь " + getLoggedUserName() + " ввёл невалидный URL. Ключ " + setting.getKey());
                }
            }
        }

        if (exception != null) {
            throw exception;
        }
    }

    private TMRuntimeException checkNumberSetting(CommonSettingsEntity setting, TMRuntimeException exception, int low, int high) {
        try {
            int result = Integer.parseInt(setting.getValue());
            if (result < low || result > high) {
                exception = addException(exception, "Значение по ключу " + setting.getKey() +
                        " должно быть больше " + low + " и меньше " + high + ".");
                log.info(BUSINESS_MARKER, "Пользователь " + getLoggedUserName() + " ввёл некорректное время " +
                        result + " для свойства " + setting.getKey() + ".");
            }
        } catch (NumberFormatException ex) {
            exception = addException(exception, PARSE_ERROR_MESSAGE + "Ключ " + setting.getKey());
            log.info(BUSINESS_MARKER, "Пользователь " + getLoggedUserName() + " " +
                    PARSE_ERROR_MESSAGE.replace("Н", "н") + " Ключ " + setting.getKey());
        }
        return exception;
    }
}
