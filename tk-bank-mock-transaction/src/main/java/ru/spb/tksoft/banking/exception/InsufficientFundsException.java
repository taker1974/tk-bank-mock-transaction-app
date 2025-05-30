package ru.spb.tksoft.banking.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.validation.constraints.NotBlank;
import ru.spb.tksoft.utils.log.LogEx;

/**
 * The amount is greater than the balance.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public class InsufficientFundsException extends RuntimeException {

    private static final Logger log = LoggerFactory.getLogger(InsufficientFundsException.class);

    /** Error code. */
    public static final int CODE = 4912;

    /** Error message. */
    public static final String MESSAGE = "Insufficient funds";

    /** Default constructor. */
    public InsufficientFundsException() {

        super(MESSAGE);
        LogEx.error(log, LogEx.getThisMethodName(), LogEx.EXCEPTION_THROWN, CODE, this);
    }

    /**
     * Constructor with additional message.
     * 
     * @param message Additional error message.
     */
    public InsufficientFundsException(@NotBlank final String message) {

        super(MESSAGE + ": " + message);
        LogEx.error(log, LogEx.getThisMethodName(), LogEx.EXCEPTION_THROWN, CODE, this);
    }
}
