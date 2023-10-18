package org.sberinsur.tm.beans.constants;

import lombok.Getter;
import org.sberinsur.tm.dto.entity.test.EntityEntity;
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

}
