package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.OrdarDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Ordar}.
 */
public interface OrdarService {

    /**
     * Save a ordar.
     *
     * @param ordarDTO the entity to save.
     * @return the persisted entity.
     */
    OrdarDTO save(OrdarDTO ordarDTO);

    /**
     * Get all the ordars.
     *
     * @return the list of entities.
     */
    List<OrdarDTO> findAll();

    /**
     * Get all the ordars with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    Page<OrdarDTO> findAllWithEagerRelationships(Pageable pageable);


    /**
     * Get the "id" ordar.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrdarDTO> findOne(Long id);

    /**
     * Delete the "id" ordar.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
