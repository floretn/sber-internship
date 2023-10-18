package org.sberinsur.tm.secure;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.vault.repository.mapping.Secret;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Secret
public class Test {
    //@JsonProperty("ois_url_auth")
    private String ois_url_auth;
    //@JsonProperty("core_url_auth")
    private String core_url_auth;
}
