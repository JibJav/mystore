package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.StoreDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Store}.
 */
public interface StoreService {

    /**
     * Save a store.
     *
     * @param storeDTO the entity to save.
     * @return the persisted entity.
     */
    StoreDTO save(StoreDTO storeDTO);

    /**
     * Get all the stores.
     *
     * @return the list of entities.
     */
    List<StoreDTO> findAll();


    /**
     * Get the "id" store.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StoreDTO> findOne(Long id);

    /**
     * Delete the "id" store.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
