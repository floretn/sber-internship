package org.sberinsur.tm.beans.exceptions;

/**
 * Конкретизированное бизнес-исключение, сигнализирующее о несовпадении маски файла с активными масками для обработки.
 * @author Софронов И.Е.
 */
public class TMNotValidMaskException extends TMRuntimeException {
    public TMNotValidMaskException(String message) {
        super(message);
    }
}
