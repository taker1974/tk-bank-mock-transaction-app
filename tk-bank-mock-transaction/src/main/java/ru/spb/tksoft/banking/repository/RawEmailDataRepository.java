package ru.spb.tksoft.banking.repository;

import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.spb.tksoft.banking.entity.RawEmailDataEntity;

/**
 * Repository of RawEmailDataEntity.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Repository
public interface RawEmailDataRepository extends JpaRepository<RawEmailDataEntity, Long> {

    /**
     * @return Set of RawEmailDataEntity by userId.
     */
    @Query(nativeQuery = true, value =
            "SELECT * FROM \"email_data\" d WHERE d.user_id = :userId")
    Set<RawEmailDataEntity> findByUserId(long userId);

    /**
     * @return RawEmailDataEntity.
     */
    @Query(nativeQuery = true, value =
            "SELECT * FROM \"email_data\" d WHERE d.email = :email")
    Optional<RawEmailDataEntity> findEmailExact(String email);

    /**
     * @return Contacts count for userId.
     */
    @Query(nativeQuery = true, value =
            "SELECT COUNT(id) FROM \"email_data\" d WHERE d.user_id = :userId")
    int countContacts(long userId);
}
