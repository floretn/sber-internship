package org.sberinsur.tm.beans.constants;

import lombok.Getter;
import org.sberinsur.tm.dao.entity.test.EntityEntity;
import org.sberinsur.tm.dao.repository.test.ICommonSettingsRepository;
import org.sberinsur.tm.dao.repository.test.IEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

import static org.sberinsur.tm.beans.constants.SystemPropertiesBorders.*;
import static org.sberinsur.tm.beans.enums.NamesOfSetupValuesEnum.*;

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

    @Autowired
    public TMResources (ICommonSettingsRepository setupValuesRepo, IEntityRepository maskRepo) {
        this.commonSettingsRepository = setupValuesRepo;
        this.entityRepository = maskRepo;
    }

    //Директория для успешно обработанных файлов
    public String SuccessDirectory;

    //Директория для файлов обработанных с ошибкой
    public String ErrorDirectory;

    //Рабочая директория
    public String ProcessDirectory;

    // Маркерная строка, для пометки файла как архива.
    public String TarGZ;

    // Путь к директории загрузки.
    public String LoadingHere;

    //валидные маски
    public List<EntityEntity> etalonMasksEntities;

    //интервал проверки входящих файлов
    public long checkPeriod;
    //интервал блокировки хранилища на время проверки
    public long checkDuration;
    //интервал очистки директорий
    public long storageLife;
    /**Интервал ожидания мгновенного увеличения размера файла*/
    public long pause;
    /**Интервал ожидания догрузки файла в рамках сессии*/
    public long sessionTime;
    /**Количество сессий, в которые ждём догрузку файла*/
    public int numberOfSession;
    /**Время поставки*/
    public long timeForDelivery;

    public int numberOfRowForSingleReadWrite;

    /**Период отчистки директорийи*/
    public long periodInHoursForCleaning;

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

        if (!Files.exists(Paths.get(SuccessDirectory))) {
            SuccessDirectory = Files.createDirectories(Paths.get(SuccessDirectory)).toAbsolutePath().toString();
        }

        if (!Files.exists(Paths.get(ErrorDirectory))) {
            ErrorDirectory = Files.createDirectories(Paths.get(ErrorDirectory)).toAbsolutePath().toString();
        }

        if (!Files.exists(Paths.get(ProcessDirectory))) {
            ProcessDirectory = Files.createDirectories(Paths.get(ProcessDirectory)).toAbsolutePath().toString();
        }

        if (!Files.exists(Paths.get(LoadingHere))) {
            ErrorDirectory = Files.createDirectories(Paths.get(LoadingHere)).toAbsolutePath().toString();
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
