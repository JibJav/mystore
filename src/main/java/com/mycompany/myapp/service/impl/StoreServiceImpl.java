package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.StoreService;
import com.mycompany.myapp.domain.Store;
import com.mycompany.myapp.repository.StoreRepository;
import com.mycompany.myapp.service.dto.StoreDTO;
import com.mycompany.myapp.service.mapper.StoreMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Store}.
 */
@Service
@Transactional
public class StoreServiceImpl implements StoreService {

    private final Logger log = LoggerFactory.getLogger(StoreServiceImpl.class);

    private final StoreRepository storeRepository;

    private final StoreMapper storeMapper;

    public StoreServiceImpl(StoreRepository storeRepository, StoreMapper storeMapper) {
        this.storeRepository = storeRepository;
        this.storeMapper = storeMapper;
    }

    @Override
    public StoreDTO save(StoreDTO storeDTO) {
        log.debug("Request to save Store : {}", storeDTO);
        Store store = storeMapper.toEntity(storeDTO);
        store = storeRepository.save(store);
        return storeMapper.toDto(store);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StoreDTO> findAll() {
        log.debug("Request to get all Stores");
        return storeRepository.findAll().stream()
            .map(storeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<StoreDTO> findOne(Long id) {
        log.debug("Request to get Store : {}", id);
        return storeRepository.findById(id)
            .map(storeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Store : {}", id);
        storeRepository.deleteById(id);
    }
}
