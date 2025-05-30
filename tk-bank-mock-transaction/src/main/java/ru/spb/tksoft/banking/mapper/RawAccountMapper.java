package ru.spb.tksoft.banking.mapper;

import javax.annotation.concurrent.ThreadSafe;
import jakarta.validation.constraints.NotNull;
import ru.spb.tksoft.banking.dto.RawAccountDto;
import ru.spb.tksoft.banking.entity.RawAccountEntity;

/**
 * Mapper for RawAccount*.
 *
 * Converting {@code DTO from/to entity}.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@ThreadSafe
public final class RawAccountMapper {

    private RawAccountMapper() {}

    /**
     * Entity to DTO.
     * 
     * @param entity user entity.
     * @return user DTO.
     */
    @NotNull
    public static RawAccountDto toDto(@NotNull final RawAccountEntity entity) {

        return new RawAccountDto(entity.getId(),
                entity.getUserId(),
                entity.getBalance(), entity.getBalanceAutoLimit());
    }
}
