package org.sberinsur.tm.services.parser;

import org.sberinsur.tm.beans.entity.BoxedFile;

/** Ненароков
 * Интерфейс сервиса парсинга.
 */
public interface IToDBParser {
   void testRead(BoxedFile file);
}
