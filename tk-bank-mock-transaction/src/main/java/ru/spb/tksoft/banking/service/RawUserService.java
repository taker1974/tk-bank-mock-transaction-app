package ru.spb.tksoft.banking.service;

import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import ru.spb.tksoft.banking.dto.RawUserDto;
import ru.spb.tksoft.banking.mapper.RawUserMapper;
import ru.spb.tksoft.banking.repository.RawUserRepository;
import ru.spb.tksoft.banking.tools.PageTools;
import ru.spb.tksoft.utils.log.LogEx;

/**
 * User service for raw "user" data.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Service
@RequiredArgsConstructor
public class RawUserService {

    private final Logger log = LoggerFactory.getLogger(RawUserService.class);

    @NotNull
    private final RawUserRepository rawUserRepository;

    /**
     * Get all users.
     * 
     * @return Paginated list of users.
     */
    @NotNull
    public Page<RawUserDto> getAllUsers(Pageable pageable) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        List<RawUserDto> dtos = rawUserRepository.findAll().stream()
                .map(RawUserMapper::toDto).toList();

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return PageTools.convertListToPage(dtos, pageable);
    }

    /**
     * Find users by name%.
     * 
     * @return Paginated list of users.
     */
    @NotNull
    public Page<RawUserDto> findUsersByNameLike(
            final String namePrefix,
            final Pageable pageable) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        List<RawUserDto> dtos = rawUserRepository.findByNameLike(namePrefix, pageable).stream()
                .map(RawUserMapper::toDto).toList();

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return PageTools.convertListToPage(dtos, pageable);
    }

    /**
     * Find users by date of birth equal and after.
     * 
     * @return Paginated list of users.
     */
    @NotNull
    public Page<RawUserDto> findUsersByDateOfBirth(
            final LocalDate dateOfBirth,
            final Pageable pageable) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        List<RawUserDto> dtos =
                rawUserRepository.findByBirthDateEqualAndAfter(dateOfBirth, pageable).stream()
                        .map(RawUserMapper::toDto).toList();

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return PageTools.convertListToPage(dtos, pageable);
    }
}
