package ru.spb.tksoft.banking.controller.advice;

import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import ru.spb.tksoft.common.controller.advice.CommonControllerAdvice;

/**
 * Processing of very common exception. Order is important! @see @Order()
 * 
 * @see ru.spb.tksoft.common.controller.advice.CommonControllerAdvice
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@ControllerAdvice
@Order()
public class BankingCommonControllerAdvice extends CommonControllerAdvice {

    /**
     * Конструктор по умолчанию.
     */
    public BankingCommonControllerAdvice() {
        super();
    }
}
