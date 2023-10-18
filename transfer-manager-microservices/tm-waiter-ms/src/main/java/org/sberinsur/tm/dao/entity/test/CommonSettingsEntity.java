package org.sberinsur.tm.dao.entity.test;

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
}
