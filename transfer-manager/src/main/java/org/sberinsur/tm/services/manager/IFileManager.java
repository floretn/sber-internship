package org.sberinsur.tm.services.manager;

import java.io.File;
import java.nio.file.Path;

/**
 * Интерфейс сервиса перемещения файлов
 */
public interface IFileManager {
    File move(File file, Path path);
    File copy(File file, Path path);
    void removeFile(File file);
}
