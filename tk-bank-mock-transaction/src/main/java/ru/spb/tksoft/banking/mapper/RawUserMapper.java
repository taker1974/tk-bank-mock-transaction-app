package ru.spb.tksoft.banking.mapper;

import javax.annotation.concurrent.ThreadSafe;
import jakarta.validation.constraints.NotNull;
import ru.spb.tksoft.banking.dto.user.raw.RawUserDto;
import ru.spb.tksoft.banking.entity.raw.RawUserEntity;

/**
 * Mapper for RawUser*.
 *
 * Converting {@code DTO from/to entity}.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@ThreadSafe
public final class RawUserMapper {

    private RawUserMapper() {}

    /**
     * Entity to DTO.
     * 
     * @param entity user entity.
     * @return user DTO.
     */
    @NotNull
    public static RawUserDto toDto(@NotNull final RawUserEntity entity) {

        return new RawUserDto(entity.getId(),
                entity.getName(), entity.getPassword(),
                entity.getDateOfBirth());
    }
}
