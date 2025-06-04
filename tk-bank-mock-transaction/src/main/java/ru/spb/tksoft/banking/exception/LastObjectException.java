package ru.spb.tksoft.banking.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.validation.constraints.NotBlank;
import ru.spb.tksoft.utils.log.LogEx;

/**
 * Can't do action with the last object.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public class LastObjectException extends RuntimeException {

    private static final Logger log = LoggerFactory.getLogger(LastObjectException.class);

    /** Error code. */
    public static final int CODE = 8608;

    /** Error message. */
    public static final String MESSAGE = "Can't do action with the last object";

    /**
     * The only constructor.
     * 
     * @param message Error message.
     */
    public LastObjectException(@NotBlank final String message) {

        super(MESSAGE + ": " + message);
        LogEx.error(log, LogEx.getThisMethodName(), LogEx.EXCEPTION_THROWN, CODE, this);
    }
}
