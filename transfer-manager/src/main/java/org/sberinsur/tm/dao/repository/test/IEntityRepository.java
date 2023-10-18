package org.sberinsur.tm.dao.repository.test;

import org.sberinsur.tm.dao.entity.test.EntityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

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

//    TODO: возможно, полезный метод получения только активных сущностей. Если не понадобится - не забыть убрать!
//    @Query("from EntityEntity where status = '" + WORKED_ENTITY_STATUS +  "'")
//    List<EntityEntity> findAllEtalonEntities();
}
