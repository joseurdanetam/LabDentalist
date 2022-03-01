package com.labdentalist.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.labdentalist.app.IntegrationTest;
import com.labdentalist.app.domain.Intervencion;
import com.labdentalist.app.repository.IntervencionRepository;
import com.labdentalist.app.service.dto.IntervencionDTO;
import com.labdentalist.app.service.mapper.IntervencionMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link IntervencionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IntervencionResourceIT {

    private static final String DEFAULT_TITULO = "AAAAAAAAAA";
    private static final String UPDATED_TITULO = "BBBBBBBBBB";

    private static final Double DEFAULT_PRECIO_UNITARIO = 1D;
    private static final Double UPDATED_PRECIO_UNITARIO = 2D;

    private static final String ENTITY_API_URL = "/api/intervencions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IntervencionRepository intervencionRepository;

    @Autowired
    private IntervencionMapper intervencionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIntervencionMockMvc;

    private Intervencion intervencion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Intervencion createEntity(EntityManager em) {
        Intervencion intervencion = new Intervencion().titulo(DEFAULT_TITULO).precioUnitario(DEFAULT_PRECIO_UNITARIO);
        return intervencion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Intervencion createUpdatedEntity(EntityManager em) {
        Intervencion intervencion = new Intervencion().titulo(UPDATED_TITULO).precioUnitario(UPDATED_PRECIO_UNITARIO);
        return intervencion;
    }

    @BeforeEach
    public void initTest() {
        intervencion = createEntity(em);
    }

    @Test
    @Transactional
    void createIntervencion() throws Exception {
        int databaseSizeBeforeCreate = intervencionRepository.findAll().size();
        // Create the Intervencion
        IntervencionDTO intervencionDTO = intervencionMapper.toDto(intervencion);
        restIntervencionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(intervencionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Intervencion in the database
        List<Intervencion> intervencionList = intervencionRepository.findAll();
        assertThat(intervencionList).hasSize(databaseSizeBeforeCreate + 1);
        Intervencion testIntervencion = intervencionList.get(intervencionList.size() - 1);
        assertThat(testIntervencion.getTitulo()).isEqualTo(DEFAULT_TITULO);
        assertThat(testIntervencion.getPrecioUnitario()).isEqualTo(DEFAULT_PRECIO_UNITARIO);
    }

    @Test
    @Transactional
    void createIntervencionWithExistingId() throws Exception {
        // Create the Intervencion with an existing ID
        intervencion.setId(1L);
        IntervencionDTO intervencionDTO = intervencionMapper.toDto(intervencion);

        int databaseSizeBeforeCreate = intervencionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIntervencionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(intervencionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intervencion in the database
        List<Intervencion> intervencionList = intervencionRepository.findAll();
        assertThat(intervencionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllIntervencions() throws Exception {
        // Initialize the database
        intervencionRepository.saveAndFlush(intervencion);

        // Get all the intervencionList
        restIntervencionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(intervencion.getId().intValue())))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].precioUnitario").value(hasItem(DEFAULT_PRECIO_UNITARIO.doubleValue())));
    }

    @Test
    @Transactional
    void getIntervencion() throws Exception {
        // Initialize the database
        intervencionRepository.saveAndFlush(intervencion);

        // Get the intervencion
        restIntervencionMockMvc
            .perform(get(ENTITY_API_URL_ID, intervencion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(intervencion.getId().intValue()))
            .andExpect(jsonPath("$.titulo").value(DEFAULT_TITULO))
            .andExpect(jsonPath("$.precioUnitario").value(DEFAULT_PRECIO_UNITARIO.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingIntervencion() throws Exception {
        // Get the intervencion
        restIntervencionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewIntervencion() throws Exception {
        // Initialize the database
        intervencionRepository.saveAndFlush(intervencion);

        int databaseSizeBeforeUpdate = intervencionRepository.findAll().size();

        // Update the intervencion
        Intervencion updatedIntervencion = intervencionRepository.findById(intervencion.getId()).get();
        // Disconnect from session so that the updates on updatedIntervencion are not directly saved in db
        em.detach(updatedIntervencion);
        updatedIntervencion.titulo(UPDATED_TITULO).precioUnitario(UPDATED_PRECIO_UNITARIO);
        IntervencionDTO intervencionDTO = intervencionMapper.toDto(updatedIntervencion);

        restIntervencionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, intervencionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(intervencionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Intervencion in the database
        List<Intervencion> intervencionList = intervencionRepository.findAll();
        assertThat(intervencionList).hasSize(databaseSizeBeforeUpdate);
        Intervencion testIntervencion = intervencionList.get(intervencionList.size() - 1);
        assertThat(testIntervencion.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testIntervencion.getPrecioUnitario()).isEqualTo(UPDATED_PRECIO_UNITARIO);
    }

    @Test
    @Transactional
    void putNonExistingIntervencion() throws Exception {
        int databaseSizeBeforeUpdate = intervencionRepository.findAll().size();
        intervencion.setId(count.incrementAndGet());

        // Create the Intervencion
        IntervencionDTO intervencionDTO = intervencionMapper.toDto(intervencion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIntervencionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, intervencionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(intervencionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intervencion in the database
        List<Intervencion> intervencionList = intervencionRepository.findAll();
        assertThat(intervencionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIntervencion() throws Exception {
        int databaseSizeBeforeUpdate = intervencionRepository.findAll().size();
        intervencion.setId(count.incrementAndGet());

        // Create the Intervencion
        IntervencionDTO intervencionDTO = intervencionMapper.toDto(intervencion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIntervencionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(intervencionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intervencion in the database
        List<Intervencion> intervencionList = intervencionRepository.findAll();
        assertThat(intervencionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIntervencion() throws Exception {
        int databaseSizeBeforeUpdate = intervencionRepository.findAll().size();
        intervencion.setId(count.incrementAndGet());

        // Create the Intervencion
        IntervencionDTO intervencionDTO = intervencionMapper.toDto(intervencion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIntervencionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(intervencionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Intervencion in the database
        List<Intervencion> intervencionList = intervencionRepository.findAll();
        assertThat(intervencionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIntervencionWithPatch() throws Exception {
        // Initialize the database
        intervencionRepository.saveAndFlush(intervencion);

        int databaseSizeBeforeUpdate = intervencionRepository.findAll().size();

        // Update the intervencion using partial update
        Intervencion partialUpdatedIntervencion = new Intervencion();
        partialUpdatedIntervencion.setId(intervencion.getId());

        partialUpdatedIntervencion.precioUnitario(UPDATED_PRECIO_UNITARIO);

        restIntervencionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIntervencion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIntervencion))
            )
            .andExpect(status().isOk());

        // Validate the Intervencion in the database
        List<Intervencion> intervencionList = intervencionRepository.findAll();
        assertThat(intervencionList).hasSize(databaseSizeBeforeUpdate);
        Intervencion testIntervencion = intervencionList.get(intervencionList.size() - 1);
        assertThat(testIntervencion.getTitulo()).isEqualTo(DEFAULT_TITULO);
        assertThat(testIntervencion.getPrecioUnitario()).isEqualTo(UPDATED_PRECIO_UNITARIO);
    }

    @Test
    @Transactional
    void fullUpdateIntervencionWithPatch() throws Exception {
        // Initialize the database
        intervencionRepository.saveAndFlush(intervencion);

        int databaseSizeBeforeUpdate = intervencionRepository.findAll().size();

        // Update the intervencion using partial update
        Intervencion partialUpdatedIntervencion = new Intervencion();
        partialUpdatedIntervencion.setId(intervencion.getId());

        partialUpdatedIntervencion.titulo(UPDATED_TITULO).precioUnitario(UPDATED_PRECIO_UNITARIO);

        restIntervencionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIntervencion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIntervencion))
            )
            .andExpect(status().isOk());

        // Validate the Intervencion in the database
        List<Intervencion> intervencionList = intervencionRepository.findAll();
        assertThat(intervencionList).hasSize(databaseSizeBeforeUpdate);
        Intervencion testIntervencion = intervencionList.get(intervencionList.size() - 1);
        assertThat(testIntervencion.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testIntervencion.getPrecioUnitario()).isEqualTo(UPDATED_PRECIO_UNITARIO);
    }

    @Test
    @Transactional
    void patchNonExistingIntervencion() throws Exception {
        int databaseSizeBeforeUpdate = intervencionRepository.findAll().size();
        intervencion.setId(count.incrementAndGet());

        // Create the Intervencion
        IntervencionDTO intervencionDTO = intervencionMapper.toDto(intervencion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIntervencionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, intervencionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(intervencionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intervencion in the database
        List<Intervencion> intervencionList = intervencionRepository.findAll();
        assertThat(intervencionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIntervencion() throws Exception {
        int databaseSizeBeforeUpdate = intervencionRepository.findAll().size();
        intervencion.setId(count.incrementAndGet());

        // Create the Intervencion
        IntervencionDTO intervencionDTO = intervencionMapper.toDto(intervencion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIntervencionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(intervencionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intervencion in the database
        List<Intervencion> intervencionList = intervencionRepository.findAll();
        assertThat(intervencionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIntervencion() throws Exception {
        int databaseSizeBeforeUpdate = intervencionRepository.findAll().size();
        intervencion.setId(count.incrementAndGet());

        // Create the Intervencion
        IntervencionDTO intervencionDTO = intervencionMapper.toDto(intervencion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIntervencionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(intervencionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Intervencion in the database
        List<Intervencion> intervencionList = intervencionRepository.findAll();
        assertThat(intervencionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIntervencion() throws Exception {
        // Initialize the database
        intervencionRepository.saveAndFlush(intervencion);

        int databaseSizeBeforeDelete = intervencionRepository.findAll().size();

        // Delete the intervencion
        restIntervencionMockMvc
            .perform(delete(ENTITY_API_URL_ID, intervencion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Intervencion> intervencionList = intervencionRepository.findAll();
        assertThat(intervencionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
