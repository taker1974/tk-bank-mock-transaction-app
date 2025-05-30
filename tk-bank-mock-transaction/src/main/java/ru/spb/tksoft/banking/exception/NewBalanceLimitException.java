package ru.spb.tksoft.banking.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.validation.constraints.NotBlank;
import ru.spb.tksoft.utils.log.LogEx;

/**
 * New balance exceeds maximum limit.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public class NewBalanceLimitException extends RuntimeException {

    private static final Logger log = LoggerFactory.getLogger(NewBalanceLimitException.class);

    /** Error code. */
    public static final int CODE = 2641;

    /** Error message. */
    public static final String MESSAGE = "New balance exceeds maximum limit";

    /** Default constructor. */
    public NewBalanceLimitException() {

        super(MESSAGE);
        LogEx.error(log, LogEx.getThisMethodName(), LogEx.EXCEPTION_THROWN, CODE, this);
    }

    /**
     * Constructor with additional message.
     * 
     * @param message Additional error message.
     */
    public NewBalanceLimitException(@NotBlank final String message) {

        super(MESSAGE + ": " + message);
        LogEx.error(log, LogEx.getThisMethodName(), LogEx.EXCEPTION_THROWN, CODE, this);
    }
}
