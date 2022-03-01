package com.labdentalist.app.repository;

import com.labdentalist.app.domain.Cita;
import com.labdentalist.app.domain.Cliente;
import java.time.Instant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Cita entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CitaRepository extends JpaRepository<Cita, Long>, JpaSpecificationExecutor<Cita> {
    Page<Cita> findAllByFechaCitaGreaterThanEqual(Instant now, Pageable pageable);

    Page<Cita> findAllByClienteId(Long id, Pageable pageable);
}
