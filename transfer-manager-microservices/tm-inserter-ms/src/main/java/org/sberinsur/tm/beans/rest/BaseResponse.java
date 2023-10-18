package org.sberinsur.tm.beans.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.sberinsur.tm.beans.entity.BoxedFile;
import org.sberinsur.tm.beans.exceptions.TMRuntimeException;

@Data
@AllArgsConstructor
public class BaseResponse {
    private int status;
    private TMRuntimeException error;
    private BoxedFile file;
}
