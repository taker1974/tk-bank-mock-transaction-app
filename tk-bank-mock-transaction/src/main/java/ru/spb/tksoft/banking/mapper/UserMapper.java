package ru.spb.tksoft.banking.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.concurrent.ThreadSafe;
import jakarta.validation.constraints.NotNull;
import ru.spb.tksoft.banking.dto.UserDto;
import ru.spb.tksoft.banking.entity.RawAccountEntity;
import ru.spb.tksoft.banking.entity.RawEmailDataEntity;
import ru.spb.tksoft.banking.entity.RawPhoneDataEntity;
import ru.spb.tksoft.banking.entity.RawUserEntity;

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
     * @param userEntity User entity.
     * @param accountEntity Account entity.
     * @param emailDataEntities Email entities.
     * @param phoneDataEntities Phone entities.
     * @return user DTO.
     */
    @NotNull
    public static UserDto toDto(@NotNull final RawUserEntity userEntity,
            @NotNull final RawAccountEntity accountEntity,
            @NotNull final Set<RawEmailDataEntity> emailDataEntities,
            @NotNull final Set<RawPhoneDataEntity> phoneDataEntities) {

        return new UserDto(
                userEntity.getId(),
                userEntity.getName(),
                userEntity.getPassword(),
                userEntity.getDateOfBirth(),
                accountEntity.getBalance(),
                emailDataEntities.stream()
                        .map(RawEmailDataEntity::getContactValue).collect(Collectors.toSet()),
                phoneDataEntities.stream()
                        .map(RawPhoneDataEntity::getContactValue).collect(Collectors.toSet()));
    }
}
