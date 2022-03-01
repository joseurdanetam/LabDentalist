package com.labdentalist.app.web.rest;

import com.labdentalist.app.domain.Cita;
import com.labdentalist.app.domain.Cliente;
import com.labdentalist.app.domain.Factura;
import com.labdentalist.app.domain.Receta;
import com.labdentalist.app.repository.HistorialRepository;
import com.labdentalist.app.service.CitaService;
import com.labdentalist.app.service.FacturaService;
import com.labdentalist.app.service.HistorialService;
import com.labdentalist.app.service.RecetaService;
import com.labdentalist.app.service.dto.HistorialDTO;
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
 * REST controller for managing {@link com.labdentalist.app.domain.Historial}.
 */
@RestController
@RequestMapping("/api")
public class HistorialResource {

    private final Logger log = LoggerFactory.getLogger(HistorialResource.class);

    private static final String ENTITY_NAME = "historial";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HistorialService historialService;

    private final HistorialRepository historialRepository;

    private final RecetaService recetaService;

    private final FacturaService facturaService;

    private final CitaService citaService;

    public HistorialResource(
        HistorialService historialService,
        HistorialRepository historialRepository,
        CitaService citaService,
        FacturaService facturaService,
        RecetaService recetaService
    ) {
        this.historialService = historialService;
        this.historialRepository = historialRepository;
        this.citaService = citaService;
        this.facturaService = facturaService;
        this.recetaService = recetaService;
    }

    /**
     * {@code POST  /historials} : Create a new historial.
     *
     * @param historialDTO the historialDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new historialDTO, or with status {@code 400 (Bad Request)} if the historial has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/historials")
    public ResponseEntity<HistorialDTO> createHistorial(@RequestBody HistorialDTO historialDTO) throws URISyntaxException {
        log.debug("REST request to save Historial : {}", historialDTO);
        if (historialDTO.getId() != null) {
            throw new BadRequestAlertException("A new historial cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HistorialDTO result = historialService.save(historialDTO);
        return ResponseEntity
            .created(new URI("/api/historials/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /historials/:id} : Updates an existing historial.
     *
     * @param id the id of the historialDTO to save.
     * @param historialDTO the historialDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historialDTO,
     * or with status {@code 400 (Bad Request)} if the historialDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the historialDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/historials/{id}")
    public ResponseEntity<HistorialDTO> updateHistorial(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HistorialDTO historialDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Historial : {}, {}", id, historialDTO);
        if (historialDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historialDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!historialRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HistorialDTO result = historialService.save(historialDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, historialDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /historials/:id} : Partial updates given fields of an existing historial, field will ignore if it is null
     *
     * @param id the id of the historialDTO to save.
     * @param historialDTO the historialDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historialDTO,
     * or with status {@code 400 (Bad Request)} if the historialDTO is not valid,
     * or with status {@code 404 (Not Found)} if the historialDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the historialDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/historials/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HistorialDTO> partialUpdateHistorial(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HistorialDTO historialDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Historial partially : {}, {}", id, historialDTO);
        if (historialDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historialDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!historialRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HistorialDTO> result = historialService.partialUpdate(historialDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, historialDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /historials} : get all the historials.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of historials in body.
     */
    @GetMapping("/historials")
    public ResponseEntity<List<HistorialDTO>> getAllHistorials(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Historials");
        Page<HistorialDTO> page = historialService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /historials} : get all the historials.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of historials in body.
     */
    @GetMapping("/historials/getfactura")
    public ResponseEntity<List<Factura>> findAllByClienteIdFactura(
        @RequestParam Long id,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of Historials");
        Page<Factura> page = facturaService.findAllByClienteId(id, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /historials/:id} : get the "id" historial.
     *
     * @param id the id of the historialDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the historialDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/historials/{id}")
    public ResponseEntity<HistorialDTO> getHistorial(@PathVariable Long id) {
        log.debug("REST request to get Historial : {}", id);
        Optional<HistorialDTO> historialDTO = historialService.findOne(id);
        return ResponseUtil.wrapOrNotFound(historialDTO);
    }

    /**
     * {@code DELETE  /historials/:id} : delete the "id" historial.
     *
     * @param id the id of the historialDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/historials/{id}")
    public ResponseEntity<Void> deleteHistorial(@PathVariable Long id) {
        log.debug("REST request to delete Historial : {}", id);
        historialService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/historials/getrecetas")
    public ResponseEntity<List<Receta>> searchRecetaById(@RequestParam Long id, Pageable pageable) {
        log.debug("REST request to get a page of Recetas");
        Page<Receta> recetas = recetaService.searchRecetaById(id, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), recetas);
        return ResponseEntity.ok().headers(headers).body(recetas.getContent());
    }

    @GetMapping("/historials/getcitas")
    public ResponseEntity<List<Cita>> findAllByClienteId(@RequestParam(required = false) Long id, Pageable pageable) {
        //Long idCliente = Long.parseLong(id);
        Page<Cita> page = citaService.findAllByClienteId(id, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
