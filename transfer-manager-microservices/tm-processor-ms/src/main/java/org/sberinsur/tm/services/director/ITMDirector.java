package org.sberinsur.tm.services.director;

import org.sberinsur.tm.beans.entity.BoxedFile;

import java.io.File;

/** Ненароков
 * Интерфейс оркестратора.
 */
public interface ITMDirector {
    void processFile(File file, BoxedFile archive);
    void startProcessing();
    void stopProcessing();
}
