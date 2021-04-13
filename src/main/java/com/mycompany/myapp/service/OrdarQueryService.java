package com.mycompany.myapp.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.mycompany.myapp.domain.Ordar;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.repository.OrdarRepository;
import com.mycompany.myapp.service.dto.OrdarCriteria;
import com.mycompany.myapp.service.dto.OrdarDTO;
import com.mycompany.myapp.service.mapper.OrdarMapper;

/**
 * Service for executing complex queries for {@link Ordar} entities in the database.
 * The main input is a {@link OrdarCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OrdarDTO} or a {@link Page} of {@link OrdarDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrdarQueryService extends QueryService<Ordar> {

    private final Logger log = LoggerFactory.getLogger(OrdarQueryService.class);

    private final OrdarRepository ordarRepository;

    private final OrdarMapper ordarMapper;

    public OrdarQueryService(OrdarRepository ordarRepository, OrdarMapper ordarMapper) {
        this.ordarRepository = ordarRepository;
        this.ordarMapper = ordarMapper;
    }

    /**
     * Return a {@link List} of {@link OrdarDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrdarDTO> findByCriteria(OrdarCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Ordar> specification = createSpecification(criteria);
        return ordarMapper.toDto(ordarRepository.findByUserIsCurrentUser());
    }

    /**
     * Return a {@link Page} of {@link OrdarDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrdarDTO> findByCriteria(OrdarCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Ordar> specification = createSpecification(criteria);
        return ordarRepository.findAll(specification, page)
            .map(ordarMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrdarCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Ordar> specification = createSpecification(criteria);
        return ordarRepository.count(specification);
    }

    /**
     * Function to convert {@link OrdarCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Ordar> createSpecification(OrdarCriteria criteria) {
        Specification<Ordar> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Ordar_.id));
            }
            if (criteria.getOrdarwithname() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOrdarwithname(), Ordar_.ordarwithname));
            }
            if (criteria.getOrdarId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrdarId(), Ordar_.ordarId));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Ordar_.status));
            }
            if (criteria.getOrdarDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrdarDate(), Ordar_.ordarDate));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), Ordar_.amount));
            }
            if (criteria.getTotalItems() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalItems(), Ordar_.totalItems));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(Ordar_.user, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getProductId() != null) {
                specification = specification.and(buildSpecification(criteria.getProductId(),
                    root -> root.join(Ordar_.products, JoinType.LEFT).get(Product_.id)));
            }
            if (criteria.getStoreId() != null) {
                specification = specification.and(buildSpecification(criteria.getStoreId(),
                    root -> root.join(Ordar_.store, JoinType.LEFT).get(Store_.id)));
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(buildSpecification(criteria.getCustomerId(),
                    root -> root.join(Ordar_.customer, JoinType.LEFT).get(Customer_.id)));
            }
        }
        return specification;
    }
}
