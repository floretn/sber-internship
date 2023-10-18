package org.sberinsur.tm.beans.constants;

import lombok.Getter;
import org.sberinsur.tm.beans.rest.TMResourcesDTO;
import org.sberinsur.tm.dto.entity.test.EntityEntity;

import java.util.*;

/**
 * Класс, содержащий статические переменные, используемые приложением.
 * @author Ненароков П.Ю.
 * @author Софронов И.Е.
 */
@Getter
public class TMResources {
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

    public static void setAllProperties(TMResourcesDTO resources) {
        SuccessDirectory = resources.SuccessDirectory;
        ErrorDirectory = resources.ErrorDirectory;
        ProcessDirectory = resources.ProcessDirectory;
        TarGZ = resources.TarGZ;
        LoadingHere = resources.LoadingHere;
        etalonMasksEntities = resources.etalonMasksEntities;
        checkPeriod = resources.checkPeriod;
        checkDuration = resources.checkDuration;
        storageLife = resources.storageLife;
        pause = resources.pause;
        sessionTime = resources.sessionTime;
        numberOfSession = resources.numberOfSession;
        timeForDelivery = resources.timeForDelivery;
        numberOfRowForSingleReadWrite = resources.numberOfRowForSingleReadWrite;
        periodInHoursForCleaning = resources.periodInHoursForCleaning;
    }
}
