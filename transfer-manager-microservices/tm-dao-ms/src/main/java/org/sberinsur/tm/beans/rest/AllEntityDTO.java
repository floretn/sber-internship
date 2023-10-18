package org.sberinsur.tm.beans.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sberinsur.tm.dao.entity.test.EntityEntity;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AllEntityDTO {
    private List<EntityEntity> entities;
}
