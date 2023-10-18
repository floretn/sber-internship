package org.sberinsur.tm.dao.entity.test;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Сущность аналитической записи.
 * @author Ненароков П.Ю.
 * @author Софронов И.Е.
 */
@Entity
@Table(name = "dump", schema = "test")
@Data
public class DumpEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "archive_name")
    private String archiveName;

    @Column(name = "archive_size")
    private long archiveSize;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_size")
    private long fileSize;

    @Column(name = "entity_name")
    private String entityName;

    @Column(name = "row_count")
    private int rowCount;

    @Column
    private LocalDateTime started;

    @Column
    private LocalDateTime finished;

    @Column(name = "error_time")
    private LocalDateTime errorTime;

    @Column(name = "error_description")
    private String errorDescription;

    @Column(name = "delivery_guid")
    private UUID deliveryGuid;

    @Column(name = "file_guid")
    private UUID fileGuid;

    public void describeError(LocalDateTime time, String description) {
        setErrorTime(time);
        setErrorDescription(description);
    }
}
