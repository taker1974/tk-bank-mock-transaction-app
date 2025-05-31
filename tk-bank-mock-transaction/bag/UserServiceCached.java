package ru.spb.tksoft.banking.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import ru.spb.tksoft.banking.controller.JwtUser;
import ru.spb.tksoft.banking.dto.UserDto;
import ru.spb.tksoft.banking.mapper.UserMapper;
import ru.spb.tksoft.banking.repository.UserRepository;
import ru.spb.tksoft.banking.tools.PageTools;
import ru.spb.tksoft.utils.log.LogEx;
import ru.spb.tksoft.banking.entity.EmailDataEntity;
import ru.spb.tksoft.banking.entity.PhoneDataEntity;
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

    @NotNull
    private final AuthServiceCached authServiceCached;

    /** Clear caches. */
    @CacheEvict(value = "user", allEntries = true)
    public void clearCaches() {
        // ...
    }

    /**
     * Find all users.
     * 
     * @param pageable Pagination settings.
     * @return Paginated list of users.
     */
    public Page<UserDto> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserMapper::toDto);
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

        List<UserDto> dtos = userRepository.findByNameLike(namePrefix).stream()
                .map(UserMapper::toDto).toList();

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

        List<UserDto> dtos = userRepository.findByBirthDateEqualAndAfter(dateOfBirth).stream()
                .map(UserMapper::toDto).toList();

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

    /**
     * Add email to user.
     * 
     * @return DTO.
     */
    @CacheEvict(value = "user", allEntries = true)
    @NotNull
    public UserDto addEmail(final JwtUser user, final String email) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        UserEntity userEntity = userRepository.findById(user.userId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "User with given credentials not found: " + user.email()));

        if (!isValidEMail(email)) {
            throw new IllegalArgumentException("Email is invalid: " + email);
        }

        if (userEntity.getEmails().stream().anyMatch(e -> e.getEmail().equals(email))) {
            throw new IllegalArgumentException("Email exists: " + email);
        }

        userEntity.getEmails().add(new EmailDataEntity(userEntity, email));
        UserDto dto = UserMapper.toDto(userRepository.save(userEntity));

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return dto;
    }


    /**
     * Add phone to user.
     * 
     * @return DTO.
     */
    @CacheEvict(value = "user", allEntries = true)
    @NotNull
    public UserDto addPhone(final JwtUser user, final String phone) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        UserEntity userEntity = userRepository.findById(user.userId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "User with given credentials not found: " + user.email()));


        if (userEntity.getPhones().stream().anyMatch(e -> e.getPhone().equals(phone))) {
            throw new IllegalArgumentException("Phone exists: " + phone);
        }

        userEntity.getPhones().add(new PhoneDataEntity(userEntity, phone));
        UserDto dto = UserMapper.toDto(userRepository.save(userEntity));

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return dto;
    }

    /**
     * Remove email from user.
     * 
     * @return DTO.
     */
    @CacheEvict(value = "user", allEntries = true)
    @NotNull
    public UserDto removeEmail(final JwtUser user, final long emailId) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        UserEntity userEntity = userRepository.findById(user.userId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "User with given credentials not found: " + user.email()));

        if (userEntity.getEmails().size() <= 1) {
            throw new IllegalArgumentException("Cannot remove the only phone number");
        }

        EmailDataEntity emailEntity = userEntity.getEmails().stream()
                .filter(e -> e.getId().equals(emailId)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Email with given ID not exists: " + emailId));

        userEntity.getEmails().remove(emailEntity);
        UserDto dto = UserMapper.toDto(userRepository.save(userEntity));

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return dto;
    }

    /**
     * Remove phone from user.
     * 
     * @return DTO.
     */
    @CacheEvict(value = "user", allEntries = true)
    @NotNull
    public UserDto removePhone(final JwtUser user, final long phoneId) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        UserEntity userEntity = userRepository.findById(user.userId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "User with given credentials not found: " + user.email()));

        if (userEntity.getPhones().size() <= 1) {
            throw new IllegalArgumentException("Cannot remove the only phone number");
        }

        EmailDataEntity emailEntity = userEntity.getEmails().stream()
                .filter(e -> e.getId().equals(emailId)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Email with given ID not exists: " + emailId));

        userEntity.getEmails().remove(emailEntity);
        UserDto dto = UserMapper.toDto(userRepository.save(userEntity));

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return dto;
    }
}
