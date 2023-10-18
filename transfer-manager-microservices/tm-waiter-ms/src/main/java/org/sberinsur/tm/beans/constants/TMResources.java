package org.sberinsur.tm.beans.constants;

import org.sberinsur.tm.dao.entity.test.EntityEntity;

import java.util.List;

/**
 * Класс, содержащий статические переменные, используемые приложением.
 * @author Ненароков П.Ю.
 * @author Софронов И.Е.
 */
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
