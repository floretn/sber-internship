package org.sberinsur.tm.controller;

import org.sberinsur.tm.secure.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.vault.core.VaultKeyValueOperationsSupport;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

//    @Autowired
    private Test test = new Test();

    @Autowired
    private Environment environment;

    @Autowired
    private VaultTemplate vaultTemplate;

    @GetMapping("test")
    public String getOisUrl() {
        return environment.getProperty("ois_url") +
                (String) vaultTemplate.opsForKeyValue("tm-ms/",
                        VaultKeyValueOperationsSupport.KeyValueBackend.unversioned()).get("db_keys").getData().
                        get("ois_url_auth");
    }

    @GetMapping("esho")
    public String getEshoOdinTest() {
        return environment.getProperty("core_url") + test.getCore_url_auth();
    }
}
