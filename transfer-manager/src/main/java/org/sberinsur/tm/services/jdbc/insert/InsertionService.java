package org.sberinsur.tm.services.jdbc.insert;

import org.sberinsur.tm.beans.entity.BoxedFile;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Сервис вставки данных в целевую таблицу. Реализован через JDBC для упрощения работы с пакетными вставками.
 * @author Софронов И.Е.
 */
@Component
public class InsertionService implements IInsertionService{

    /**
     * Метод непосресдтвенной вставки данных в таблицу.
     * @param file обёртка файла со всеми мета-данными.
     * @param ps - скрипт вставки данных в БД.
     * @throws SQLException бросает при ошибках работы с БД или при составлении statement.
     */
    @Override
    public void insertParsed(BoxedFile file, PreparedStatement ps) throws SQLException {
        if (file.getContent() == null) {
            return;
        }
        for (String[] singleRow : file.getContent()) {
            setValues(ps, singleRow, file);
            ps.addBatch();
        }
        ps.executeBatch();
    }

    /**
     * Установка значений переменных для одной строки вносимой в БД.
     * @param ps принимает подготовленный запрос.
     * @param singleRow принимает массив значений.
     * @param file обёртка файла со всеми мета-данными.
     * @throws SQLException бросает при ошибках работы с БД или при составлении statement.
     */
    private void setValues(PreparedStatement ps, String[] singleRow, BoxedFile file) throws SQLException {
        int columnsNumberInCSV = singleRow.length;
        for (int i = 0; i < columnsNumberInCSV; i++) {
            try {
                ps.setInt(i + 1, Integer.parseInt(singleRow[i]));
                continue;
            } catch (NumberFormatException ex) {}

            try {
                ps.setDate(i + 1, new Date(new SimpleDateFormat(file.getValidatedEntity().getInnerDateFormat()).
                        parse(singleRow[i]).getTime()));
                continue;
            } catch (ParseException ex) {}

            ps.setString(i + 1, singleRow[i]);
        }

        ps.setString(++columnsNumberInCSV, file.getGuidOfFile().toString());
        ps.setString(++columnsNumberInCSV, file.getGuidOfDelivery().toString());
        ps.setDate(++columnsNumberInCSV, new Date(System.currentTimeMillis()));
    }
}
