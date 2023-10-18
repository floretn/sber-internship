package org.sberinsur.tm.dto.entity.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Сущность аналитической записи.
 * @author Ненароков П.Ю.
 * @author Софронов И.Е.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DumpEntity {
    private Integer id;
    private String archiveName;
    private long archiveSize;
    private String fileName;
    private long fileSize;
    private String entityName;
    private int rowCount;
    private LocalDateTime started;
    private LocalDateTime finished;
    private LocalDateTime errorTime;
    private String errorDescription;
    private UUID deliveryGuid;
    private UUID fileGuid;

    public void describeError(LocalDateTime time, String description) {
        setErrorTime(time);
        setErrorDescription(description);
    }
}
