package ru.spb.tksoft.banking.repository;

import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.spb.tksoft.banking.entity.RawPhoneDataEntity;

/**
 * Repository of RawPhoneDataEntity.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Repository
public interface RawPhoneDataRepository extends JpaRepository<RawPhoneDataEntity, Long> {

    /**
     * @return Set of RawPhoneDataEntity by userId.
     */
    @Query(nativeQuery = true, value =
            "SELECT * FROM \"phone_data\" d WHERE d.user_id = :userId")
    Set<RawPhoneDataEntity> findByUserId(long userId);

    /**
     * @return RawPhoneDataEntity.
     */
    @Query(nativeQuery = true, value =
            "SELECT * FROM \"phone_data\" d WHERE d.phone = :phone")
    Optional<RawPhoneDataEntity> findPhoneExact(String phone);

    /**
     * @return Contacts count for userId.
     */
    @Query(nativeQuery = true, value =
            "SELECT COUNT(id) FROM \"phone_data\" d WHERE d.user_id = :userId")
    int countContacts(long userId);
}
