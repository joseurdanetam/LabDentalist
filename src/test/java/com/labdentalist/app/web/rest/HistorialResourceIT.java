package com.labdentalist.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.labdentalist.app.IntegrationTest;
import com.labdentalist.app.domain.Historial;
import com.labdentalist.app.repository.HistorialRepository;
import com.labdentalist.app.service.dto.HistorialDTO;
import com.labdentalist.app.service.mapper.HistorialMapper;
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
 * Integration tests for the {@link HistorialResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HistorialResourceIT {

    private static final String ENTITY_API_URL = "/api/historials";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HistorialRepository historialRepository;

    @Autowired
    private HistorialMapper historialMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHistorialMockMvc;

    private Historial historial;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Historial createEntity(EntityManager em) {
        Historial historial = new Historial();
        return historial;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Historial createUpdatedEntity(EntityManager em) {
        Historial historial = new Historial();
        return historial;
    }

    @BeforeEach
    public void initTest() {
        historial = createEntity(em);
    }

    @Test
    @Transactional
    void createHistorial() throws Exception {
        int databaseSizeBeforeCreate = historialRepository.findAll().size();
        // Create the Historial
        HistorialDTO historialDTO = historialMapper.toDto(historial);
        restHistorialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(historialDTO)))
            .andExpect(status().isCreated());

        // Validate the Historial in the database
        List<Historial> historialList = historialRepository.findAll();
        assertThat(historialList).hasSize(databaseSizeBeforeCreate + 1);
        Historial testHistorial = historialList.get(historialList.size() - 1);
    }

    @Test
    @Transactional
    void createHistorialWithExistingId() throws Exception {
        // Create the Historial with an existing ID
        historial.setId(1L);
        HistorialDTO historialDTO = historialMapper.toDto(historial);

        int databaseSizeBeforeCreate = historialRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHistorialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(historialDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Historial in the database
        List<Historial> historialList = historialRepository.findAll();
        assertThat(historialList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllHistorials() throws Exception {
        // Initialize the database
        historialRepository.saveAndFlush(historial);

        // Get all the historialList
        restHistorialMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historial.getId().intValue())));
    }

    @Test
    @Transactional
    void getHistorial() throws Exception {
        // Initialize the database
        historialRepository.saveAndFlush(historial);

        // Get the historial
        restHistorialMockMvc
            .perform(get(ENTITY_API_URL_ID, historial.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(historial.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingHistorial() throws Exception {
        // Get the historial
        restHistorialMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHistorial() throws Exception {
        // Initialize the database
        historialRepository.saveAndFlush(historial);

        int databaseSizeBeforeUpdate = historialRepository.findAll().size();

        // Update the historial
        Historial updatedHistorial = historialRepository.findById(historial.getId()).get();
        // Disconnect from session so that the updates on updatedHistorial are not directly saved in db
        em.detach(updatedHistorial);
        HistorialDTO historialDTO = historialMapper.toDto(updatedHistorial);

        restHistorialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, historialDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historialDTO))
            )
            .andExpect(status().isOk());

        // Validate the Historial in the database
        List<Historial> historialList = historialRepository.findAll();
        assertThat(historialList).hasSize(databaseSizeBeforeUpdate);
        Historial testHistorial = historialList.get(historialList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingHistorial() throws Exception {
        int databaseSizeBeforeUpdate = historialRepository.findAll().size();
        historial.setId(count.incrementAndGet());

        // Create the Historial
        HistorialDTO historialDTO = historialMapper.toDto(historial);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistorialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, historialDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Historial in the database
        List<Historial> historialList = historialRepository.findAll();
        assertThat(historialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHistorial() throws Exception {
        int databaseSizeBeforeUpdate = historialRepository.findAll().size();
        historial.setId(count.incrementAndGet());

        // Create the Historial
        HistorialDTO historialDTO = historialMapper.toDto(historial);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistorialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Historial in the database
        List<Historial> historialList = historialRepository.findAll();
        assertThat(historialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHistorial() throws Exception {
        int databaseSizeBeforeUpdate = historialRepository.findAll().size();
        historial.setId(count.incrementAndGet());

        // Create the Historial
        HistorialDTO historialDTO = historialMapper.toDto(historial);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistorialMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(historialDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Historial in the database
        List<Historial> historialList = historialRepository.findAll();
        assertThat(historialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHistorialWithPatch() throws Exception {
        // Initialize the database
        historialRepository.saveAndFlush(historial);

        int databaseSizeBeforeUpdate = historialRepository.findAll().size();

        // Update the historial using partial update
        Historial partialUpdatedHistorial = new Historial();
        partialUpdatedHistorial.setId(historial.getId());

        restHistorialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistorial.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHistorial))
            )
            .andExpect(status().isOk());

        // Validate the Historial in the database
        List<Historial> historialList = historialRepository.findAll();
        assertThat(historialList).hasSize(databaseSizeBeforeUpdate);
        Historial testHistorial = historialList.get(historialList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateHistorialWithPatch() throws Exception {
        // Initialize the database
        historialRepository.saveAndFlush(historial);

        int databaseSizeBeforeUpdate = historialRepository.findAll().size();

        // Update the historial using partial update
        Historial partialUpdatedHistorial = new Historial();
        partialUpdatedHistorial.setId(historial.getId());

        restHistorialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistorial.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHistorial))
            )
            .andExpect(status().isOk());

        // Validate the Historial in the database
        List<Historial> historialList = historialRepository.findAll();
        assertThat(historialList).hasSize(databaseSizeBeforeUpdate);
        Historial testHistorial = historialList.get(historialList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingHistorial() throws Exception {
        int databaseSizeBeforeUpdate = historialRepository.findAll().size();
        historial.setId(count.incrementAndGet());

        // Create the Historial
        HistorialDTO historialDTO = historialMapper.toDto(historial);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistorialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, historialDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(historialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Historial in the database
        List<Historial> historialList = historialRepository.findAll();
        assertThat(historialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHistorial() throws Exception {
        int databaseSizeBeforeUpdate = historialRepository.findAll().size();
        historial.setId(count.incrementAndGet());

        // Create the Historial
        HistorialDTO historialDTO = historialMapper.toDto(historial);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistorialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(historialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Historial in the database
        List<Historial> historialList = historialRepository.findAll();
        assertThat(historialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHistorial() throws Exception {
        int databaseSizeBeforeUpdate = historialRepository.findAll().size();
        historial.setId(count.incrementAndGet());

        // Create the Historial
        HistorialDTO historialDTO = historialMapper.toDto(historial);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistorialMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(historialDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Historial in the database
        List<Historial> historialList = historialRepository.findAll();
        assertThat(historialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHistorial() throws Exception {
        // Initialize the database
        historialRepository.saveAndFlush(historial);

        int databaseSizeBeforeDelete = historialRepository.findAll().size();

        // Delete the historial
        restHistorialMockMvc
            .perform(delete(ENTITY_API_URL_ID, historial.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Historial> historialList = historialRepository.findAll();
        assertThat(historialList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
