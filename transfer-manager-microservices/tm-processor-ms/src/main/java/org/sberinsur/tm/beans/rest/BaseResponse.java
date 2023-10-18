package org.sberinsur.tm.beans.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sberinsur.tm.beans.exceptions.TMRuntimeException;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse {
    protected int status;
    protected TMRuntimeException error;
}
