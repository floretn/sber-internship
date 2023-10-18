package org.sberinsur.tm.controllers;

import org.sberinsur.tm.beans.rest.AllDumpDTO;
import org.sberinsur.tm.dao.entity.test.DumpEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.sberinsur.tm.dao.repository.test.IDumpRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dump")
public class DumpController {

    @Autowired
    private IDumpRepository dumpRepository;

    @GetMapping
    public AllDumpDTO getAll() {
        return new AllDumpDTO(dumpRepository.findAll());
    }

    @GetMapping("find_by_id/{id}")
    public DumpEntity getDumpById(@PathVariable int id) {
        return dumpRepository.getById(id);
    }

    @PostMapping("save")
    public void saveDump(@RequestBody DumpEntity dump) {
        dumpRepository.save(dump);
    }
}
