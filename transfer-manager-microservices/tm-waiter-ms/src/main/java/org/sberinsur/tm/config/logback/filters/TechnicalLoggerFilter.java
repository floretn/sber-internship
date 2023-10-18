package org.sberinsur.tm.config.logback.filters;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import org.sberinsur.tm.beans.constants.Markers;
import org.slf4j.Marker;

/**
 * Фильтр для технических логов. Если попавший сюда лог не имеет маркера, то он летит в файл с техническими логами.
 * @author Софронов И.Е.
 * @see Markers
 */
public class TechnicalLoggerFilter extends Filter<ILoggingEvent> {
    /**
     * Метод создания фильтра для технических-логов.
     * @param event объект, содержащий проверяемый лог.
     * @return FilterReply - решение по логу.
     *                      ACCEPT - пропускает лог, если лог НЕ СОДЕРЖИТ маркер Markers.BUSINESS_MARKER.
     *                      DENY - запрещает лог, если лог СОДЕРЖИТ маркер.
     */
    @Override
    public FilterReply decide(ILoggingEvent event) {
        Marker marker = event.getMarker();
        if (marker != null) {
            return FilterReply.DENY;
        }
        return FilterReply.ACCEPT;
    }
}
