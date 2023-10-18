package org.sberinsur.tm.beans.constants;

import lombok.Getter;
import org.sberinsur.tm.dao.entity.test.EntityEntity;
import org.sberinsur.tm.dao.repository.test.IEntityRepository;
import org.sberinsur.tm.dao.repository.test.ICommonSettingsRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.*;

import static org.sberinsur.tm.beans.enums.NamesOfSetupValuesEnum.*;
import static org.sberinsur.tm.beans.constants.SystemPropertiesBorders.*;

/**
 * Класс, содержащий статические переменные, используемые приложением.
 * @author Ненароков П.Ю.
 * @author Софронов И.Е.
 */
@Getter
@Component
public class TMResources {

    //репозиторий основных настроек
    ICommonSettingsRepository commonSettingsRepository;
    //репохиторий валидных масок
    IEntityRepository entityRepository;

    public TMResources (ICommonSettingsRepository setupValuesRepo, IEntityRepository maskRepo) {
        this.commonSettingsRepository = setupValuesRepo;
        this.entityRepository = maskRepo;
    }

    //Директория для успешно обработанных файлов
    public static String SuccessDirectory;

    //Директория для файлов обработанных с ошибкой
    public static String ErrorDirectory;

    //Рабочая директория
    public static String ProcessDirectory;

    // Маркерная строка, для пометки файла как архива.
    public static String TarGZ;

    // Путь к директории загрузки.
    public static String LoadingHere;

    //валидные маски
    public static List<EntityEntity> etalonMasksEntities;

    //интервал проверки входящих файлов
    public static long checkPeriod;
    //интервал блокировки хранилища на время проверки
    public static long checkDuration;
    //интервал очистки директорий
    public static long storageLife;
    /**Интервал ожидания мгновенного увеличения размера файла*/
    public static long pause;
    /**Интервал ожидания догрузки файла в рамках сессии*/
    public static long sessionTime;
    /**Количество сессий, в которые ждём догрузку файла*/
    public static int numberOfSession;
    /**Время поставки*/
    public static long timeForDelivery;

    public static int numberOfRowForSingleReadWrite;

    /**Период отчистки директорийи*/
    public static long periodInHoursForCleaning;

    @PostConstruct
    public void startSetting() throws SQLException, IOException {
        setAllProperties();
    }

    /**
     * Метод, устанавливающий рабочие сущности для работы с файлами.
     * @author Софронов И.Е.
     *
     */
    public void setEtalonMasksEntities() {
        etalonMasksEntities = entityRepository.findAll();
    }

    /**
     * Метод назначения статических переменных, обращается к БД хранящей необходимые данные.
     * @throws IOException при отстутствии нужных директорий и невозможности их создать.
     * @throws NullPointerException при отсутствии требуемых настроек в БД.
     * @author Ненароков П.Ю.
     * @author Софронов И.Е.
     */
    public void setAllProperties() throws IOException, SQLException {

        SuccessDirectory = commonSettingsRepository.findByKey(SUCCESS_DIRECTORY.name()).getValue();
        ErrorDirectory = commonSettingsRepository.findByKey(ERROR_DIRECTORY.name()).getValue();
        ProcessDirectory = commonSettingsRepository.findByKey(PROCESS_DIRECTORY.name()).getValue();
        LoadingHere = commonSettingsRepository.findByKey(LOADING_HERE.name()).getValue();

        TarGZ = "archiveIsTarGz";

        if (!Files.exists(Path.of(SuccessDirectory))) {
            SuccessDirectory = Files.createDirectories(Path.of(SuccessDirectory)).toAbsolutePath().toString();
        }

        if (!Files.exists(Path.of(ErrorDirectory))) {
            ErrorDirectory = Files.createDirectories(Path.of(ErrorDirectory)).toAbsolutePath().toString();
        }

        if (!Files.exists(Path.of(ProcessDirectory))) {
            ProcessDirectory = Files.createDirectories(Path.of(ProcessDirectory)).toAbsolutePath().toString();
        }

        if (!Files.exists(Path.of(LoadingHere))) {
            ErrorDirectory = Files.createDirectories(Path.of(LoadingHere)).toAbsolutePath().toString();
        }

        setEtalonMasksEntities();

        checkDuration = Long.parseLong(commonSettingsRepository.findByKey(CHECK_DURATION.name()).getValue());
        checkPeriod = Long.parseLong(commonSettingsRepository.findByKey(CHECK_PERIOD.name()).getValue());
        storageLife = Long.parseLong(commonSettingsRepository.findByKey(STORAGE_LIFE.name()).getValue());
        pause = Long.parseLong(commonSettingsRepository.findByKey(PAUSE.name()).getValue());
        sessionTime = Long.parseLong(commonSettingsRepository.findByKey(SESSION_TIME.name()).getValue()) * MILLISECONDS_IN_MINUTE;
        numberOfSession = Integer.parseInt(commonSettingsRepository.findByKey(NUMBER_OF_SESSION.name()).getValue());
        timeForDelivery = Long.parseLong(commonSettingsRepository.findByKey(TIME_FOR_DELIVERY.name()).getValue()) *
                MILLISECONDS_IN_MINUTE;
        numberOfRowForSingleReadWrite = Integer.parseInt(commonSettingsRepository.findByKey(NUMBER_OF_ROW_FOR_SINGLE_READ_WRITE.name()).
                getValue());
        periodInHoursForCleaning = Integer.parseInt(commonSettingsRepository.findByKey(PERIOD_FOR_CLEANING.name()).getValue());

    }
}
