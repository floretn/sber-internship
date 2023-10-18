package org.sberinsur.tm.dto.entity.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Сущность системных настроек.
 * @author Ненароков П.Ю.
 * @author Софронов И.Е.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonSettingsEntity {
    Integer id;
    String key;
    String value;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
