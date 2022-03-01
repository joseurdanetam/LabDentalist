package com.labdentalist.app.service;

import com.labdentalist.app.domain.Cita;
import com.labdentalist.app.domain.Cliente;
import com.labdentalist.app.service.dto.CitaDTO;
import java.time.Instant;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.labdentalist.app.domain.Cita}.
 */
public interface CitaService {
    /**
     * Save a cita.
     *
     * @param citaDTO the entity to save.
     * @return the persisted entity.
     */
    CitaDTO save(CitaDTO citaDTO);

    /**
     * Partially updates a cita.
     *
     * @param citaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CitaDTO> partialUpdate(CitaDTO citaDTO);

    /**
     * Get all the citas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CitaDTO> findAll(Pageable pageable);

    /**
     * Get the "id" cita.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CitaDTO> findOne(Long id);

    /**
     * Delete the "id" cita.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Page<Cita> findAllByFechaCitaGreaterThanEqual(Instant now, Pageable pageable);

    Page<CitaDTO> buscarCitas(String filtro, Pageable pageable);

    Page<Cita> findAllByClienteId(Long id, Pageable pageable);
}
