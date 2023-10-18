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

    public EntityEntity() {}

    public EntityEntity(int id, String entityName, String mask, char status, LocalDate validDateFrom, LocalDate validDateBefore, String innerDateFormat, String fileDateFormat, Separator separator, String columns, String description, String dbConnectionKey) {
        this.id = id;
        this.entityName = entityName;
        this.mask = mask;
        this.status = status;
        this.validDateFrom = validDateFrom;
        this.validDateBefore = validDateBefore;
        this.innerDateFormat = innerDateFormat;
        this.fileDateFormat = fileDateFormat;
        this.separator = separator;
        this.columns = columns;
        this.description = description;
        this.dbConnectionKey = dbConnectionKey;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public LocalDate getValidDateFrom() {
        return validDateFrom;
    }

    public void setValidDateFrom(LocalDate validDateFrom) {
        this.validDateFrom = validDateFrom;
    }

    public LocalDate getValidDateBefore() {
        return validDateBefore;
    }

    public void setValidDateBefore(LocalDate validDateBefore) {
        this.validDateBefore = validDateBefore;
    }

    public String getInnerDateFormat() {
        return innerDateFormat;
    }

    public void setInnerDateFormat(String innerDateFormat) {
        this.innerDateFormat = innerDateFormat;
    }

    public String getFileDateFormat() {
        return fileDateFormat;
    }

    public void setFileDateFormat(String fileDateFormat) {
        this.fileDateFormat = fileDateFormat;
    }

    public Separator getSeparator() {
        return separator;
    }

    public void setSeparator(Separator separator) {
        this.separator = separator;
    }

    public String getColumns() {
        return columns;
    }

    public void setColumns(String columns) {
        this.columns = columns;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDbConnectionKey() {
        return dbConnectionKey;
    }

    public void setDbConnectionKey(String dbConnectionKey) {
        this.dbConnectionKey = dbConnectionKey;
    }
}
