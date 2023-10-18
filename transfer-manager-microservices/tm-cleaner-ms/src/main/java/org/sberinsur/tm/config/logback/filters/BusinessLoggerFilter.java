package org.sberinsur.tm.config.logback.filters;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import org.sberinsur.tm.config.logback.filters.markers.Markers;
import org.slf4j.Marker;

/**
 * Фильтр для бизнес-логов. Если попавший сюда лог имеет маркер Markers.BUSINESS_MARKER, то он летит в файл с бизнес-логами.
 * @author Софронов И.Е.
 * @see Markers
 */
public class BusinessLoggerFilter extends Filter<ILoggingEvent> {
    /**
     * Метод создания фильтра для бизнес-логов.
     * @param event объект, содержащий проверяемый лог.
     * @return FilterReply - решение по логу.
     *                      ACCEPT - пропускает лог, если лог СОДЕРЖИТ маркер Markers.BUSINESS_MARKER.
     *                      DENY - запрещает лог, если лог НЕ СОДЕРЖИТ маркера.
     */
    @Override
    public FilterReply decide(ILoggingEvent event) {
        Marker marker = event.getMarker();
        if (marker != null && marker.contains(Markers.BUSINESS_MARKER)) {
            return FilterReply.ACCEPT;
        }
        return FilterReply.DENY;
    }
}
