package org.sberinsur.tm.beans.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sberinsur.tm.beans.exceptions.TMRuntimeException;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse {
    private int status;
    private TMRuntimeException error;
}
