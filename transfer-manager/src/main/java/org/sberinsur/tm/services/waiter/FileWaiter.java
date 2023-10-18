package org.sberinsur.tm.services.waiter;

import org.sberinsur.tm.services.director.ITMDirector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.devtools.filewatch.ChangedFile;
import org.springframework.boot.devtools.filewatch.ChangedFiles;
import org.springframework.boot.devtools.filewatch.FileChangeListener;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.sberinsur.tm.beans.constants.Markers.BUSINESS_MARKER;

/**
 * Класс кастомного менеджера приема файлов.
 * @author Ненароков П.Ю.
 * @author Софронов И.Е.
 */
@Service
public class FileWaiter implements FileChangeListener {

    private static final Logger log = LoggerFactory.getLogger(FileWaiter.class);

    @Autowired
    ITMDirector director;

    /**
     * Логика поведения после обнаружения новых файлов в наблюдаемых директориях.
     * @param changeSet список файлов (обнаруженных либо измененных) сгруппированный по директориям.
     */
    @Override
    public void onChange(Set<ChangedFiles> changeSet) {
        for (ChangedFiles changedFiles : changeSet) {
            for (ChangedFile changedFile : changedFiles) {
                if (changedFile.getType() == ChangedFile.Type.ADD) {
                    log.info(BUSINESS_MARKER, "На обработку поступил файл " + changedFile.getFile().getName());
                    director.processFile(changedFile.getFile(), null);
                }
            }
        }
    }
}
