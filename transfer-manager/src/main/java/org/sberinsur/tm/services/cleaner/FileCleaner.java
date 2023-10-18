package org.sberinsur.tm.services.cleaner;

import org.sberinsur.tm.beans.constants.TMResources;
import org.sberinsur.tm.dao.entity.test.EntityEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;

import static org.sberinsur.tm.beans.constants.Markers.BUSINESS_MARKER;
import static org.sberinsur.tm.beans.constants.TMResources.periodInHoursForCleaning;
import static org.sberinsur.tm.beans.constants.TMResources.storageLife;

/**
 * Класс, отвечающий за чистку директорий
 * @author Романов И.О.
 */
@Service
public class FileCleaner implements IFileCleaner {

    /** Пул потоков, в котором планируется чистка директорий */
    private final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1, runnable -> {
        Thread t = Executors.defaultThreadFactory().newThread(runnable);
        t.setName("FileCleanerThread");
        t.setDaemon(true);
        return t;
    });

    /** Поле для логирования */
    private static final Logger log = LoggerFactory.getLogger(FileCleaner.class);

    /**
     * Метод, чистящий по умолчанию две директорииЖ: директорию ошибок и успешно обработанных файлов.
     */
    public void clean(){
        scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {
            log.info(BUSINESS_MARKER, "Проверяю наличие неактуальных файлов");
            cleanErrorDirectory();
            cleanSuccessDirectory();
        }, 0, periodInHoursForCleaning, TimeUnit.HOURS);
    }

    /**
     * Метод, отвечащющий за удаление файлов, возрастом более определенного количества дней
     * @param targetDirectory - директория нахождения файлов
     * @author Романов И.О.
     */
    @Override
    public void clean(File targetDirectory){
         //Если директория существует
        if(targetDirectory.exists()){
             // Получаем список файлов из текущей директории
            List<File> listOfFiles = getListOfFilesInDirectory(targetDirectory);
            //Пробегаемся по списку файлов и спрашиваем его срок годности.
            //Если срок вышел - удаляем.
            for (File file: listOfFiles) {
                if(shouldDelete(getDateFromFileMask(file))){
                    if(file.delete()){
                        log.info(BUSINESS_MARKER, "Файл "+ file.getName() + " успешно удален "
                                + "из директории " + targetDirectory.getName());
                    } else {
                        log.error(BUSINESS_MARKER, "Ошибка удаления файла" + file.getName()
                                + "из директории " + targetDirectory.getName());
                    }
                }
            }
        }
    }

    /**
     * Метод, возвращающий дату с маски файла в формате LocalDateTime для дальнейшего принятия решения
     * @param file - файл, с макси которого считывваем дату
     * @return localDateTime - дата с маски файла
     */
    private LocalDateTime getDateFromFileMask(File file){
        List<EntityEntity> etalonFileProps = List.copyOf(TMResources.etalonMasksEntities);
        String[] mask = file.getName().split("_");
        String stringWithDate = mask[mask.length-1].split("\\.")[0];
        LocalDateTime localDateTime = null;
        for (EntityEntity etalon: etalonFileProps) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(etalon.getFileDateFormat(), Locale.getDefault());
            localDateTime = LocalDateTime.parse(stringWithDate, dtf);
        }
        return localDateTime;
    }


    /**
     * Метод, определяющий необходимо ли удалить файл
     * @param dateFromFileMask - дата с маски файла
     * @return - возращаем true - если можно удалить файл и  false в противном случае
     */
    private boolean shouldDelete(LocalDateTime dateFromFileMask){
        if(dateFromFileMask!=null){
            if((LocalDateTime.now().getYear()>dateFromFileMask.getYear() )) {
                return (365 - dateFromFileMask.getDayOfYear() + LocalDateTime.now().getDayOfYear()) > storageLife;
            }else if (dateFromFileMask.getYear() ==  LocalDateTime.now().getYear()){
                return(LocalDateTime.now().getDayOfYear() - dateFromFileMask.getDayOfYear() > storageLife);
            } else {
            return false;
        }
    }
    return false;
}

    /**
     * Метод повозвращающий список файлов из директории.
     * @param targetDirectory - директория, из которой создается список файлов
     * @return - список файлов в targetDirectory
     */
    private List<File> getListOfFilesInDirectory(File targetDirectory){
        return new LinkedList<>(Arrays.asList(Objects.requireNonNull(targetDirectory.listFiles())));
    }

    /**
     * Метод, чистящий только саксесс диреткорию
     */
    private void cleanSuccessDirectory(){
        clean(Path.of(TMResources.SuccessDirectory).toFile());
    }

    /**
     * Метод, чистящий только ерор диреткорию
     */
    private void cleanErrorDirectory(){
        clean(Path.of(TMResources.ErrorDirectory).toFile());
    }
}
