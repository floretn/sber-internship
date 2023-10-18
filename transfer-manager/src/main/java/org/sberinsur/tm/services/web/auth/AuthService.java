package org.sberinsur.tm.services.web.auth;

import org.sberinsur.tm.dao.entity.auth.UserEntity;
import org.sberinsur.tm.dao.repository.auth.IUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Относится к web слою.
 * Сервис для авторизации пользователей в ситеме.
 * @author Софронов И.Е.
 */
@Service
public class AuthService implements UserDetailsService{

    @Autowired
    private IUserRepo userRepository;

    /**
     * Загрузка пользователя из БД по его имени. Необходимо только для авторизации пользователей
     * @see org.sberinsur.tm.config.spring.security.WebSecurityConfig
     * @param username строка-имя пользователя
     * @return UserEntity сущность из БД. Если пользователь с таким именем не был найден в БД, возвращаем пустого пользователя.
     *      В этом случае пароль к нему не подобрать, так как невозможно ввести null для строки
     *      и избегаем nullPointerException при обращении к пользователю.
     */
    @Override
    public UserEntity loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            return new UserEntity();
        }
        return user;
    }
}
