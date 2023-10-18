package org.sberinsur.tm.util.file;

import org.sberinsur.tm.beans.constants.TMResources;
import org.sberinsur.tm.beans.entity.BoxedFile;
import org.sberinsur.tm.beans.exceptions.TMRuntimeException;
import org.sberinsur.tm.dto.entity.test.EntityEntity;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * Класс, содержащий статические методы для разного рода
 * валидации структуры файлов.
 * @author Ненароков П.Ю.
 */
public class FileValidator{

    /**
     * Проверка колонок файла на соответсвие.
     * @param columnsInFile принимает массив строк из названий колонок в файле.
     * @param validFileProps принимает образец валидной структуры файла.
     * @throws TMRuntimeException в случае ошибок в структуре файла.
     */
    public static void validFileStructure(String[] columnsInFile, EntityEntity validFileProps) {

        String[] etalonColumns = validFileProps.getColumns().split("/");

        if (columnsInFile.length != etalonColumns.length) {
            throw new TMRuntimeException("Файл не может быть обработан из-за ошибок в структуре: " +
                    "количество столбцов не совпадает с ожидаемым");
        }

        for (int i = 0; i < columnsInFile.length; i++) {
            if (!columnsInFile[i].equals(etalonColumns[i])) {
                throw new TMRuntimeException("Файл не может быть обработан из-за ошибок в структуре: " +
                        "на месте " + i + "-го столбца с названием " + columnsInFile[i] + " ожидается столбец " + etalonColumns[i]);
            }
        }
    }

    /**
     * Проверяет с каким расширением мы работаем.
     * @param bfile прнимает путь к файлу.
     * @return возвращает строку с решением о дальнейшей обработке.
     */
    public static String checkIfArchive(BoxedFile bfile) {
        File file = bfile.getWorkFile();
        if (getFileExtension(file).equals("csv")) {
            return TMResources.ProcessDirectory;
        } else if (getFileExtension(file).equals("tar.gz")) {
            return TMResources.TarGZ;
        } else {
            return TMResources.ErrorDirectory;
        }
    }

    /**
     * Проверяет название файла на соответствие.
     * @param file принимает путь к файлу.
     * @return возвращает решение о валидности (да/нет).
     */
    public static boolean validMask(BoxedFile file) {


        List<EntityEntity> etalonFileProps = List.copyOf(TMResources.etalonMasksEntities);

        var flag = new Object() {
            boolean valid = false;
        };

        String[] mask = file.getWorkFile().getName().split("_");

        etalonFileProps.forEach(etalon -> {
            boolean valid = true;

            for (int i = 0; i < mask.length - 1; i++) {
                if (!mask[i].equals(etalon.getMask().split("_")[i])) {
                    valid = false;

                    break;
                }
            }
            try{
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern(etalon.getFileDateFormat(), Locale.getDefault());
                LocalDateTime.parse(mask[mask.length - 1].substring(0,mask[mask.length - 1].indexOf('.')), dtf);
            } catch (Exception e) {
                valid = false;
            }
            if (valid) {
                flag.valid = true;
                file.setValidatedEntity(etalon);
            }
        });

        if ((file.getValidatedEntity() == null) ||
                (file.getValidatedEntity().getValidDateBefore().isBefore(LocalDate.now()) &&
                        file.getValidatedEntity().getValidDateFrom().isAfter(LocalDate.now()))) {
            return false;
        }
        file.getDump().setEntityName(file.getValidatedEntity().getEntityName());
        return flag.valid;
    }

    /**
     * Определяет расширение файла.
     * @param file принимает путь к файлу.
     * @return возвращает строку с расширением файла.
     */
    public static String getFileExtension(File file) {
        String fileName = file.getName();

        if(fileName.contains(".") && fileName.indexOf(".") != 0) {
            return fileName.substring(fileName.indexOf(".") + 1);
        } else {
            return "";
        }
    }

    /**
     * Проверка валидности файла по конкретной маске
     * @param files принимает список файлов для проверки
     * @param archiveFileProps принимает маску образец
     * @return решение о валидности (да/нет)
     */
    public static boolean certainValidMask(List<String> files, EntityEntity archiveFileProps ) {

        boolean valid = true;

        for (String file : files) {

            String[] mask = file.split("_");

            for (int i = 0; i < mask.length - 1; i++) {
                if (!mask[i].equals(archiveFileProps.getMask().split("_")[i])) {
                    valid = false;
                    break;
                }
            }
            if (!valid) {
                break;
            }
        }

        return valid;
    }
}