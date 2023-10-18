package org.sberinsur.tm.services.cleaner;

import java.io.File;

/**
 * Интерфейс сервиса очистки файлов с истекшим сроком годности.
 */
public interface IFileCleaner {
        void clean(File file);
        void clean();
}
