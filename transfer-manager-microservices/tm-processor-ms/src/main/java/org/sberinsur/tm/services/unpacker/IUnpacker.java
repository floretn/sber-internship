package org.sberinsur.tm.services.unpacker;

import org.sberinsur.tm.beans.entity.BoxedFile;

import java.io.File;
import java.util.List;

/**
 * Интерфейс сервиса распаковки.
 */
public interface IUnpacker {
    List<File> extractFromTar(BoxedFile file);
    List<String> getEntry(BoxedFile file);
}
