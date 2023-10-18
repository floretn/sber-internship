package org.sberinsur.tm.beans.rest;

import org.sberinsur.tm.dao.entity.test.EntityEntity;
import org.sberinsur.tm.beans.constants.TMResources;

import java.util.List;

public class TMResourcesDTO {
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

    public TMResourcesDTO(TMResources resources) {
        this.SuccessDirectory = resources.SuccessDirectory;
        this.ErrorDirectory = resources.ErrorDirectory;
        this.ProcessDirectory = resources.ProcessDirectory;
        this.TarGZ = resources.TarGZ;
        this.LoadingHere = resources.LoadingHere;
        this.etalonMasksEntities = resources.etalonMasksEntities;
        this.checkPeriod = resources.checkPeriod;
        this.checkDuration = resources.checkDuration;
        this.storageLife = resources.storageLife;
        this.pause = resources.pause;
        this.sessionTime = resources.sessionTime;
        this.numberOfSession = resources.numberOfSession;
        this.timeForDelivery = resources.timeForDelivery;
        this.numberOfRowForSingleReadWrite = resources.numberOfRowForSingleReadWrite;
        this.periodInHoursForCleaning = resources.periodInHoursForCleaning;
    }
}
