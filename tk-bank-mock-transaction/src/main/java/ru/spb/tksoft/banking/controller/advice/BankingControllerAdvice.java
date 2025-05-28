package ru.spb.tksoft.banking.controller.advice;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import ru.spb.tksoft.common.controller.advice.AbstractBaseControllerAdvice;

/**
 * Перехват исключений RecommendationController.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BankingControllerAdvice extends AbstractBaseControllerAdvice {

    /**
     * Конструктор по умолчанию.
     */
    private BankingControllerAdvice() {
        super();
    }
}
