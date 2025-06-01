package ru.spb.tksoft.banking.service;

import java.time.LocalDate;
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
import ru.spb.tksoft.banking.controller.JwtUser;
import ru.spb.tksoft.banking.dto.UserDto;
import ru.spb.tksoft.banking.mapper.UserMapper;
import ru.spb.tksoft.banking.repository.UserRepository;
import ru.spb.tksoft.banking.tools.PageTools;
import ru.spb.tksoft.utils.log.LogEx;
import ru.spb.tksoft.banking.entity.EmailDataEntity;
import ru.spb.tksoft.banking.entity.UserContact;
import ru.spb.tksoft.banking.entity.UserContacts;
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

    private UserEntity getUserById(final long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User with given id not found: " + userId));
    }

    @CacheEvict(value = "user", allEntries = true)
    @NotNull
    private UserDto addContact(final JwtUser user,
            final UserContacts contacts, final UserContact contact) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        UserEntity userEntity = getUserById(user.userId());
        contacts.addContact(contact);
        UserDto dto = UserMapper.toDto(userRepository.save(userEntity));

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
        UserEntity userEntity = getUserById(user.userId());
        return addContact(user,
                userEntity.getEmailContacts(), new EmailDataEntity(userEntity, email));
    }

    /**
     * Add phone to user.
     * 
     * @return DTO.
     */
    @CacheEvict(value = "user", allEntries = true)
    @NotNull
    public UserDto addPhone(final JwtUser user, final String phone) {
        UserEntity userEntity = getUserById(user.userId());
        return addContact(user,
                userEntity.getPhoneContacts(), new EmailDataEntity(userEntity, phone));
    }

    @CacheEvict(value = "user", allEntries = true)
    @NotNull
    private UserDto removeContact(final JwtUser user,
            final UserContacts contacts, final long contactId) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        UserEntity userEntity = getUserById(user.userId());
        contacts.removeContact(contactId);
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
        UserEntity userEntity = getUserById(user.userId());
        return removeContact(user,
                userEntity.getEmailContacts(), emailId);
    }

    /**
     * Remove phone from user.
     * 
     * @return DTO.
     */
    @CacheEvict(value = "user", allEntries = true)
    @NotNull
    public UserDto removePhone(final JwtUser user, final long phoneId) {
        UserEntity userEntity = getUserById(user.userId());
        return removeContact(user,
                userEntity.getPhoneContacts(), phoneId);
    }

    @CacheEvict(value = "user", allEntries = true)
    @NotNull
    private UserDto updateContact(final JwtUser user,
            final UserContacts contacts, final UserContact contactUpdated) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        UserEntity userEntity = getUserById(user.userId());
        contacts.updateContact(contactUpdated);
        UserDto dto = UserMapper.toDto(userRepository.save(userEntity));

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return dto;
    }

    /**
     * Update email.
     * 
     * @return DTO.
     */
    @CacheEvict(value = "user", allEntries = true)
    @NotNull
    public UserDto updateEmail(final JwtUser user,
            final long emailId, final String newEmail) {

        UserEntity userEntity = getUserById(user.userId());
        return updateContact(user,
                userEntity.getEmailContacts(), new EmailDataEntity(userEntity, newEmail));
    }

    /**
     * Update phone.
     * 
     * @return DTO.
     */
    @CacheEvict(value = "user", allEntries = true)
    @NotNull
    public UserDto updatePhone(final JwtUser user,
            final long phoneId, final String newPhone) {

        UserEntity userEntity = getUserById(user.userId());
        return updateContact(user,
                userEntity.getPhoneContacts(), new EmailDataEntity(userEntity, newPhone));
    }
}
