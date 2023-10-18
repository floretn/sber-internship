package org.sberinsur.tm.dto.entity.test;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Сущность аналитической записи.
 * @author Ненароков П.Ю.
 * @author Софронов И.Е.
 */
@Data
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

    public LocalDateTime getErrorTime() {
        return errorTime;
    }

    public void setErrorTime(LocalDateTime errorTime) {
        this.errorTime = errorTime;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getArchiveName() {
        return archiveName;
    }

    public void setArchiveName(String archiveName) {
        this.archiveName = archiveName;
    }

    public long getArchiveSize() {
        return archiveSize;
    }

    public void setArchiveSize(long archiveSize) {
        this.archiveSize = archiveSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public LocalDateTime getStarted() {
        return started;
    }

    public void setStarted(LocalDateTime started) {
        this.started = started;
    }

    public LocalDateTime getFinished() {
        return finished;
    }

    public void setFinished(LocalDateTime finished) {
        this.finished = finished;
    }

    public UUID getDeliveryGuid() {
        return deliveryGuid;
    }

    public void setDeliveryGuid(UUID deliveryGuid) {
        this.deliveryGuid = deliveryGuid;
    }

    public UUID getFileGuid() {
        return fileGuid;
    }

    public void setFileGuid(UUID fileGuid) {
        this.fileGuid = fileGuid;
    }
}
