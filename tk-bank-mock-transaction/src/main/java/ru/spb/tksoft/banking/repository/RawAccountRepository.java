package ru.spb.tksoft.banking.repository;

import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.spb.tksoft.banking.entity.RawAccountEntity;

/**
 * Repository of RawAccountEntity.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Repository
public interface RawAccountRepository extends JpaRepository<RawAccountEntity, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM \"account\" a WHERE a.user_id = :userId")
    Optional<RawAccountEntity> findByUserId(@Param("userId") Long userId);

    /**
     * Deposits amount into user account.
     * 
     * @param userId User id.
     * @param amount Amount to deposit.
     */
    @Modifying
    @Query("UPDATE RawAccountEntity a SET a.balance = a.balance + :amount WHERE a.userId = :userId")
    void deposit(@Param("userId") Long userId, @Param("amount") BigDecimal amount);

    /**
     * Withdraws amount from user account.
     * 
     * @param userId User id.
     * @param amount Amount to withdraw.
     * @return Number of updated accounts.
     */
    @Modifying
    @Query("""
            UPDATE RawAccountEntity a
            SET a.balance = a.balance - :amount
            WHERE a.userId = :userId
            AND a.balance >= :amount
            """)
    int withdraw(@Param("userId") Long userId, @Param("amount") BigDecimal amount);

    /**
     * Grows account balance by specified rate, but not more than the specified limit. "AND
     * (a.balance * (1 + :rate)) > 0" - protection from overflow.
     * 
     * @param userId User id.
     * @param rate Rate to grow by.
     * @return Number of updated accounts. 1- success, 0 - fail.
     */
    @Modifying
    // @Query("""
    // UPDATE RawAccountEntity a
    // SET a.balance =
    // CASE
    // WHEN (a.balance * (1 + :rate)) <= a.balanceAutoLimit
    // THEN a.balance * (1 + :rate)
    // ELSE a.balanceAutoLimit
    // END
    // WHERE a.userId = :userId
    // AND (a.balance * (1 + :rate)) > 0
    // """)
    @Query(value = """
                UPDATE account
                SET balance =
                    CASE
                        WHEN (balance * (1 + CAST(:rate AS numeric))) <= balance_autolimit
                        THEN balance * (1 + CAST(:rate AS numeric))
                        ELSE balance_autolimit
                    END
                WHERE user_id = :userId
                AND (balance * (1 + CAST(:rate AS numeric))) > 0
            """, nativeQuery = true)
    int growBalance(@Param("userId") Long userId, @Param("rate") BigDecimal rate);
}
