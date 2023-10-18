package org.sberinsur.tm.dao.entity.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Модель для пользователей системы.
 * @author Софронов И.Е.
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USERS", schema = "AUTH_INFO")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_PK")
    private int id;

    @Column(name = "USER_USERNAME")
    private String username;

    @Column(name = "USER_PASSWORD_HASH")
    private String password;

}
