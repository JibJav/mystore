package com.mycompany.myapp.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.myapp.repository.OrdarRepository;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.service.OrdarQueryService;
import com.mycompany.myapp.service.OrdarService;
import com.mycompany.myapp.service.dto.OrdarCriteria;
import com.mycompany.myapp.service.dto.OrdarDTO;
import com.mycompany.myapp.service.mapper.OrdarMapper;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.micrometer.core.annotation.Timed;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Ordar}.
 */
@RestController
@RequestMapping("/api")
public class OrdarResource {

    private final Logger log = LoggerFactory.getLogger(OrdarResource.class);

    private static final String ENTITY_NAME = "ordar";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrdarService ordarService;

    private final OrdarQueryService ordarQueryService;
    

    private final OrdarMapper ordarMapper;

    private final OrdarRepository ordarRepository;

    public OrdarResource(OrdarService ordarService, OrdarQueryService ordarQueryService,OrdarMapper ordarMapper,OrdarRepository ordarRepository) {
        this.ordarService = ordarService;
        this.ordarQueryService = ordarQueryService;
        
        this.ordarMapper = ordarMapper;
        this.ordarRepository = ordarRepository;
    }

    /**
     * {@code POST  /ordars} : Create a new ordar.
     *
     * @param ordarDTO the ordarDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ordarDTO, or with status {@code 400 (Bad Request)} if the ordar has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ordars")
    public ResponseEntity<OrdarDTO> createOrdar(@Valid @RequestBody OrdarDTO ordarDTO) throws URISyntaxException {
        log.debug("REST request to save Ordar : {}", ordarDTO);
        if (ordarDTO.getId() != null) {
            throw new BadRequestAlertException("A new ordar cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrdarDTO result = ordarService.save(ordarDTO);
        return ResponseEntity.created(new URI("/api/ordars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ordars} : Updates an existing ordar.
     *
     * @param ordarDTO the ordarDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordarDTO,
     * or with status {@code 400 (Bad Request)} if the ordarDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ordarDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ordars")
    public ResponseEntity<OrdarDTO> updateOrdar(@Valid @RequestBody OrdarDTO ordarDTO) throws URISyntaxException {
        log.debug("REST request to update Ordar : {}", ordarDTO);
        if (ordarDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OrdarDTO result = ordarService.save(ordarDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ordarDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /ordars} : get all the ordars.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ordars in body.
     */
    @GetMapping("/ordars")
    public ResponseEntity<List<OrdarDTO>> getAllOrdars(OrdarCriteria criteria) {
        log.debug("REST request to get Ordars by criteria: {}", criteria);
        List<OrdarDTO> entityList = ordarQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /ordars/count} : count all the ordars.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/ordars/count")
    public ResponseEntity<Long> countOrdars(OrdarCriteria criteria) {
        log.debug("REST request to count Ordars by criteria: {}", criteria);
        return ResponseEntity.ok().body(ordarQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ordars/:id} : get the "id" ordar.
     *
     * @param id the id of the ordarDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ordarDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ordars/{id}")
    public ResponseEntity<OrdarDTO> getOrdar(@PathVariable Long id) {
        log.debug("REST request to get Ordar : {}", id);
        Optional<OrdarDTO> ordarDTO = ordarService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ordarDTO);
    }
    
    
//    @GetMapping("/ordars/{id}")
//    @Timed
//    public ResponseEntity<OrdarDTO> getOrdar(@PathVariable Long id) {
//        log.debug("REST request to get BankAccount : {}", id);
//        Optional<OrdarDTO> bankAccountDTO = ordarRepository.findById(id)
//            .map(ordarMapper::toDto);
//
//        // Return 404 if the entity is not owned by the connected user
//        Optional<String> userLogin = SecurityUtils.getCurrentUserLogin();
//        if (bankAccountDTO.isPresent() &&
//            userLogin.isPresent() &&
//            userLogin.get().equals(bankAccountDTO.get().getUserLogin())) {
//            return ResponseUtil.wrapOrNotFound(bankAccountDTO);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
    

    /**
     * {@code DELETE  /ordars/:id} : delete the "id" ordar.
     *
     * @param id the id of the ordarDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ordars/{id}")
    public ResponseEntity<Void> deleteOrdar(@PathVariable Long id) {
        log.debug("REST request to delete Ordar : {}", id);
        ordarService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
