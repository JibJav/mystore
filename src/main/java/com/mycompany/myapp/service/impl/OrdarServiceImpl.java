package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.OrdarService;
import com.mycompany.myapp.domain.Ordar;
import com.mycompany.myapp.repository.OrdarRepository;
import com.mycompany.myapp.service.dto.OrdarDTO;
import com.mycompany.myapp.service.mapper.OrdarMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Ordar}.
 */
@Service
@Transactional
public class OrdarServiceImpl implements OrdarService {

    private final Logger log = LoggerFactory.getLogger(OrdarServiceImpl.class);

    private final OrdarRepository ordarRepository;

    private final OrdarMapper ordarMapper;

    public OrdarServiceImpl(OrdarRepository ordarRepository, OrdarMapper ordarMapper) {
        this.ordarRepository = ordarRepository;
        this.ordarMapper = ordarMapper;
    }

    @Override
    public OrdarDTO save(OrdarDTO ordarDTO) {
        log.debug("Request to save Ordar : {}", ordarDTO);
        Ordar ordar = ordarMapper.toEntity(ordarDTO);
        ordar = ordarRepository.save(ordar);
        return ordarMapper.toDto(ordar);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdarDTO> findAll() {
        log.debug("Request to get all Ordars");
        return ordarRepository.findAllWithEagerRelationships().stream()
            .map(ordarMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    public Page<OrdarDTO> findAllWithEagerRelationships(Pageable pageable) {
        return ordarRepository.findAllWithEagerRelationships(pageable).map(ordarMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrdarDTO> findOne(Long id) {
        log.debug("Request to get Ordar : {}", id);
        return ordarRepository.findOneWithEagerRelationships(id)
            .map(ordarMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ordar : {}", id);
        ordarRepository.deleteById(id);
    }
}
