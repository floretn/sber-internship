package org.sberinsur.tm.controller;

import org.sberinsur.tm.secure.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.vault.authentication.TokenAuthentication;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.core.VaultKeyValueOperationsSupport;
import org.springframework.vault.core.VaultTemplate;

@Configuration
public class VaultConfiguration {

    @Value("${vault.token}")
    private String token;

    @Bean
    public VaultTemplate vaultTemplate() {
        VaultEndpoint vaultEndpoint = VaultEndpoint.create("127.0.0.1", 8200);
        vaultEndpoint.setScheme("http");
        return new VaultTemplate(vaultEndpoint, new TokenAuthentication(token));
    }

    //TODO: спросить, что тут не так блин
    @Bean
    public Test test() {
        return vaultTemplate().
                opsForKeyValue("tm-ms/", VaultKeyValueOperationsSupport.KeyValueBackend.KV_1).
                get("db_keys", Test.class).
                getData();
    }

}
