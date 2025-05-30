package ru.spb.tksoft.banking.mapper;

import javax.annotation.concurrent.ThreadSafe;
import jakarta.validation.constraints.NotNull;
import ru.spb.tksoft.banking.dto.UserDto;
import ru.spb.tksoft.banking.entity.UserEntity;

/**
 * Mapper for User*.
 *
 * Converting {@code DTO from/to entity}.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@ThreadSafe
public final class UserMapper {

    private UserMapper() {}

    /**
     * Entity to DTO.
     * 
     * @param entity user entity.
     * @return user DTO.
     */
    @NotNull
    public static UserDto toDto(@NotNull final UserEntity entity) {

        return new UserDto(
                entity.getId(),
                entity.getName(),
                entity.getPassword(),
                entity.getDateOfBirth(),
                entity.getAccount().getBalance(),
                entity.getEmails().stream().map(e -> e.getEmail()).toList(),
                entity.getPhones().stream().map(e -> e.getPhone()).toList());
    }
}
