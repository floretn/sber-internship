package org.sberinsur.tm.services.unpacker;

import org.rauschig.jarchivelib.ArchiveEntry;
import org.rauschig.jarchivelib.ArchiveStream;
import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.ArchiverFactory;
import org.sberinsur.tm.beans.exceptions.TMRuntimeException;
import org.sberinsur.tm.beans.entity.BoxedFile;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, ответственный за распаковку архивов с расширением tar.gz.
 * @author Романов И.О.
 */
@Service
public class Unpacker implements IUnpacker{

    /**
     * Метод отвечающий за распаковку архива, поступающего на вход
     * @param bfile - обёртка над исходным файлом, который нужно распаковать
     * @return unpackerFiles - список файлов из архива.
     */
    @Override
    public List<File> extractFromTar(BoxedFile bfile){
        //распаковываем из обертки
        File file = bfile.getWorkFile();
        //инстанс нашего экстрактора
        Archiver archiver = ArchiverFactory.createArchiver("tar","gz");
        try {
            //extract извлекает содержимое архива file в директорию наззначения
            archiver.extract(file, file.getParentFile());
        } catch (IOException e) {
            bfile.getDump().describeError(LocalDateTime.now(), e.getMessage());
            throw new TMRuntimeException(e);
        }
        List<File> unpakeredFiles = new ArrayList<>();
        for (String fileName : getEntry(bfile)) {
            unpakeredFiles.add(new File(bfile.getWorkFile().getParentFile().getAbsolutePath() + "/" + fileName));
        }
        return unpakeredFiles;
    }

    /**
     * Метод, получающий список стрингов файлов из архива - нужен для аналитики
     * @param bfile - архив, поступающий на вход
     * @return список стрингов файлов из архива bfile
     */
    @Override
    public List<String> getEntry(BoxedFile bfile) {
        File file = bfile.getWorkFile();
        Archiver archiver = ArchiverFactory.createArchiver("tar","gz");
        List<String> result = new ArrayList<>();
        try {
            ArchiveStream entryStream = archiver.stream(file);
            ArchiveEntry entry;
            while ((entry = entryStream.getNextEntry()) != null) {
                result.add(entry.getName());
            }
        } catch (IOException e) {
            bfile.getDump().describeError(LocalDateTime.now(), e.getMessage());
            throw new TMRuntimeException(e);
        }
        return result;
    }
}
