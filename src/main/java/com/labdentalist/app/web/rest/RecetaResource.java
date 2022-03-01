package com.labdentalist.app.web.rest;

import com.labdentalist.app.repository.RecetaRepository;
import com.labdentalist.app.service.RecetaService;
import com.labdentalist.app.service.dto.RecetaDTO;
import com.labdentalist.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.labdentalist.app.domain.Receta}.
 */
@RestController
@RequestMapping("/api")
public class RecetaResource {

    private final Logger log = LoggerFactory.getLogger(RecetaResource.class);

    private static final String ENTITY_NAME = "receta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RecetaService recetaService;

    private final RecetaRepository recetaRepository;

    public RecetaResource(RecetaService recetaService, RecetaRepository recetaRepository) {
        this.recetaService = recetaService;
        this.recetaRepository = recetaRepository;
    }

    /**
     * {@code POST  /recetas} : Create a new receta.
     *
     * @param recetaDTO the recetaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new recetaDTO, or with status {@code 400 (Bad Request)} if the receta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/recetas")
    public ResponseEntity<RecetaDTO> createReceta(@RequestBody RecetaDTO recetaDTO) throws URISyntaxException {
        log.debug("REST request to save Receta : {}", recetaDTO);
        if (recetaDTO.getId() != null) {
            throw new BadRequestAlertException("A new receta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RecetaDTO result = recetaService.save(recetaDTO);
        return ResponseEntity
            .created(new URI("/api/recetas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /recetas/:id} : Updates an existing receta.
     *
     * @param id the id of the recetaDTO to save.
     * @param recetaDTO the recetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recetaDTO,
     * or with status {@code 400 (Bad Request)} if the recetaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the recetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/recetas/{id}")
    public ResponseEntity<RecetaDTO> updateReceta(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RecetaDTO recetaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Receta : {}, {}", id, recetaDTO);
        if (recetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RecetaDTO result = recetaService.save(recetaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recetaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /recetas/:id} : Partial updates given fields of an existing receta, field will ignore if it is null
     *
     * @param id the id of the recetaDTO to save.
     * @param recetaDTO the recetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recetaDTO,
     * or with status {@code 400 (Bad Request)} if the recetaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the recetaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the recetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/recetas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RecetaDTO> partialUpdateReceta(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RecetaDTO recetaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Receta partially : {}, {}", id, recetaDTO);
        if (recetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RecetaDTO> result = recetaService.partialUpdate(recetaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recetaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /recetas} : get all the recetas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of recetas in body.
     */
    @GetMapping("/recetas")
    public ResponseEntity<List<RecetaDTO>> getAllRecetas(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Recetas");
        Page<RecetaDTO> page = recetaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /recetas/:id} : get the "id" receta.
     *
     * @param id the id of the recetaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the recetaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/recetas/{id}")
    public ResponseEntity<RecetaDTO> getReceta(@PathVariable Long id) {
        log.debug("REST request to get Receta : {}", id);
        Optional<RecetaDTO> recetaDTO = recetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(recetaDTO);
    }

    /**
     * {@code DELETE  /recetas/:id} : delete the "id" receta.
     *
     * @param id the id of the recetaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/recetas/{id}")
    public ResponseEntity<Void> deleteReceta(@PathVariable Long id) {
        log.debug("REST request to delete Receta : {}", id);
        recetaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/recetas/busqueda-recetas")
    public ResponseEntity<List<RecetaDTO>> busquedaCliente(
        Pageable pageable,
        @RequestParam(required = false, defaultValue = "") String filtro
    ) {
        filtro = !filtro.equals("undefined") ? filtro : "%";
        Page<RecetaDTO> page = recetaService.busquedaRecetas(filtro, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
