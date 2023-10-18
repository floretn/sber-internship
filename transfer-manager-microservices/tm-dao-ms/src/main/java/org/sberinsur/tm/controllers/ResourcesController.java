package org.sberinsur.tm.controllers;

import org.sberinsur.tm.beans.constants.TMResources;
import org.sberinsur.tm.beans.rest.TMResourcesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tm_resources")
public class ResourcesController {

    @Autowired
    private TMResources tmResources;

    @GetMapping
    public TMResourcesDTO getResources() {
        return new TMResourcesDTO(tmResources);
    }
}
