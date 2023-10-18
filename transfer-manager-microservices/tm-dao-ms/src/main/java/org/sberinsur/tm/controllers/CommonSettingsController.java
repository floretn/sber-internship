package org.sberinsur.tm.controllers;

import org.sberinsur.tm.beans.rest.AllCommonSettingsDTO;
import org.sberinsur.tm.beans.rest.BaseResponse;
import org.sberinsur.tm.dao.entity.test.CommonSettingsEntity;
import org.sberinsur.tm.dao.repository.test.ICommonSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/settings")
public class CommonSettingsController {

    @Autowired
    private ICommonSettingsRepository settingsRepository;

    @GetMapping("find_by_key/{key}")
    public CommonSettingsEntity getSettingByKey(@PathVariable String key) {
        return settingsRepository.findByKey(key);
    }

    @PostMapping("find_by_value")
    public AllCommonSettingsDTO getSettingByValue(@RequestBody String value) {
        return new AllCommonSettingsDTO(List.of(settingsRepository.findByValue(value)));
    }

    @GetMapping("find_all")
    public AllCommonSettingsDTO getAllSettings() {
        return new AllCommonSettingsDTO(settingsRepository.findAll());
    }

    @PostMapping("find_any")
    public AllCommonSettingsDTO getAny(@RequestBody CommonSettingsEntity setting) {
        return new AllCommonSettingsDTO(Arrays.asList(settingsRepository.findByKey(setting.getKey()),
                settingsRepository.findByValue(setting.getValue())));
    }

    @PutMapping
    public void saveSetting(@RequestBody CommonSettingsEntity setting) {
        settingsRepository.save(setting);
    }

    @PostMapping
    public BaseResponse updateSettings(@RequestBody AllCommonSettingsDTO settings) {
        settingsRepository.saveAll(settings.getCommonSettings());
        return new BaseResponse(200, null);
    }

    @DeleteMapping()
    public void deleteSetting(@RequestBody CommonSettingsEntity setting) {
        settingsRepository.delete(setting);
    }
}
