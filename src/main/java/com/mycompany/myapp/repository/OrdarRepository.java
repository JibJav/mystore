package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Ordar;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Ordar entity.
 */
@Repository
public interface OrdarRepository extends JpaRepository<Ordar, Long>, JpaSpecificationExecutor<Ordar> {

    @Query("select ordar from Ordar ordar where ordar.user.login = ?#{principal.username}")
    List<Ordar> findByUserIsCurrentUser();
    
    @Query("select ordar from Ordar ordar where ordar.user.login = ?#{principal.username}")
    List<Ordar> findByUserIsCurrentUser(Specification<Ordar> specification );

    @Query(value = "select distinct ordar from Ordar ordar left join fetch ordar.products",
        countQuery = "select count(distinct ordar) from Ordar ordar")
    Page<Ordar> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct ordar from Ordar ordar left join fetch ordar.products")
    List<Ordar> findAllWithEagerRelationships();

    @Query("select ordar from Ordar ordar left join fetch ordar.products where ordar.id =:id")
    Optional<Ordar> findOneWithEagerRelationships(@Param("id") Long id);
}
