package org.sberinsur.tm.dto.entity.test;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.sberinsur.tm.beans.enums.Separator;

import java.time.LocalDate;

/**
 * Сущность сущностей.
 * @author Ненароков П.Ю.
 * @author Софронов И.Е.
 */
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

    int id;
    String entityName;
    String mask;
    char status;
    LocalDate validDateFrom;
    LocalDate validDateBefore;
    String innerDateFormat;
    String fileDateFormat;
    Separator separator;
    String columns;
    String description;
    String dbConnectionKey;
}
