package com.labdentalist.app.web.rest;

import com.labdentalist.app.domain.Intervencion;
import com.labdentalist.app.repository.IntervencionRepository;
import com.labdentalist.app.service.IntervencionService;
import com.labdentalist.app.service.dto.IntervencionDTO;
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
 * REST controller for managing {@link com.labdentalist.app.domain.Intervencion}.
 */
@RestController
@RequestMapping("/api")
public class IntervencionResource {

    private final Logger log = LoggerFactory.getLogger(IntervencionResource.class);

    private static final String ENTITY_NAME = "intervencion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IntervencionService intervencionService;

    private final IntervencionRepository intervencionRepository;

    public IntervencionResource(IntervencionService intervencionService, IntervencionRepository intervencionRepository) {
        this.intervencionService = intervencionService;
        this.intervencionRepository = intervencionRepository;
    }

    /**
     * {@code POST  /intervencions} : Create a new intervencion.
     *
     * @param intervencionDTO the intervencionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new intervencionDTO, or with status {@code 400 (Bad Request)} if the intervencion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/intervencions")
    public ResponseEntity<IntervencionDTO> createIntervencion(@RequestBody IntervencionDTO intervencionDTO) throws URISyntaxException {
        log.debug("REST request to save Intervencion : {}", intervencionDTO);
        if (intervencionDTO.getId() != null) {
            throw new BadRequestAlertException("A new intervencion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IntervencionDTO result = intervencionService.save(intervencionDTO);
        return ResponseEntity
            .created(new URI("/api/intervencions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /intervencions/:id} : Updates an existing intervencion.
     *
     * @param id the id of the intervencionDTO to save.
     * @param intervencionDTO the intervencionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated intervencionDTO,
     * or with status {@code 400 (Bad Request)} if the intervencionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the intervencionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/intervencions/{id}")
    public ResponseEntity<IntervencionDTO> updateIntervencion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IntervencionDTO intervencionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Intervencion : {}, {}", id, intervencionDTO);
        if (intervencionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, intervencionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!intervencionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        IntervencionDTO result = intervencionService.save(intervencionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, intervencionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /intervencions/:id} : Partial updates given fields of an existing intervencion, field will ignore if it is null
     *
     * @param id the id of the intervencionDTO to save.
     * @param intervencionDTO the intervencionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated intervencionDTO,
     * or with status {@code 400 (Bad Request)} if the intervencionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the intervencionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the intervencionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/intervencions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IntervencionDTO> partialUpdateIntervencion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IntervencionDTO intervencionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Intervencion partially : {}, {}", id, intervencionDTO);
        if (intervencionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, intervencionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!intervencionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IntervencionDTO> result = intervencionService.partialUpdate(intervencionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, intervencionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /intervencions} : get all the intervencions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of intervencions in body.
     */
    @GetMapping("/intervencions")
    public ResponseEntity<List<IntervencionDTO>> getAllIntervencions(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Intervencions");
        Page<IntervencionDTO> page = intervencionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /intervencions/:id} : get the "id" intervencion.
     *
     * @param id the id of the intervencionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the intervencionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/intervencions/{id}")
    public ResponseEntity<IntervencionDTO> getIntervencion(@PathVariable Long id) {
        log.debug("REST request to get Intervencion : {}", id);
        Optional<IntervencionDTO> intervencionDTO = intervencionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(intervencionDTO);
    }

    /**
     * {@code DELETE  /intervencions/:id} : delete the "id" intervencion.
     *
     * @param id the id of the intervencionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/intervencions/{id}")
    public ResponseEntity<Void> deleteIntervencion(@PathVariable Long id) {
        log.debug("REST request to delete Intervencion : {}", id);
        intervencionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/intervencions/get-factura-intervencion")
    public ResponseEntity<List<Intervencion>> findAllByFacturaId(@RequestParam Long id) {
        log.debug("REST request to get a page of Intervencions");
        List<Intervencion> intervenciones = intervencionService.findAllByFacturaId(id);
        return ResponseEntity.ok().body(intervenciones);
    }
}
