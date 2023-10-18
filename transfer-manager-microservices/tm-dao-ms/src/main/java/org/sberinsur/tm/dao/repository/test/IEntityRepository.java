package org.sberinsur.tm.dao.repository.test;

import org.sberinsur.tm.dao.entity.test.EntityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.sberinsur.tm.beans.constants.EntityConstants.WORKED_ENTITY_STATUS;

/**
 * Репозиторий для сущности маски.
 * @author Ненароков П.Ю.
 * @author Софронов И.Е.
 */
@Repository
public interface IEntityRepository extends JpaRepository<EntityEntity, Integer> {
    EntityEntity findByEntityName(String entityName);
    EntityEntity findByMask(String mask);
    EntityEntity findById(int id);
    List<EntityEntity> findByDbConnectionKey(String key);
}
