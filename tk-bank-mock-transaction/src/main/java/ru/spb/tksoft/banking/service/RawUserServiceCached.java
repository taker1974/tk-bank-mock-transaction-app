package ru.spb.tksoft.banking.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.lang.Collections;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import ru.spb.tksoft.banking.controller.JwtUser;
import ru.spb.tksoft.banking.dto.RawContactItemDto;
import ru.spb.tksoft.banking.dto.RawContactListDto;
import ru.spb.tksoft.banking.dto.RawUserDto;
import ru.spb.tksoft.banking.dto.UserDto;
import ru.spb.tksoft.banking.entity.RawEmailDataEntity;
import ru.spb.tksoft.banking.entity.RawPhoneDataEntity;
import ru.spb.tksoft.banking.entity.RawUserEntity;
import ru.spb.tksoft.banking.entity.UserContact;
import ru.spb.tksoft.banking.entity.UserContacts;
import ru.spb.tksoft.banking.exception.AlreadyExistsException;
import ru.spb.tksoft.banking.mapper.RawUserMapper;
import ru.spb.tksoft.banking.mapper.UserMapper;
import ru.spb.tksoft.banking.repository.RawEmailDataRepository;
import ru.spb.tksoft.banking.repository.RawPhoneDataRepository;
import ru.spb.tksoft.banking.repository.RawUserRepository;
import ru.spb.tksoft.banking.tools.PageTools;
import ru.spb.tksoft.utils.log.LogEx;

/**
 * User service for raw "user" data. Cached methods.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Service
@RequiredArgsConstructor
public class RawUserServiceCached {

    private final Logger log = LoggerFactory.getLogger(RawUserServiceCached.class);

    @NotNull
    private final RawUserRepository rawUserRepository;

    @NotNull
    private final RawEmailDataRepository rawEmailDataRepository;

    @NotNull
    private final RawPhoneDataRepository rawPhoneDataRepository;

    @NotNull
    private final CacheManager cacheManager;

    private void clearCache(String name) {

        Cache cache = cacheManager.getCache(name);
        if (cache != null) {
            cache.clear();
        }
    }

    /** Clear caches. */
    public void clearCaches() {

        clearCache("user");
        clearCache("userByEmail");
        clearCache("userByPhone");
        clearCache("userById");

        clearCache("userEmails");
        clearCache("userPhones");
    }

    /**
     * Find users by email exact match.
     * 
     * @return DTO.
     */
    @Cacheable(value = "userByEmail", unless = "#result.isEmpty()", key = "#email")
    @NotNull
    public RawUserDto findUserByEmailExact(final String email) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        RawUserEntity entity = rawUserRepository.findOneByEmailExact(email)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User with given email not found"));

        RawUserDto dto = RawUserMapper.toDto(entity);

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return dto;
    }

    /**
     * Find users by phone exact match.
     * 
     * @return DTO.
     */
    @Cacheable(value = "userByPhone", unless = "#result.isEmpty()", key = "#phone")
    @NotNull
    public RawUserDto findUserByPhoneExact(final String phone) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        RawUserEntity entity = rawUserRepository.findOneByPhoneExact(phone)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User with given phone not found"));

        RawUserDto dto = RawUserMapper.toDto(entity);

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return dto;
    }

    private RawUserEntity getUserById(final long userId) {

        var cache = cacheManager.getCache("userById");
        if (cache != null) {
            ValueWrapper value = cache.get(userId);
            if (value != null) {
                return (RawUserEntity) value.get();
            }
        }

        RawUserEntity result = rawUserRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User with given id not found"));

        if (cache != null) {
            cache.put(userId, result);
        }
        return result;
    }

    /**
     * Get all emails for given user.
     * 
     * @param user JwtUser.
     * @return DTO.
     */
    @Cacheable(value = "userEmails", key = "#user.userId()")
    public RawContactListDto getUserEmails(final JwtUser user) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        long userId = user.userId();
        Set<UserContact> emails = rawEmailDataRepository.findByUserId(userId)
                .stream().collect(Collectors.toSet());

        return RawUserMapper.toDto(userId, emails);
    }

    /**
     * Get all phones for given user.
     * 
     * @param user JwtUser.
     * @return DTO.
     */
    @Cacheable(value = "userPhones", key = "#user.userId()")
    public RawContactListDto getUserPhones(final JwtUser user) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        long userId = user.userId();
        Set<UserContact> phones = rawPhoneDataRepository.findByUserId(userId)
                .stream().collect(Collectors.toSet());

        return RawUserMapper.toDto(userId, phones);
    }

    /**
     * Add email to user.
     * 
     * @return DTO.
     */
    @CacheEvict(value = "userEmails", allEntries = true)
    @NotNull
    public RawContactItemDto addEmail(final JwtUser user, final String email) {

        long userId = user.userId();
        Optional<RawEmailDataEntity> entity = rawEmailDataRepository.findEmailExact(email);
        if (entity.isPresent()) {
            throw new AlreadyExistsException("Email already exists");
        }

        RawEmailDataEntity newEmail = new RawEmailDataEntity(userId, email);
        return RawUserMapper.toDto(rawEmailDataRepository.save(newEmail));
    }

    /**
     * Add phone to user.
     * 
     * @return DTO.
     */
    @CacheEvict(value = "userPhones", allEntries = true)
    @NotNull
    public RawContactItemDto addPhone(final JwtUser user, final String phone) {

        long userId = user.userId();
        Optional<RawPhoneDataEntity> entity = rawPhoneDataRepository.findPhoneExact(phone);
        if (entity.isPresent()) {
            throw new AlreadyExistsException("Phone already exists");
        }

        var newPhone = new RawPhoneDataEntity(userId, phone);
        return RawUserMapper.toDto(rawPhoneDataRepository.save(newPhone));
    }
}
