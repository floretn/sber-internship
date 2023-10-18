package org.sberinsur.tm.services.waiter;

import lombok.Setter;
import org.sberinsur.tm.beans.rest.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.devtools.filewatch.ChangedFile;
import org.springframework.boot.devtools.filewatch.ChangedFiles;
import org.springframework.boot.devtools.filewatch.FileChangeListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
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

    @Setter
    private RestTemplate restTemplate;

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
                    try {
                        restTemplate.postForObject("http://tm-processor-ms/process", changedFile.getFile(), BaseResponse.class);
                    } catch (Exception ex) {
                       log.error(BUSINESS_MARKER, "", ex);
                    }
                }
            }
        }
    }
}
