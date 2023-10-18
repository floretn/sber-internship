package org.sberinsur.tm.dao.repository.test;

import org.sberinsur.tm.dao.entity.test.CommonSettingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для получение основных настроек приложения.
 * @author Ненароков П.Ю.
 * @author Софронов И.Е.
 */
@Repository
public interface ICommonSettingsRepository extends JpaRepository<CommonSettingsEntity, Integer> {
    CommonSettingsEntity findByKey(String key);
    CommonSettingsEntity findByValue(String value);
}
