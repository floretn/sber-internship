package org.sberinsur.tm.beans.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sberinsur.tm.dao.entity.test.DumpEntity;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllDumpDTO {
    private List<DumpEntity> dumps;
}
