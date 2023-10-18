package org.sberinsur.tm.cleaner;

import java.io.File;

/**
 * Интерфейс сервиса очистки файлов с истекшим сроком годности.
 * @author ioromanov
 */
public interface IFileCleaner {
        void clean(File file);
        void clean();
}
