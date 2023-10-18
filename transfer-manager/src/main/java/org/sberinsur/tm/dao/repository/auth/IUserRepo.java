package org.sberinsur.tm.dao.repository.auth;

import org.sberinsur.tm.dao.entity.auth.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий общения системы с таблицей базы данных, содержащей информацию о пользователях.
 * @author Софронов И.Е.
 */
@Repository
public interface IUserRepo extends CrudRepository<UserEntity, Long> {
    /**
     * Метод получения пользователя по его имени
     * @param username строка-имя пользователя
     * @return UserEntity сущность из таблицы пользователей в БД. Если пользователь не найден, но вернётся null
     */
    UserEntity findByUsername(String username);
}
