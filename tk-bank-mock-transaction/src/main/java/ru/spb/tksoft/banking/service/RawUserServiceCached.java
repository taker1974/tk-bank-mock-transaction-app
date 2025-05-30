package ru.spb.tksoft.banking.service;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import ru.spb.tksoft.banking.dto.RawUserDto;
import ru.spb.tksoft.banking.entity.RawUserEntity;
import ru.spb.tksoft.banking.mapper.RawUserMapper;
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

    /** Clear caches. */
    @CacheEvict(value = "user", allEntries = true)
    public void clearCaches() {
        // ...
    }

    /**
     * Get all users.
     * 
     * @return Paginated list of users.
     */
    @Cacheable(value = "user", unless = "#result.isEmpty()")
    @NotNull
    public Page<RawUserDto> getAllUsers(Pageable pageable) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        List<RawUserEntity> entities = rawUserRepository.findAll();
        List<RawUserDto> dtos = new ArrayList<>(entities.size());

        entities.stream().forEach(
                entity -> dtos.add(
                        RawUserMapper.toDto(entity)));

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return PageTools.convertListToPage(dtos, pageable);
    }
}
