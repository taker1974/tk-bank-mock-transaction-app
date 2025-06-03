package ru.spb.tksoft.banking.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.concurrent.ThreadSafe;
import io.jsonwebtoken.lang.Collections;
import jakarta.validation.constraints.NotNull;
import ru.spb.tksoft.banking.dto.RawContactItemDto;
import ru.spb.tksoft.banking.dto.RawContactListDto;
import ru.spb.tksoft.banking.dto.RawUserDto;
import ru.spb.tksoft.banking.entity.RawUserEntity;
import ru.spb.tksoft.banking.entity.UserContact;

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
     * RawUserEntity to DTO.
     * 
     * @param entity Entity.
     * @return DTO.
     */
    @NotNull
    public static RawUserDto toDto(@NotNull final RawUserEntity entity) {

        return new RawUserDto(entity.getId(),
                entity.getName(), entity.getPassword(),
                entity.getDateOfBirth());
    }

    /** E-Mails title. */
    public static final String TITLE_EMAILS = "E-mails";

    /**
     * Set of UserContact to DTO.
     *
     * @param entities Set of entities.
     * @return DTO.
     */
    @NotNull
    public static RawContactListDto toDto(@NotNull final long userId,
            @NotNull final Set<UserContact> entities) {

        if (entities.isEmpty()) {
            return new RawContactListDto(TITLE_EMAILS, userId, Collections.emptySet());
        }

        return new RawContactListDto(TITLE_EMAILS, userId,
                entities.stream()
                        .map(e -> new RawContactItemDto(e.getContactId(), e.getContactValue()))
                        .collect(Collectors.toSet()));
    }

    /**
     * Single UserContact to DTO.
     *
     * @param contact UserContact.
     * @return DTO.
     */
    @NotNull
    public static RawContactItemDto toDto(@NotNull final UserContact contact) {

        return new RawContactItemDto(contact.getContactId(), contact.getContactValue());
    }
}
