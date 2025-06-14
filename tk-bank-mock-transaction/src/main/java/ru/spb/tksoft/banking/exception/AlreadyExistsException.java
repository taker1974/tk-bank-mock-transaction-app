package ru.spb.tksoft.banking.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.validation.constraints.NotBlank;
import ru.spb.tksoft.utils.log.LogEx;

/**
 * Entity, user or whatever already exists.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public class AlreadyExistsException extends RuntimeException {

    private static final Logger log = LoggerFactory.getLogger(AlreadyExistsException.class);

    /** Error code. */
    public static final int CODE = 4912;

    /** Error message. */
    public static final String MESSAGE = "Object already exists";

    /**
     * The only constructor.
     * 
     * @param message Error message.
     */
    public AlreadyExistsException(@NotBlank final String message) {

        super(MESSAGE + ": " + message);
        LogEx.error(log, LogEx.getThisMethodName(), LogEx.EXCEPTION_THROWN, CODE, this);
    }
}
