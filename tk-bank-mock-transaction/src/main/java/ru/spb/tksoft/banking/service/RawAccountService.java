package ru.spb.tksoft.banking.service;

import java.math.BigDecimal;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import ru.spb.tksoft.banking.entity.RawAccountEntity;
import ru.spb.tksoft.banking.repository.RawAccountRepository;
import ru.spb.tksoft.utils.log.LogEx;

/**
 * Account service for raw "account" data. Methods without cached data.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Service
@Transactional(
        isolation = Isolation.REPEATABLE_READ,
        propagation = Propagation.REQUIRED,
        timeout = 5)
@RequiredArgsConstructor
public class RawAccountService {

    private final Logger log = LoggerFactory.getLogger(RawAccountService.class);

    @NotNull
    private final RawAccountRepository rawAccountRepository;

    @NotNull
    private final RawAccountServiceCached rawAccountServiceCached;

    @Value("${banking.autoincrement.rate}")
    private double autoGrowRate = 0.1; // default value is 10%

    @Scheduled(cron = "${banking.autoincrement.cron:0/30 * * * * ?}")
    protected void autoGrow() {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        List<RawAccountEntity> accounts = rawAccountRepository.findAll();
        var rate = BigDecimal.valueOf(autoGrowRate);

        for (RawAccountEntity account : accounts) {
            if (account.getBalance().compareTo(BigDecimal.ZERO) <= 0 ||
                    account.getBalance().compareTo(account.getBalanceAutoLimit()) >= 0) {
                continue;
            }

            try {
                rawAccountServiceCached.grow(account.getUserId(), rate);
                Thread.sleep(1000); // Sleep for a second to prevent excessive processing
            } catch (Exception e) {
                log.error("Error while growing account: " + account.getUserId(), e);
            }
        }

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPED);
    }
}
