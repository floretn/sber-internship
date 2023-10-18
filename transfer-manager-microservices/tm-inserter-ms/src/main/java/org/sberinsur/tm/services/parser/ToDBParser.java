package org.sberinsur.tm.services.parser;

import com.opencsv.*;
import org.sberinsur.tm.beans.entity.BoxedFile;
import org.sberinsur.tm.beans.exceptions.TMRuntimeException;
import org.sberinsur.tm.beans.rest.BaseResponse;
import org.sberinsur.tm.dto.entity.test.CommonSettingsEntity;
import org.sberinsur.tm.services.insert.IInsertionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.web.client.RestTemplate;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import static org.springframework.vault.core.VaultKeyValueOperationsSupport.KeyValueBackend.*;
import static org.sberinsur.tm.beans.constants.EntityConstants.*;
import static org.sberinsur.tm.beans.constants.Markers.BUSINESS_MARKER;
import static org.sberinsur.tm.util.file.FileValidator.validFileStructure;
import static org.sberinsur.tm.beans.constants.TMResources.numberOfRowForSingleReadWrite;

/**
 * Сервис, парсящий CSV файлы.
 * @author Ненароков П.Ю.
 * @author Софронов И.Е.
 */
@Service
public class ToDBParser implements IToDBParser {

    private static final Logger log = LoggerFactory.getLogger(ToDBParser.class);

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private IInsertionService insertionService;
    @Autowired
    private Environment env;
    @Autowired
    private VaultTemplate vaultTemplate;

    @Value("${ois_url}")
    private String oisUrl;

    @Value("${core_url}")
    private String coreUrl;

    /**
     * Главный метод, непосредственно парсящий файл.
     * @param file путь к файлу для обработки.
     * @throws TMRuntimeException при неудачном прочтении файла.
     */
    public void  testRead(BoxedFile file) {
        CSVParser parser = new CSVParserBuilder()
                    .withEscapeChar('\"')
                    .withQuoteChar('\'')
                    .withSeparator(file.getValidatedEntity().getSeparator().getSeparator())
                .build();

        try (Reader reader = Files.newBufferedReader(Path.of(file.getWorkFile().getPath()));
             CSVReader csvReader = new CSVReaderBuilder(reader).withCSVParser(parser).build()) {

            validFileStructure(csvReader.readNext(), file.getValidatedEntity());

            Iterator<String[]> iterator = csvReader.iterator();

            String key = file.getValidatedEntity().getDbConnectionKey();
            String url = env.getProperty(key);
            String authInfo = (String) vaultTemplate.
                            opsForKeyValue("tm-ms/", unversioned()).get("db_keys").getData().get(key + "_auth");
            if (authInfo != null) {
                url = url + authInfo;
            }

            try (Connection conn = DriverManager.getConnection(url);

                    PreparedStatement ps = conn.prepareStatement(buildBigStatement(file))) {
                conn.setAutoCommit(false);
                try{
                    while (iterator.hasNext()) {
                        file.setContent(new ArrayList<>());
                        for (int i = 0; i < numberOfRowForSingleReadWrite; i++) {
                            file.getContent().add(iterator.next());
                            if (!iterator.hasNext()) {
                                break;
                            }
                        }
                        insertionService.insertParsed(file, ps);
                        file.incrementRowCount(file.getContent().size());
                        log.info(BUSINESS_MARKER, file.getContent().size() + "");
                        file.getContent().clear();
                    }
                } catch (SQLException ex) {
                    try {
                        conn.rollback();
                    } catch (Exception ex1) {
                        ex.addSuppressed(ex1);
                    }
                    throw ex;
                }
                conn.commit();
            }
        } catch (TMRuntimeException ex) {
            log.error(BUSINESS_MARKER, "", ex);
            throw ex;
        } catch (Exception ex) {
            log.error(BUSINESS_MARKER, "", ex);
            throw new TMRuntimeException(ex);
        }
        log.info(BUSINESS_MARKER, "Данные успешно считаны из файла " + file.getWorkFile().getName());
    }

    /**
     * Составление запроса в БД по валидной маске и структуре файла.
     * @param file принимает валидированный файл.
     * @return строку, представляющую собой запрос.
     */
    private String buildBigStatement(BoxedFile file) {
        StringBuilder builderMain = new StringBuilder();
        StringBuilder builderValues = new StringBuilder();
        String[] columns = file.getValidatedEntity().getColumns().substring(1).split("/");

        builderMain.append("insert into ").append(file.getValidatedEntity().getEntityName()).append(" (").
                append(columns[0]);
        builderValues.append(") values (?");

        for (int i = 1; i < columns.length; i++) {
            builderMain.append(", ").append(columns[i]);
            builderValues.append(", ?");
        }

        builderMain.append(", ").append(NAME_FOR_COLUMN_OF_OBJECT_GUID).
                append(", ").append(NAME_FOR_COLUMN_OF_DELIVERY_GUID).
                append(", ").append(NAME_FOR_COLUMN_OF_PROCESS_DATE);

        builderValues.append(", ?, ?, ?);");
        builderMain.append(builderValues);

        return builderMain.toString();
    }
}
