package ru.spb.tksoft.banking.repository.raw;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.spb.tksoft.banking.entity.raw.RawPhoneDataEntity;

/**
 * Repository of RawPhoneDataEntity.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Repository
public interface RawPhoneDataRepository extends JpaRepository<RawPhoneDataEntity, Long> {

    /**
     * @return Paginated list of RawPhoneDataEntity by userId.
     */
    @Query(value = "SELECT d FROM RawPhoneDataEntity d WHERE d.userId = :userId")
    Page<RawPhoneDataEntity> findByUserId(@Param("userId") long userId, Pageable pageable);
}
