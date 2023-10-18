package org.sberinsur.tm.dao.entity.test;

import lombok.*;

import javax.persistence.*;

/**
 * Сущность системных настроек.
 * @author Ненароков П.Ю.
 * @author Софронов И.Е.
 */
@Entity
@Table(name = "common_settings", schema = "test")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonSettingsEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column
    String key;

    @Column
    String value;
}
