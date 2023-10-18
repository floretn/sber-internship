package org.sberinsur.tm.services.jdbc.insert;

import org.sberinsur.tm.beans.entity.BoxedFile;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface IInsertionService {
    void insertParsed(BoxedFile file, PreparedStatement ps) throws SQLException;
}
