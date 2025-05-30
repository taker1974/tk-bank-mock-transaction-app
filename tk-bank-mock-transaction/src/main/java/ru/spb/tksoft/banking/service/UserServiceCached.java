package ru.spb.tksoft.banking.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import ru.spb.tksoft.banking.dto.UserDto;
import ru.spb.tksoft.banking.mapper.UserMapper;
import ru.spb.tksoft.banking.repository.UserRepository;
import ru.spb.tksoft.banking.tools.PageTools;
import ru.spb.tksoft.utils.log.LogEx;
import ru.spb.tksoft.banking.entity.UserEntity;

/**
 * User service. Cached methods.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Service
@RequiredArgsConstructor
public class UserServiceCached {

    private final Logger log = LoggerFactory.getLogger(UserServiceCached.class);

    @NotNull
    private final UserRepository userRepository;

    /** Clear caches. */
    @CacheEvict(value = "user", allEntries = true)
    public void clearCaches() {
        // ...
    }

    /**
     * Find users by name%.
     * 
     * @return Paginated list of users.
     */
    @Cacheable(value = "user", unless = "#result.isEmpty()",
            key = "{#namePrefix, #pageable.pageNumber, #pageable.pageSize, #pageable.sort}")
    @NotNull
    public Page<UserDto> findUsersByNameLike(
            final String namePrefix,
            final Pageable pageable) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        List<UserEntity> entities = userRepository.findByNameLike(namePrefix);
        List<UserDto> dtos = new ArrayList<>(entities.size());

        entities.stream().forEach(
                entity -> dtos.add(
                        UserMapper.toDto(entity)));

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return PageTools.convertListToPage(dtos, pageable);
    }

    /**
     * Find users by date of birth equal and after.
     * 
     * @return Paginated list of users.
     */
    @Cacheable(value = "user", unless = "#result.isEmpty()",
            key = "{#dateOfBirth.toString(), #pageable.pageNumber, #pageable.pageSize, #pageable.sort}")
    @NotNull
    public Page<UserDto> findUsersByDateOfBirth(
            final LocalDate dateOfBirth,
            final Pageable pageable) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        List<UserEntity> entities = userRepository.findByBirthDateEqualAndAfter(dateOfBirth);
        List<UserDto> dtos = new ArrayList<>(entities.size());

        entities.stream().forEach(
                entity -> dtos.add(
                        UserMapper.toDto(entity)));

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return PageTools.convertListToPage(dtos, pageable);
    }

    /**
     * Find users by email exact match.
     * 
     * @return DTO.
     */
    @Cacheable(value = "user", unless = "#result.isEmpty()", key = "#email")
    @NotNull
    public UserDto findUsersByEmailExact(final String email) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        UserEntity entity = userRepository.findByEMailExact(email)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User with given email not found: " + email));

        UserDto dto = UserMapper.toDto(entity);

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return dto;
    }

    /**
     * Find users by phone exact match.
     * 
     * @return DTO.
     */
    @Cacheable(value = "user", unless = "#result.isEmpty()", key = "#phone")
    @NotNull
    public UserDto findUsersByPhoneExact(final String phone) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        UserEntity entity = userRepository.findByPhoneExact(phone)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User with given phone not found: " + phone));

        UserDto dto = UserMapper.toDto(entity);

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return dto;
    }
}
