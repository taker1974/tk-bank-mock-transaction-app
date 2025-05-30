package ru.spb.tksoft.banking.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.spb.tksoft.banking.entity.UserEntity;

/**
 * Repository of UserEntity.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    /**
     * @return Paginated list of UserEntity by user's name%.
     */
    @Query(value = "SELECT u FROM UserEntity u WHERE u.name LIKE :name%")
    List<UserEntity> findByNameLike(@Param("name") String name);

    /**
     * @return Paginated list of UserEntity where user's birth date >= given value.
     */
    @Query(value = "SELECT u FROM UserEntity u WHERE u.dateOfBirth >= :dateOfBirth")
    List<UserEntity> findByBirthDateEqualAndAfter(@Param("dateOfBirth") LocalDate dateOfBirth);

    /**
     * @return UserEntity by user's exact name.
     */ 
    @Query(nativeQuery = true, value = "SELECT * FROM \"user\" u WHERE u.name = ':name' LIMIT 1")
    Optional<UserEntity> findByNameExact();

    /**
     * @return UserEntity by exact phone number.
     */
    @Query(value = """
            SELECT u.*
            FROM "user" u
            JOIN phone_data d ON u.id = d.user_id
            WHERE d.phone = :phone LIMIT 1
            """, nativeQuery = true)
    Optional<UserEntity> findByPhoneExact(@Param ("phone") String phone);

    /**
     * @return UserEntity by exact email.
     */
    @Query(value = """
            SELECT u.*
            FROM "user" u
            JOIN email_data d ON u.id = d.user_id
            WHERE d.email = :email LIMIT 1
            """, nativeQuery = true)
    Optional<UserEntity> findByEMailExact(@Param("email") String email);
}
