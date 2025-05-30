package ru.spb.tksoft.banking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
     * @return Paginated list of RawEmailDataEntity by userId.
     */
    @Query(value = "SELECT d FROM RawEmailDataEntity d WHERE d.userId = :userId")
    Page<RawEmailDataEntity> findByUserId(@Param("userId") long userId, Pageable pageable);
}
