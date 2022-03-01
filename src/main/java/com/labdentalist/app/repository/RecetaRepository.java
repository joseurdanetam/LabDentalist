package com.labdentalist.app.repository;

import com.labdentalist.app.domain.Receta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Receta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecetaRepository extends JpaRepository<Receta, Long>, JpaSpecificationExecutor<Receta> {
    Page<Receta> findAllByClienteId(Long id, Pageable pageable);
}
