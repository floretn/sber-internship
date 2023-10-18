package org.sberinsur.tm.services.director;

import org.sberinsur.tm.beans.exceptions.TMNotValidMaskException;
import org.sberinsur.tm.beans.exceptions.TMRuntimeException;
import org.sberinsur.tm.beans.exceptions.TMStoppedEntityException;
import org.sberinsur.tm.dao.repository.test.IDumpRepository;
import org.sberinsur.tm.beans.entity.BoxedFile;
import org.sberinsur.tm.services.manager.IFileManager;
import org.sberinsur.tm.services.parser.IToDBParser;
import org.sberinsur.tm.services.unpacker.IUnpacker;
import org.sberinsur.tm.util.file.FileValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.*;

import static org.sberinsur.tm.beans.constants.Markers.BUSINESS_MARKER;
import static org.sberinsur.tm.beans.constants.TMResources.*;

/**
 * Класс параллельно обрабатывающий входящие файлы.
 * @author Романов И.О.
 * @author Софронов И.Е.
 */
@Service
class TMDirector implements ITMDirector {

    private static final Logger log = LoggerFactory.getLogger(TMDirector.class);

    private final IToDBParser parser;
    private final IUnpacker unpacker;
    private final IFileManager fileManager;
    private final IDumpRepository analyticsRepo;

    /**Очередь задач, которая сохраняется при остановке бассейна потоков, чтобы внести изменения пропертей в систему*/
    private BlockingQueue<Runnable> tasks;
    /**Бассейн потоков, в которых обрабатываются пачки файлов*/
    private ThreadPoolExecutor threadPool = new ThreadPoolExecutor(4, 4, 0L,
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
    /**Статус работы бассейна потоков*/
    private boolean status = true;

    private long timeOfStartLastDelivery = -1;
    private UUID guidOfDelivery = UUID.randomUUID();

    @Autowired
    public TMDirector(IToDBParser parser, IUnpacker unpacker, IFileManager mover,
                      IDumpRepository analyticsRepo) {
        this.parser = parser;
        this.unpacker = unpacker;
        this.fileManager = mover;
        this.analyticsRepo = analyticsRepo;
    }

    /**
     * Метод добавления потока обработки каждого файла из пачки файлов в бассейн потоков.
     * @param changedFile файл, упавший в директорию загрузок.
     * @param archive архив, из которого получен файл. Если файл не был в архиве - null.
     * @author Софронов И.Е.
     */
    @Override
    public void processFile(File changedFile, BoxedFile archive) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - timeOfStartLastDelivery > timeForDelivery) {
            timeOfStartLastDelivery = currentTime;
            guidOfDelivery = UUID.randomUUID();
            log.info(BUSINESS_MARKER, "Начало новой поставки файлов");
        }
        Runnable fileHandler = () -> {
            BoxedFile boxedFile = new BoxedFile(changedFile, guidOfDelivery, archive);
            try {
                int cntSession = 1;
                boxedFile.getDump().setFileName(boxedFile.getWorkFile().getName());
                boxedFile.getDump().setFileSize(boxedFile.getWorkFile().getUsableSpace());
                while (cntSession <= numberOfSession) {
                    log.info(BUSINESS_MARKER, cntSession + "-ая итерация цикла обработки файла " + changedFile.getName());
                    boxedFile.setWorkFile(changedFile);
                    log.info(BUSINESS_MARKER, "Ожидается догрузка файла " + changedFile.getName());
                    receiveFile(boxedFile.getWorkFile());
                    log.info(BUSINESS_MARKER, "Файл " + changedFile.getName() + " принят в обработку");
                    boxedFile.setWorkFile(fileManager.copy(boxedFile.getWorkFile(), Path.of(ProcessDirectory)));
                    log.info(BUSINESS_MARKER, "Файл " + changedFile.getName() + " скопирован в рабочую директорию");
                    try {
                        if (parseFile(boxedFile)) {
                            log.info(BUSINESS_MARKER, "Файл " + changedFile.getName() + " был успешно обработан. " +
                                    boxedFile.getDump().getRowCount() + " строк");
                            moveFile(boxedFile, SuccessDirectory);
                            log.info(BUSINESS_MARKER, "Файл " + changedFile.getName() +
                                    " был перенесён из рабочей директории в " +
                                    "директорию успешно обработанных файлов");
                            boxedFile.getDump().setFinished(LocalDateTime.now());
                            analyticsRepo.save(boxedFile.getDump());
                            break;
                        }
                    } catch (TMStoppedEntityException exception) {
                        log.info(BUSINESS_MARKER, exception.getMessage());
                        moveFile(boxedFile, ErrorDirectory);
                        log.info(BUSINESS_MARKER, "Файл " + changedFile.getName() + " был перенесён из рабочей директории в " +
                                "директорию ошибок");
                        return;
                    } catch (TMNotValidMaskException exception) {
                        log.info(BUSINESS_MARKER, exception.getMessage());
                        fileManager.removeFile(boxedFile.getWorkFile());
                        log.info(BUSINESS_MARKER, "Файл " + boxedFile.getWorkFile().getName() +
                                " был удалён из рабочей директории");
                        return;
                    } catch (TMRuntimeException ex) {
                        log.error(BUSINESS_MARKER, "Ошибка обработки " + boxedFile.getWorkFile().getName(), ex);
                        fileManager.removeFile(boxedFile.getWorkFile());
                        log.info(BUSINESS_MARKER, "Файл " + boxedFile.getWorkFile().getName() +
                                " был удалён из рабочей директории");
                        return;
                    }
                    if (cntSession == numberOfSession) {
                        log.info(BUSINESS_MARKER, "Количество допустимых сессий для файла " + changedFile.getName() +
                                " истекло");
                        moveFile(boxedFile, ErrorDirectory);
                        log.info(BUSINESS_MARKER, "Файл " + changedFile.getName() + " был перенесён из рабочей директории в " +
                                "директорию ошибок");
                        boxedFile.getDump().setFinished(LocalDateTime.now());
                        analyticsRepo.save(boxedFile.getDump());
                    } else {
                        log.info(BUSINESS_MARKER, "Файл " + changedFile.getName() + " НЕ был успешно обработан");
                        fileManager.removeFile(boxedFile.getWorkFile());
                        log.info(BUSINESS_MARKER, "Файл " + changedFile.getName() + " был удалён из рабочей директории");
                    }
                    cntSession++;
                }
            } catch (Exception ex) {
                log.error(BUSINESS_MARKER, "", ex);
            }
        };

        if (status) {
            threadPool.execute(fileHandler);
        } else {
            tasks.add(fileHandler);
        }
    }

    /**
     * Метод, останавливающий пул потоков обработки файлов.
     * @author Софронов И.Е.
     */
    @Override
    public synchronized void stopProcessing() {
        log.info(BUSINESS_MARKER, "Начало остановки пула потоков обработки файлов");
        if (!status) {
            return;
        }
        tasks = new LinkedBlockingQueue<>();
        status = false;
        threadPool.shutdown();
        tasks.addAll(threadPool.getQueue());
        while (threadPool.getActiveCount() != 0) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                log.error(BUSINESS_MARKER, "", e);
            }
        }
        threadPool = null;
        log.info(BUSINESS_MARKER, "Пул потоков обработки файлов успешно остановлен");
    }

    /**
     * Метод, возобновляющий работу пула потоков обработки файлов.
     * @author Софронов И.Е.
     */
    @Override
    public synchronized void startProcessing() {
        log.info(BUSINESS_MARKER, "Начало повторного запуска пула потоков обработки файлов");
        if (status) {
            return;
        }
        threadPool = new ThreadPoolExecutor(4, 4, 0L,
                TimeUnit.MILLISECONDS, tasks);
        status = true;
        tasks = null;
        log.info(BUSINESS_MARKER, "Пул потоков обработки файлов успешно запущен");
    }

    /**
     * Метод обработки файла.
     * @param changedFile файл, который необходимо обработать.
     * @throws TMNotValidMaskException при ошибке валидации маски файла.
     * @throws TMRuntimeException при любых остальных ошибках обработки файла.
     * @author Романов И.О.
     * @author Софронов И.Е.
     */
    private boolean parseFile(BoxedFile changedFile) {
        changedFile.getDump().setStarted(LocalDateTime.now());
        if(changedFile.getWorkFile().exists()){
            FileValidator.validMask(changedFile);
            if(FileValidator.getFileExtension(changedFile.getWorkFile()).equals("csv")){
                log.info(BUSINESS_MARKER, "Принятый в обработку файл " + changedFile.getWorkFile().getName() +
                        " - НЕ архив");
                try{
                    parser.testRead(changedFile);
                    changedFile.getDump().describeError(null, null);
                    return true;
                } catch (TMRuntimeException e){
                    changedFile.getDump().describeError(LocalDateTime.now(), e.getMessage());
                    log.error(BUSINESS_MARKER, "Не могу записать файл " +
                            changedFile.getWorkFile().getName() + " в БД: " + e.getMessage());
                    return false;
                }
            }
            if(FileValidator.getFileExtension(changedFile.getWorkFile()).equals("tar.gz")){
                log.info(BUSINESS_MARKER, "Принятый в обработку в файл " + changedFile.getWorkFile().getName() + " - Архив");
                if(FileValidator.certainValidMask(unpacker.getEntry(changedFile),
                                                  changedFile.getValidatedEntity())){
                    log.info(BUSINESS_MARKER, "Начинаю распаковку архива " +
                            changedFile.getWorkFile().getName() + ".");
                    for (File file : unpacker.extractFromTar(changedFile)) {
                        processFile(file, changedFile);
                        log.info(BUSINESS_MARKER, "Файл " + file.getName() + " из архива " +
                                changedFile.getWorkFile().getName() + " отправлен на обработку");
                    }
                    log.info(BUSINESS_MARKER, "Архив " + changedFile.getWorkFile().getName() +
                            " успешно распакован, все файлы запущены в обработку");
                    changedFile.getDump().describeError(null, null);
                    return true;
                } else {
                    changedFile.getDump().describeError(LocalDateTime.now(),
                            "Архив " + changedFile.getWorkFile().getName() + " не прошёл валидацию");
                    log.info(BUSINESS_MARKER, "Архив " + changedFile.getWorkFile().getName() + " содержит файлы, на подходящие по маске");
                    return  false;
                }
            }
            throw new TMNotValidMaskException("Маска файла не валидна, так как его расширение на равно csv или tar.gz");
        }
        return false;
    }

    /**
     * Метод ожидания конца загрузки файла.
     * @param file файл, загрузки которого необходимо дождаться.
     * @author Софронов И.Е.
     */
    private void receiveFile(File file) throws InterruptedException {
        long usableSpace = -1;
        while (usableSpace < file.getUsableSpace()) {
            while (usableSpace < file.getUsableSpace()) {
                usableSpace = file.getUsableSpace();
                Thread.sleep(pause);
            }
            Thread.sleep(sessionTime);
        }
    }

    /**
     * Метод удаления файла из директории загрузок + метод переноса файла из рабочей директории
     * в необходимую директорию (ошибок или успешно обработанных файлов).
     * @param boxedFile обёртка обрабатываемого файла для обработки.
     * @param directory директория, куда необходимо перенести файл из рабочей директории.
     * @throws TMRuntimeException при ошибках удаления/перемещения файлов.
     *                            Если ошибок несколько - будут подавлены.
     * @author Софронов И.Е.
     */
    private void moveFile(BoxedFile boxedFile, String directory) {
        TMRuntimeException exception = null;
        try {
            fileManager.move(boxedFile.getWorkFile(), Path.of(directory));
        } catch (TMRuntimeException ex) {
            exception = ex;
        }
        if (!boxedFile.isFromArchive()) {
            try {
                fileManager.removeFile(new File(boxedFile.getOriginalFile()));
                log.info(BUSINESS_MARKER, "Файл " + boxedFile.getOriginalFile() + " был удалён из директории загрузок");
            } catch (TMRuntimeException ex) {
                if (exception == null) {
                    exception = ex;
                } else {
                    exception.addSuppressed(ex);
                }
            }
        }
        if (exception != null) {
            throw exception;
        }
    }
}