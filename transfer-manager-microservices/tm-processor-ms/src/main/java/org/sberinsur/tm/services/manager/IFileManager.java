package org.sberinsur.tm.services.manager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Интерфейс сервиса перемещения файлов
 */
public interface IFileManager {
    File move(File file, Path path) throws IOException;
    File copy(File file, Path path) throws IOException;
    void removeFile(File file) throws IOException;
}
