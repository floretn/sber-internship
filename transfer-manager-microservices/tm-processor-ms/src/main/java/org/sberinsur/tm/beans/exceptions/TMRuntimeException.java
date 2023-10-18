package org.sberinsur.tm.beans.exceptions;

/**
 * Runtime исключение для бизнес-логики. Использовать там, где
 * 1) Нужно исключение по бизнес логике;
 * 2) Нужно обернуть другое исключение, чтобы пробросить.
 * При необходимости добавляем новые конструкторы.
 * @author Софронов И.Е.
 */
public class TMRuntimeException extends RuntimeException {
    public TMRuntimeException(String message) {
        super(message);
    }

    public TMRuntimeException(Throwable throwable) {
        super(throwable);
    }

    public TMRuntimeException(String message, Throwable throwable) {
        super(message, throwable);
    }
}