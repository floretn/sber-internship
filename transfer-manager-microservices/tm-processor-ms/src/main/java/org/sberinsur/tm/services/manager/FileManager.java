package org.sberinsur.tm.services.manager;

import org.sberinsur.tm.beans.exceptions.TMRuntimeException;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;

/**
 * Класс, ответственный за перемещение объектов между рабочими директориями.
 * @author Романов И.О.
 * @author Софронов И.Е.
 */
@Component
public class FileManager implements IFileManager {

    /**
     * Метод, удаляющий файл file.
     * @param file - целевой файл.
     */
    @Override
    public void removeFile(File file) throws IOException {
        if(!file.delete()){
            throw new IOException("Невозможно удалить файл " + file.getAbsolutePath() + ".");
        }
    }

    /**
     * Метод, отвечающий за пкопирование файлов, на вход подается сам файл, и директория назначения.
     * @param file целевой файл.
     * @param path директория назначения.
     * @return File копия файла.
     */
    @Override
    public File copy(File file, Path path) throws IOException {
        Path rezFile = Files.copy(file.toPath(), Paths.get(path.toString() + "/" + file.getName()));
        return rezFile.toFile();
    }

    /**
     * Метод, отвечающий за перемещение или файлов, на вход подается сам файл, и директория назначения.
     * @param file - исходный файл, который нужно переместить.
     * @param path - директория назначения файла.
     * @return File перенесённый файл.
     */
    @Override
    public File move(File file, Path path) throws IOException {
        Path rezFile = Files.move(file.toPath(), Paths.get(path.toString() + "/" + file.getName()));
        return rezFile.toFile();
    }
}
