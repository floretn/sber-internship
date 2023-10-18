package org.sberinsur.tm.beans.rest;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BaseResponse {
    private int status;
    private Exception error;
}
