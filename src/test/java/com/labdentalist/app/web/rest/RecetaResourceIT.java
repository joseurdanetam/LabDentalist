package com.labdentalist.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.labdentalist.app.IntegrationTest;
import com.labdentalist.app.domain.Receta;
import com.labdentalist.app.repository.RecetaRepository;
import com.labdentalist.app.service.dto.RecetaDTO;
import com.labdentalist.app.service.mapper.RecetaMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link RecetaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RecetaResourceIT {

    private static final Integer DEFAULT_NUMERO_RECETA = 1;
    private static final Integer UPDATED_NUMERO_RECETA = 2;

    private static final Instant DEFAULT_FECHA_EMISION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_EMISION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FECHA_VENCIMIENTO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_VENCIMIENTO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/recetas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RecetaRepository recetaRepository;

    @Autowired
    private RecetaMapper recetaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRecetaMockMvc;

    private Receta receta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Receta createEntity(EntityManager em) {
        Receta receta = new Receta()
            .numeroReceta(DEFAULT_NUMERO_RECETA)
            .fechaEmision(DEFAULT_FECHA_EMISION)
            .fechaVencimiento(DEFAULT_FECHA_VENCIMIENTO)
            .descripcion(DEFAULT_DESCRIPCION);
        return receta;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Receta createUpdatedEntity(EntityManager em) {
        Receta receta = new Receta()
            .numeroReceta(UPDATED_NUMERO_RECETA)
            .fechaEmision(UPDATED_FECHA_EMISION)
            .fechaVencimiento(UPDATED_FECHA_VENCIMIENTO)
            .descripcion(UPDATED_DESCRIPCION);
        return receta;
    }

    @BeforeEach
    public void initTest() {
        receta = createEntity(em);
    }

    @Test
    @Transactional
    void createReceta() throws Exception {
        int databaseSizeBeforeCreate = recetaRepository.findAll().size();
        // Create the Receta
        RecetaDTO recetaDTO = recetaMapper.toDto(receta);
        restRecetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recetaDTO)))
            .andExpect(status().isCreated());

        // Validate the Receta in the database
        List<Receta> recetaList = recetaRepository.findAll();
        assertThat(recetaList).hasSize(databaseSizeBeforeCreate + 1);
        Receta testReceta = recetaList.get(recetaList.size() - 1);
        assertThat(testReceta.getNumeroReceta()).isEqualTo(DEFAULT_NUMERO_RECETA);
        assertThat(testReceta.getFechaEmision()).isEqualTo(DEFAULT_FECHA_EMISION);
        assertThat(testReceta.getFechaVencimiento()).isEqualTo(DEFAULT_FECHA_VENCIMIENTO);
        assertThat(testReceta.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    void createRecetaWithExistingId() throws Exception {
        // Create the Receta with an existing ID
        receta.setId(1L);
        RecetaDTO recetaDTO = recetaMapper.toDto(receta);

        int databaseSizeBeforeCreate = recetaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recetaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Receta in the database
        List<Receta> recetaList = recetaRepository.findAll();
        assertThat(recetaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRecetas() throws Exception {
        // Initialize the database
        recetaRepository.saveAndFlush(receta);

        // Get all the recetaList
        restRecetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(receta.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroReceta").value(hasItem(DEFAULT_NUMERO_RECETA)))
            .andExpect(jsonPath("$.[*].fechaEmision").value(hasItem(DEFAULT_FECHA_EMISION.toString())))
            .andExpect(jsonPath("$.[*].fechaVencimiento").value(hasItem(DEFAULT_FECHA_VENCIMIENTO.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }

    @Test
    @Transactional
    void getReceta() throws Exception {
        // Initialize the database
        recetaRepository.saveAndFlush(receta);

        // Get the receta
        restRecetaMockMvc
            .perform(get(ENTITY_API_URL_ID, receta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(receta.getId().intValue()))
            .andExpect(jsonPath("$.numeroReceta").value(DEFAULT_NUMERO_RECETA))
            .andExpect(jsonPath("$.fechaEmision").value(DEFAULT_FECHA_EMISION.toString()))
            .andExpect(jsonPath("$.fechaVencimiento").value(DEFAULT_FECHA_VENCIMIENTO.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    @Transactional
    void getNonExistingReceta() throws Exception {
        // Get the receta
        restRecetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewReceta() throws Exception {
        // Initialize the database
        recetaRepository.saveAndFlush(receta);

        int databaseSizeBeforeUpdate = recetaRepository.findAll().size();

        // Update the receta
        Receta updatedReceta = recetaRepository.findById(receta.getId()).get();
        // Disconnect from session so that the updates on updatedReceta are not directly saved in db
        em.detach(updatedReceta);
        updatedReceta
            .numeroReceta(UPDATED_NUMERO_RECETA)
            .fechaEmision(UPDATED_FECHA_EMISION)
            .fechaVencimiento(UPDATED_FECHA_VENCIMIENTO)
            .descripcion(UPDATED_DESCRIPCION);
        RecetaDTO recetaDTO = recetaMapper.toDto(updatedReceta);

        restRecetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recetaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Receta in the database
        List<Receta> recetaList = recetaRepository.findAll();
        assertThat(recetaList).hasSize(databaseSizeBeforeUpdate);
        Receta testReceta = recetaList.get(recetaList.size() - 1);
        assertThat(testReceta.getNumeroReceta()).isEqualTo(UPDATED_NUMERO_RECETA);
        assertThat(testReceta.getFechaEmision()).isEqualTo(UPDATED_FECHA_EMISION);
        assertThat(testReceta.getFechaVencimiento()).isEqualTo(UPDATED_FECHA_VENCIMIENTO);
        assertThat(testReceta.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void putNonExistingReceta() throws Exception {
        int databaseSizeBeforeUpdate = recetaRepository.findAll().size();
        receta.setId(count.incrementAndGet());

        // Create the Receta
        RecetaDTO recetaDTO = recetaMapper.toDto(receta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Receta in the database
        List<Receta> recetaList = recetaRepository.findAll();
        assertThat(recetaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReceta() throws Exception {
        int databaseSizeBeforeUpdate = recetaRepository.findAll().size();
        receta.setId(count.incrementAndGet());

        // Create the Receta
        RecetaDTO recetaDTO = recetaMapper.toDto(receta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Receta in the database
        List<Receta> recetaList = recetaRepository.findAll();
        assertThat(recetaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReceta() throws Exception {
        int databaseSizeBeforeUpdate = recetaRepository.findAll().size();
        receta.setId(count.incrementAndGet());

        // Create the Receta
        RecetaDTO recetaDTO = recetaMapper.toDto(receta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecetaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Receta in the database
        List<Receta> recetaList = recetaRepository.findAll();
        assertThat(recetaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRecetaWithPatch() throws Exception {
        // Initialize the database
        recetaRepository.saveAndFlush(receta);

        int databaseSizeBeforeUpdate = recetaRepository.findAll().size();

        // Update the receta using partial update
        Receta partialUpdatedReceta = new Receta();
        partialUpdatedReceta.setId(receta.getId());

        partialUpdatedReceta.numeroReceta(UPDATED_NUMERO_RECETA).fechaEmision(UPDATED_FECHA_EMISION).descripcion(UPDATED_DESCRIPCION);

        restRecetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReceta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReceta))
            )
            .andExpect(status().isOk());

        // Validate the Receta in the database
        List<Receta> recetaList = recetaRepository.findAll();
        assertThat(recetaList).hasSize(databaseSizeBeforeUpdate);
        Receta testReceta = recetaList.get(recetaList.size() - 1);
        assertThat(testReceta.getNumeroReceta()).isEqualTo(UPDATED_NUMERO_RECETA);
        assertThat(testReceta.getFechaEmision()).isEqualTo(UPDATED_FECHA_EMISION);
        assertThat(testReceta.getFechaVencimiento()).isEqualTo(DEFAULT_FECHA_VENCIMIENTO);
        assertThat(testReceta.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void fullUpdateRecetaWithPatch() throws Exception {
        // Initialize the database
        recetaRepository.saveAndFlush(receta);

        int databaseSizeBeforeUpdate = recetaRepository.findAll().size();

        // Update the receta using partial update
        Receta partialUpdatedReceta = new Receta();
        partialUpdatedReceta.setId(receta.getId());

        partialUpdatedReceta
            .numeroReceta(UPDATED_NUMERO_RECETA)
            .fechaEmision(UPDATED_FECHA_EMISION)
            .fechaVencimiento(UPDATED_FECHA_VENCIMIENTO)
            .descripcion(UPDATED_DESCRIPCION);

        restRecetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReceta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReceta))
            )
            .andExpect(status().isOk());

        // Validate the Receta in the database
        List<Receta> recetaList = recetaRepository.findAll();
        assertThat(recetaList).hasSize(databaseSizeBeforeUpdate);
        Receta testReceta = recetaList.get(recetaList.size() - 1);
        assertThat(testReceta.getNumeroReceta()).isEqualTo(UPDATED_NUMERO_RECETA);
        assertThat(testReceta.getFechaEmision()).isEqualTo(UPDATED_FECHA_EMISION);
        assertThat(testReceta.getFechaVencimiento()).isEqualTo(UPDATED_FECHA_VENCIMIENTO);
        assertThat(testReceta.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void patchNonExistingReceta() throws Exception {
        int databaseSizeBeforeUpdate = recetaRepository.findAll().size();
        receta.setId(count.incrementAndGet());

        // Create the Receta
        RecetaDTO recetaDTO = recetaMapper.toDto(receta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, recetaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(recetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Receta in the database
        List<Receta> recetaList = recetaRepository.findAll();
        assertThat(recetaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReceta() throws Exception {
        int databaseSizeBeforeUpdate = recetaRepository.findAll().size();
        receta.setId(count.incrementAndGet());

        // Create the Receta
        RecetaDTO recetaDTO = recetaMapper.toDto(receta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(recetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Receta in the database
        List<Receta> recetaList = recetaRepository.findAll();
        assertThat(recetaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReceta() throws Exception {
        int databaseSizeBeforeUpdate = recetaRepository.findAll().size();
        receta.setId(count.incrementAndGet());

        // Create the Receta
        RecetaDTO recetaDTO = recetaMapper.toDto(receta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecetaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(recetaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Receta in the database
        List<Receta> recetaList = recetaRepository.findAll();
        assertThat(recetaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReceta() throws Exception {
        // Initialize the database
        recetaRepository.saveAndFlush(receta);

        int databaseSizeBeforeDelete = recetaRepository.findAll().size();

        // Delete the receta
        restRecetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, receta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Receta> recetaList = recetaRepository.findAll();
        assertThat(recetaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
