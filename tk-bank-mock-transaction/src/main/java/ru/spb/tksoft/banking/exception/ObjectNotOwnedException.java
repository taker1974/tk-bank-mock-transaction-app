package ru.spb.tksoft.banking.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.validation.constraints.NotBlank;
import ru.spb.tksoft.utils.log.LogEx;

/**
 * Object is not owned by the user.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public class ObjectNotOwnedException extends RuntimeException {

    private static final Logger log = LoggerFactory.getLogger(ObjectNotOwnedException.class);

    /** Error code. */
    public static final int CODE = 1033;

    /** Error message. */
    public static final String MESSAGE = "Object not owned by the user";

    /** Default constructor. */
    public ObjectNotOwnedException() {

        super(MESSAGE);
        LogEx.error(log, LogEx.getThisMethodName(), LogEx.EXCEPTION_THROWN, CODE, this);
    }

    /**
     * Constructor with additional message.
     * 
     * @param message Additional error message.
     */
    public ObjectNotOwnedException(@NotBlank final String message) {

        super(MESSAGE + ": " + message);
        LogEx.error(log, LogEx.getThisMethodName(), LogEx.EXCEPTION_THROWN, CODE, this);
    }
}
