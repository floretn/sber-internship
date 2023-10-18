package org.sberinsur.tm.beans.rest;

import org.sberinsur.tm.dto.entity.test.EntityEntity;

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
//
//    public TMResources() {
//        this.SuccessDirectory = org.sberinsur.tm.beans.constants.TMResources.SuccessDirectory;
//        this.ErrorDirectory = org.sberinsur.tm.beans.constants.TMResources.ErrorDirectory;
//        this.ProcessDirectory = org.sberinsur.tm.beans.constants.TMResources.ProcessDirectory;
//        this.TarGZ = org.sberinsur.tm.beans.constants.TMResources.TarGZ;
//        this.LoadingHere = org.sberinsur.tm.beans.constants.TMResources.LoadingHere;
//        this.etalonMasksEntities = org.sberinsur.tm.beans.constants.TMResources.etalonMasksEntities;
//        this.checkPeriod = org.sberinsur.tm.beans.constants.TMResources.checkPeriod;
//        this.checkDuration = org.sberinsur.tm.beans.constants.TMResources.checkDuration;
//        this.storageLife = org.sberinsur.tm.beans.constants.TMResources.storageLife;
//        this.pause = org.sberinsur.tm.beans.constants.TMResources.pause;
//        this.sessionTime = org.sberinsur.tm.beans.constants.TMResources.sessionTime;
//        this.numberOfSession = org.sberinsur.tm.beans.constants.TMResources.numberOfSession;
//        this.timeForDelivery = org.sberinsur.tm.beans.constants.TMResources.timeForDelivery;
//        this.numberOfRowForSingleReadWrite = org.sberinsur.tm.beans.constants.TMResources.numberOfRowForSingleReadWrite;
//        this.periodInHoursForCleaning = org.sberinsur.tm.beans.constants.TMResources.periodInHoursForCleaning;
//    }
}
