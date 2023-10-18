package org.sberinsur.tm.beans.constants;

/**
 * Относится к web слою.
 * Константы для перевода миллисекунд в другие единицы времени + границ числовых свойств, устанавливаемых с фронта.
 * Единица измерения ВРЕМЕНИ по умолчанию - миллисекунды.
 * @author Софронов И.Е.
 */
public class SystemPropertiesBorders {
    /**Количество миллисекунд в минуте*/
    public static final int MILLISECONDS_IN_MINUTE = 60000;
    /**Нижняя граница для свойства CHECK_PERIOD*/
    public static final int CHECK_PERIOD_LOW = 200;
    /**Верхняя граница для свойства CHECK_PERIOD*/
    public static final int CHECK_PERIOD_HIGH = 5000;
    /**Нижняя граница для свойства CHECK_DURATION*/
    public static final int CHECK_DURATION_LOW = 50;
    /**Верхняя граница для свойства CHECK_DURATION*/
    public static final int CHECK_DURATION_HIGH = 500;
    /**Нижняя граница для свойства STORAGE_LIFE (в днях)*/
    public static final int STORAGE_LIFE_LOW = 10;
    /**Верхняя граница для свойства STORAGE_LIFE (в днях)*/
    public static final int STORAGE_LIFE_HIGH = 90;
    /**Нижняя граница для свойства PAUSE*/
    public static final int PAUSE_LOW = 100;
    /**Верхняя граница для свойства PAUSE*/
    public static final int PAUSE_HIGH = 1000;
    /**Нижняя граница для свойства SESSION_TIME (в минутах)*/
    public static final int SESSION_TIME_LOW = 5;
    /**Верхняя граница для свойства SESSION_TIME (в минутах)*/
    public static final int SESSION_TIME_HIGH = 15;
    /**Нижняя граница для свойства NUMBER_OF_SESSION*/
    public static final int NUMBER_OF_SESSION_LOW = 1;
    /**Верхняя граница для свойства NUMBER_OF_SESSION*/
    public static final int NUMBER_OF_SESSION_HIGH = 10;
    /**Нижняя граница для свойства SESSION_TIME (в минутах)*/
    public static final int TIME_FOR_DELIVERY_LOW = 15;
    /**Верхняя граница для свойства SESSION_TIME (в минутах)*/
    public static final int TIME_FOR_DELIVERY_HIGH = 35;
    /**Нижняя граница для свойства NUMBER_OF_ROW_FOR_SINGLE_READ_WRITE*/
    public static final int NUMBER_OF_ROW_FOR_SINGLE_READ_WRITE_LOW = 1000;
    /**Верхняя граница для свойства NUMBER_OF_ROW_FOR_SINGLE_READ_WRITE*/
    public static final int NUMBER_OF_ROW_FOR_SINGLE_READ_WRITE_HIGH = 100000;
    /**Нижняя граница времени для чистки директорий*/
    public static final int TIME_FOR_CLEANING_LOW = 1;
    /**Нижняя граница времени для чистки директорий*/
    public static final int TIME_FOR_CLEANING_HIGH = 240;
}
