package org.sberinsur.tm.controllers;

import org.sberinsur.tm.beans.rest.BaseResponse;
import org.sberinsur.tm.services.director.ITMDirector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequestMapping("/process")
public class ProcessorController {

    @Autowired
    private ITMDirector tmDirector;

    @PostMapping
    public BaseResponse process(@RequestBody File file) {
        tmDirector.processFile(file, null);
        return null;
    }
}
