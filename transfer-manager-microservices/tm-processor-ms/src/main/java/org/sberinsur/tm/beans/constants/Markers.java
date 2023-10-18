package org.sberinsur.tm.beans.constants;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * Тут лежат константы с маркерами для логгирования.
 * Пример использования: log.info(MARKER, "Message");
 * Лучше использовать статический импорт.
 * @author Софронов И.Е.
 * @see org.sberinsur.tm.config.logback.filters.BusinessLoggerFilter
 * @see org.sberinsur.tm.config.logback.filters.TechnicalLoggerFilter
 */
public class Markers {
    /**
     * Константа для бизнес-логов.
     * Если логируется информация по безнес-логике (файл принят в обработку, файл обработан и т.п.) используем его.
     * Пока есть только этот маркер (наверное, больше и не появится), поэтому им маркируем бизнес логику - логи летят в файл с логами по бизнесу.
     * Не маркированные логи летят в файл с техническими логами.
     */
    public static final Marker BUSINESS_MARKER = MarkerFactory.getMarker("BUSINESS");
}
