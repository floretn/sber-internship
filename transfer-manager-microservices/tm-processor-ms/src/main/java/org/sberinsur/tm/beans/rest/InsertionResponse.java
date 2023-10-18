package org.sberinsur.tm.beans.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sberinsur.tm.beans.entity.BoxedFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsertionResponse extends BaseResponse {
    private BoxedFile file;
}
