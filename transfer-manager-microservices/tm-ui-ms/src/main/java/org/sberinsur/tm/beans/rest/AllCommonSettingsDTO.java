package org.sberinsur.tm.beans.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sberinsur.tm.dto.entity.test.CommonSettingsEntity;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AllCommonSettingsDTO {
    private List<CommonSettingsEntity> commonSettings;
}
