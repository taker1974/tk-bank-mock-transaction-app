package ru.spb.tksoft.banking.repository.raw;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.spb.tksoft.banking.entity.raw.RawUserEntity;

/**
 * Repository of RawUserEntity.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Repository
public interface RawUserRepository extends JpaRepository<RawUserEntity, Long> {

    /**
     * @return List of all RawUserEntity.
     */
    @Query(nativeQuery = true, value = "SELECT * FROM \"user\" u")
    List<RawUserEntity> findAllRaw();

    /**
     * @return Paginated list of RawUserEntity by user's name%.
     */
    @Query(value = "SELECT u FROM RawUserEntity u WHERE u.name LIKE :name%")
    Page<RawUserEntity> findByNameLike(@Param("name") String name, Pageable pageable);

    /**
     * @return Paginated list of RawUserEntity where user's birth date >= given value.
     */
    @Query(value = "SELECT u FROM RawUserEntity u WHERE u.dateOfBirth >= :dateOfBirth")
    Page<RawUserEntity> findByBirthDateEqualAndAfter(@Param("dateOfBirth") LocalDate dateOfBirth,
            Pageable pageable);

    /**
     * @return UserEntity by user's exact name.
     */
    @Query(nativeQuery = true, value = "SELECT * FROM \"user\" u WHERE u.name = ':name' LIMIT 1")
    Optional<RawUserEntity> findByNameExact();
}
