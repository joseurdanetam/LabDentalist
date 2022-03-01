package com.labdentalist.app.service;

import com.labdentalist.app.domain.Intervencion;
import com.labdentalist.app.service.dto.IntervencionDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.labdentalist.app.domain.Intervencion}.
 */
public interface IntervencionService {
    /**
     * Save a intervencion.
     *
     * @param intervencionDTO the entity to save.
     * @return the persisted entity.
     */
    IntervencionDTO save(IntervencionDTO intervencionDTO);

    /**
     * Partially updates a intervencion.
     *
     * @param intervencionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<IntervencionDTO> partialUpdate(IntervencionDTO intervencionDTO);

    /**
     * Get all the intervencions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<IntervencionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" intervencion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<IntervencionDTO> findOne(Long id);

    /**
     * Delete the "id" intervencion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get the "id" intervencion.
     *
     * @param id the id of the entity.
     */
    List<Intervencion> findAllByFacturaId(Long id);
}
