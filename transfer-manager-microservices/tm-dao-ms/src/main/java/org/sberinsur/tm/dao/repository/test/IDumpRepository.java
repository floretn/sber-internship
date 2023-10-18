package org.sberinsur.tm.dao.repository.test;

import org.sberinsur.tm.dao.entity.test.DumpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Репозиторий сущности аналитики.
 * @author Ненароков П.Ю.
 * @author Софронов И.Е.
 */
@Repository
public interface IDumpRepository extends JpaRepository<DumpEntity, Integer> {
}
