package ru.spb.tksoft.banking.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.spb.tksoft.banking.entity.RawUserEntity;

/**
 * Repository of RawUserEntity.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Repository
public interface RawUserRepository extends JpaRepository<RawUserEntity, Long> {

    /**
     * RawUserEntity by user's email. Version with JOIN.
     * 
     * @return RawUserEntity or empty.
     */
    @Query(nativeQuery = true, value = """
            SELECT u.* FROM "user" u
            JOIN email_data d ON u.id = d.user_id
            WHERE d.email = :email LIMIT 1""")
    Optional<RawUserEntity> findOneByEmailExact(String email);

    /**
     * RawUserEntity by user's phone. Version with subquery.
     * 
     * @return RawUserEntity or empty.
     */
    @Query(nativeQuery = true, value = """
            SELECT u.*
            FROM "user" u
            WHERE u.id = (
                SELECT user_id
                FROM "phone_data" d
                WHERE phone = :phone
                LIMIT 1)
            """)
    Optional<RawUserEntity> findOneByPhoneExact(String phone);

    /**
     * @return Set of all RawUserEntity (for testing/management purposes).
     */
    @Query(nativeQuery = true,
            value = "SELECT * FROM \"user\" u")
    Page<RawUserEntity> findAllRaw(Pageable pageable);

    /**
     * @return Paginated list of RawUserEntity by user's name%.
     */
    @Query(value = "SELECT u FROM RawUserEntity u WHERE u.name LIKE :name%")
    Page<RawUserEntity> findByNameLike(String name, Pageable pageable);

    /**
     * @return Paginated list of RawUserEntity where user's birth date >= given value.
     */
    @Query(value = "SELECT u FROM RawUserEntity u WHERE u.dateOfBirth >= :dateOfBirth")
    Page<RawUserEntity> findByBirthDateEqualAndAfter(LocalDate dateOfBirth, Pageable pageable);

    /**
     * @return RawUserEntity by user's exact name.
     */
    @Query(nativeQuery = true,
            value = "SELECT * FROM \"user\" u WHERE u.name = ':name' LIMIT 1")
    Optional<RawUserEntity> findOneByNameExact(String name);
}
