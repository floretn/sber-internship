package org.sberinsur.tm.controllers;

import org.sberinsur.tm.beans.rest.AllEntityDTO;
import org.sberinsur.tm.dao.entity.test.EntityEntity;
import org.sberinsur.tm.dao.repository.test.IEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/entity")
public class EntitiesController {

    @Autowired
    private IEntityRepository entityRepository;

    @GetMapping("find_all")
    public AllEntityDTO getEntities() {
        return new AllEntityDTO(entityRepository.findAll());
    }

    @GetMapping("find_by_id/{id}")
    public EntityEntity getEntityById(@PathVariable int id) {
        return entityRepository.findById(id);
    }

    @GetMapping("find_by_name/{name}")
    public EntityEntity getEntityById(@PathVariable String name) {
        return entityRepository.findByEntityName(name);
    }

    @GetMapping("find_by_mask/{mask}")
    public EntityEntity getEntityByMask(@PathVariable String mask) {
        return entityRepository.findByMask(mask);
    }

    @PostMapping("find_by_db_connection_key")
    public AllEntityDTO getEntityByConnectionKey(@RequestBody String key) {
        return new AllEntityDTO(entityRepository.findByDbConnectionKey(key));
    }

    @GetMapping("return_all_entities")
    public AllEntityDTO getAllEtalonEntities(){
        return new AllEntityDTO(entityRepository.findAllEtalonEntities());
    }

    @PostMapping("save")
    public void saveEntity(@RequestBody EntityEntity entity) {
        entityRepository.save(entity);
    }
}
