package org.sberinsur.tm.dao.entity.test;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.sberinsur.tm.beans.enums.Separator;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Сущность сущностей.
 * @author Ненароков П.Ю.
 * @author Софронов И.Е.
 */
@Entity
@Table(name = "entity", schema = "test")
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EntityEntity {

    public EntityEntity(EntityEntity entity) {
        this.id = entity.id;
        this.entityName = entity.getEntityName();
        this.validDateBefore = entity.getValidDateBefore();
        this.validDateFrom = entity.getValidDateFrom();
        this.innerDateFormat = entity.getInnerDateFormat();
        this.fileDateFormat = entity.getFileDateFormat();
        this.mask = entity.getMask();
        this.status = entity.status;
        this.separator = entity.getSeparator();
        this.columns = entity.getColumns();
        this.description = entity.description;
        this.dbConnectionKey = entity.dbConnectionKey;
    }

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "entity_name")
    String entityName;

    @Column
    String mask;

    @Column
    char status;

    @Column(name = "valid_date_from")
    LocalDate validDateFrom;

    @Column(name = "valid_date_before")
    LocalDate validDateBefore;

    @Column(name = "inner_date_format")
    String innerDateFormat;

    @Column(name = "file_date_format")
    String fileDateFormat;

    @Column
    @Enumerated(EnumType.STRING)
    Separator separator;

    @Column
    String columns;

    @Column
    String description;

    @Column(name = "db_connection_key")
    String dbConnectionKey;
}
