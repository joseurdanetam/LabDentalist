package com.labdentalist.app.service;

import com.labdentalist.app.domain.Receta;
import com.labdentalist.app.service.dto.RecetaDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.labdentalist.app.domain.Receta}.
 */
public interface RecetaService {
    /**
     * Save a receta.
     *
     * @param recetaDTO the entity to save.
     * @return the persisted entity.
     */
    RecetaDTO save(RecetaDTO recetaDTO);

    /**
     * Partially updates a receta.
     *
     * @param recetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RecetaDTO> partialUpdate(RecetaDTO recetaDTO);

    /**
     * Get all the recetas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RecetaDTO> findAll(Pageable pageable);

    /**
     * Get the "id" receta.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RecetaDTO> findOne(Long id);

    /**
     * Delete the "id" receta.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get recetas by id of cliente.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Page<Receta> searchRecetaById(Long id, Pageable pageable);

    Page<RecetaDTO> busquedaRecetas(String filtro, Pageable pageable);
}
