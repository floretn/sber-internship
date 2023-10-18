package org.sberinsur.tm.controllers;

import org.sberinsur.tm.beans.entity.BoxedFile;
import org.sberinsur.tm.beans.exceptions.TMRuntimeException;
import org.sberinsur.tm.beans.rest.BaseResponse;
import org.sberinsur.tm.services.parser.IToDBParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.sberinsur.tm.beans.constants.Markers.BUSINESS_MARKER;

@RestController
@RequestMapping("/insert")
public class InsertController {

    private static final Logger log = LoggerFactory.getLogger(InsertController.class);

    @Autowired
    private IToDBParser parser;

    @PostMapping
    public BaseResponse insert(@RequestBody BoxedFile file) {
        log.info(BUSINESS_MARKER, "В обработку принят файл " + file.getWorkFile());
        try {
            parser.testRead(file);
        } catch (TMRuntimeException exception) {
            return new BaseResponse(500, exception, file);
        }
        return new BaseResponse(200, null, file);
    }
}
