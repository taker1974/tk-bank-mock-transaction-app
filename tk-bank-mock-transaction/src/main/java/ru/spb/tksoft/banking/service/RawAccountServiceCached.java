package ru.spb.tksoft.banking.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import ru.spb.tksoft.banking.dto.RawAccountDto;
import ru.spb.tksoft.banking.entity.RawAccountEntity;
import ru.spb.tksoft.banking.exception.InsufficientFundsException;
import ru.spb.tksoft.banking.exception.NewBalanceLimitException;
import ru.spb.tksoft.banking.mapper.RawAccountMapper;
import ru.spb.tksoft.banking.repository.RawAccountRepository;
import ru.spb.tksoft.banking.tools.PageTools;
import ru.spb.tksoft.utils.log.LogEx;

/**
 * Account service for raw "account" data. Cached methods.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Service
@Transactional(
        isolation = Isolation.REPEATABLE_READ,
        propagation = Propagation.REQUIRED,
        timeout = 5)
@RequiredArgsConstructor
public class RawAccountServiceCached {

    private final Logger log = LoggerFactory.getLogger(RawAccountServiceCached.class);

    @NotNull
    private final RawAccountRepository rawAccountRepository;

    /** Clear caches. */
    @CacheEvict(value = "account", allEntries = true)
    public void clearCaches() {
        // ...
    }

    /**
     * Get all accounts.
     * 
     * @return Paginated list of accounts.
     */
    @Cacheable(value = "account", unless = "#result.isEmpty()",
            key = "{#pageable.pageNumber, #pageable.pageSize, #pageable.sort}")
    @NotNull
    public Page<RawAccountDto> getAllAccounts(Pageable pageable) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        List<RawAccountEntity> entities = rawAccountRepository.findAll();
        List<RawAccountDto> dtos = new ArrayList<>(entities.size());

        entities.stream().forEach(
                entity -> dtos.add(
                        RawAccountMapper.toDto(entity)));

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return PageTools.convertListToPage(dtos, pageable);
    }

    /**
     * Process deposit.
     * 
     * @param userId User ID.
     * @param amount Amount.
     */
    @CacheEvict(value = "account", allEntries = true)
    public void deposit(Long userId, BigDecimal amount) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        validateAmount(amount);
        rawAccountRepository.deposit(userId, amount);

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPED);
    }

    /**
     * Process withdraw.
     * 
     * @param userId User ID.
     * @param amount Amount.
     * @throws InsufficientFundsException If the amount is greater than the balance.
     * @throws ConcurrencyFailureException If the account has been concurrently modified.
     */
    @CacheEvict(value = "account", allEntries = true)
    public void withdraw(Long userId, BigDecimal amount) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        validateAmount(amount);
        int updatedRows = rawAccountRepository.withdraw(userId, amount);

        if (updatedRows == 0) {
            RawAccountEntity account = rawAccountRepository.findByUserId(userId)
                    .orElseThrow(() -> new EntityNotFoundException("Account not found"));

            if (account.getBalance().compareTo(amount) < 0) {
                throw new InsufficientFundsException(
                        "account with userId " + userId);
            }

            throw new ConcurrencyFailureException(
                    "Concurrent modification detected for account: " + userId);
        }

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPED);
    }

    private void validateAmount(BigDecimal amount) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }

    /**
     * Process transfer from one account to another.
     * 
     * @param fromUserId From user ID.
     * @param toUserId To user ID.
     * @param amount Amount.
     * @throws InsufficientFundsException If the amount is greater than the balance.
     * @throws ConcurrencyFailureException If the account has been concurrently modified.
     * @throws IllegalArgumentException If the fromAccountId and toAccountId are the same.
     */
    @CacheEvict(value = "account", allEntries = true)
    public void transfer(Long fromUserId, Long toUserId, BigDecimal amount) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        validateAmount(amount);

        // Protect from self-transfer
        if (fromUserId.equals(toUserId)) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }

        // Withdraw from the source account
        int withdrawResult = rawAccountRepository.withdraw(fromUserId, amount);

        if (withdrawResult == 0) {
            RawAccountEntity fromAccount = rawAccountRepository.findByUserId(fromUserId)
                    .orElseThrow(() -> new EntityNotFoundException("Source account not found"));

            if (fromAccount.getBalance().compareTo(amount) < 0) {
                throw new InsufficientFundsException(
                        "account with userId " + fromUserId);
            }

            throw new ConcurrencyFailureException(
                    "Concurrent modification detected for account: " + fromUserId);
        }

        // Deposit into the destination account
        rawAccountRepository.deposit(toUserId, amount);

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPED);
    }

    /**
     * Grow the balance of an account by a specified rate.
     * 
     * @param userId UserId ID.
     * @param rate Rate of growth.
     * @throws IllegalArgumentException If the rate or maxLimit is invalid.
     * @throws NewBalanceLimitException If the new balance exceeds the maximum limit.
     * @throws ConcurrencyFailureException If the account has been concurrently modified.
     * @throws EntityNotFoundException If the account is not found.
     */
    @CacheEvict(value = "account", allEntries = true)
    public void grow(Long userId, BigDecimal rate) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        if (rate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Rate must be positive");
        }

        RawAccountEntity account = rawAccountRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        if (account.getBalanceAutoLimit().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Max limit cannot be negative");
        }

        // Atomic calculation of balance increase and checking for the maximum limit
        int updated = rawAccountRepository.growBalance(userId, rate);

        if (updated == 0) {
            BigDecimal newBalance = account.getBalance().multiply(BigDecimal.ONE.add(rate));

            if (newBalance.compareTo(account.getBalanceAutoLimit()) > 0) {
                throw new NewBalanceLimitException();
            }

            throw new ConcurrencyFailureException(
                    "Concurrent modification detected");
        }

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPED);
    }
}
