package com.labdentalist.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.labdentalist.app.IntegrationTest;
import com.labdentalist.app.domain.Factura;
import com.labdentalist.app.repository.FacturaRepository;
import com.labdentalist.app.service.dto.FacturaDTO;
import com.labdentalist.app.service.mapper.FacturaMapper;
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
 * Integration tests for the {@link FacturaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FacturaResourceIT {

    private static final Integer DEFAULT_NUMERO_FACTURA = 1;
    private static final Integer UPDATED_NUMERO_FACTURA = 2;

    private static final Instant DEFAULT_FECHA_EMISION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_EMISION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_TIPO_PAGO = "AAAAAAAAAA";
    private static final String UPDATED_TIPO_PAGO = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_VENCIMIENTO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_VENCIMIENTO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DECRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DECRIPCION = "BBBBBBBBBB";

    private static final Double DEFAULT_SUBTOTAL = 1D;
    private static final Double UPDATED_SUBTOTAL = 2D;

    private static final Double DEFAULT_TOTAL = 1D;
    private static final Double UPDATED_TOTAL = 2D;

    private static final Double DEFAULT_IMPORTE_PAGADO = 1D;
    private static final Double UPDATED_IMPORTE_PAGADO = 2D;

    private static final Double DEFAULT_IMPORTE_A_PAGAR = 1D;
    private static final Double UPDATED_IMPORTE_A_PAGAR = 2D;

    private static final String ENTITY_API_URL = "/api/facturas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private FacturaMapper facturaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFacturaMockMvc;

    private Factura factura;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Factura createEntity(EntityManager em) {
        Factura factura = new Factura()
            .numeroFactura(DEFAULT_NUMERO_FACTURA)
            .fechaEmision(DEFAULT_FECHA_EMISION)
            .tipoPago(DEFAULT_TIPO_PAGO)
            .fechaVencimiento(DEFAULT_FECHA_VENCIMIENTO)
            .decripcion(DEFAULT_DECRIPCION)
            .subtotal(DEFAULT_SUBTOTAL)
            .total(DEFAULT_TOTAL)
            .importePagado(DEFAULT_IMPORTE_PAGADO)
            .importeAPagar(DEFAULT_IMPORTE_A_PAGAR);
        return factura;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Factura createUpdatedEntity(EntityManager em) {
        Factura factura = new Factura()
            .numeroFactura(UPDATED_NUMERO_FACTURA)
            .fechaEmision(UPDATED_FECHA_EMISION)
            .tipoPago(UPDATED_TIPO_PAGO)
            .fechaVencimiento(UPDATED_FECHA_VENCIMIENTO)
            .decripcion(UPDATED_DECRIPCION)
            .subtotal(UPDATED_SUBTOTAL)
            .total(UPDATED_TOTAL)
            .importePagado(UPDATED_IMPORTE_PAGADO)
            .importeAPagar(UPDATED_IMPORTE_A_PAGAR);
        return factura;
    }

    @BeforeEach
    public void initTest() {
        factura = createEntity(em);
    }

    @Test
    @Transactional
    void createFactura() throws Exception {
        int databaseSizeBeforeCreate = facturaRepository.findAll().size();
        // Create the Factura
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);
        restFacturaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facturaDTO)))
            .andExpect(status().isCreated());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeCreate + 1);
        Factura testFactura = facturaList.get(facturaList.size() - 1);
        assertThat(testFactura.getNumeroFactura()).isEqualTo(DEFAULT_NUMERO_FACTURA);
        assertThat(testFactura.getFechaEmision()).isEqualTo(DEFAULT_FECHA_EMISION);
        assertThat(testFactura.getTipoPago()).isEqualTo(DEFAULT_TIPO_PAGO);
        assertThat(testFactura.getFechaVencimiento()).isEqualTo(DEFAULT_FECHA_VENCIMIENTO);
        assertThat(testFactura.getDecripcion()).isEqualTo(DEFAULT_DECRIPCION);
        assertThat(testFactura.getSubtotal()).isEqualTo(DEFAULT_SUBTOTAL);
        assertThat(testFactura.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testFactura.getImportePagado()).isEqualTo(DEFAULT_IMPORTE_PAGADO);
        assertThat(testFactura.getImporteAPagar()).isEqualTo(DEFAULT_IMPORTE_A_PAGAR);
    }

    @Test
    @Transactional
    void createFacturaWithExistingId() throws Exception {
        // Create the Factura with an existing ID
        factura.setId(1L);
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        int databaseSizeBeforeCreate = facturaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFacturaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facturaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFacturas() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList
        restFacturaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factura.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroFactura").value(hasItem(DEFAULT_NUMERO_FACTURA)))
            .andExpect(jsonPath("$.[*].fechaEmision").value(hasItem(DEFAULT_FECHA_EMISION.toString())))
            .andExpect(jsonPath("$.[*].tipoPago").value(hasItem(DEFAULT_TIPO_PAGO)))
            .andExpect(jsonPath("$.[*].fechaVencimiento").value(hasItem(DEFAULT_FECHA_VENCIMIENTO.toString())))
            .andExpect(jsonPath("$.[*].decripcion").value(hasItem(DEFAULT_DECRIPCION)))
            .andExpect(jsonPath("$.[*].subtotal").value(hasItem(DEFAULT_SUBTOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].importePagado").value(hasItem(DEFAULT_IMPORTE_PAGADO.doubleValue())))
            .andExpect(jsonPath("$.[*].importeAPagar").value(hasItem(DEFAULT_IMPORTE_A_PAGAR.doubleValue())));
    }

    @Test
    @Transactional
    void getFactura() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get the factura
        restFacturaMockMvc
            .perform(get(ENTITY_API_URL_ID, factura.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(factura.getId().intValue()))
            .andExpect(jsonPath("$.numeroFactura").value(DEFAULT_NUMERO_FACTURA))
            .andExpect(jsonPath("$.fechaEmision").value(DEFAULT_FECHA_EMISION.toString()))
            .andExpect(jsonPath("$.tipoPago").value(DEFAULT_TIPO_PAGO))
            .andExpect(jsonPath("$.fechaVencimiento").value(DEFAULT_FECHA_VENCIMIENTO.toString()))
            .andExpect(jsonPath("$.decripcion").value(DEFAULT_DECRIPCION))
            .andExpect(jsonPath("$.subtotal").value(DEFAULT_SUBTOTAL.doubleValue()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.importePagado").value(DEFAULT_IMPORTE_PAGADO.doubleValue()))
            .andExpect(jsonPath("$.importeAPagar").value(DEFAULT_IMPORTE_A_PAGAR.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingFactura() throws Exception {
        // Get the factura
        restFacturaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFactura() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();

        // Update the factura
        Factura updatedFactura = facturaRepository.findById(factura.getId()).get();
        // Disconnect from session so that the updates on updatedFactura are not directly saved in db
        em.detach(updatedFactura);
        updatedFactura
            .numeroFactura(UPDATED_NUMERO_FACTURA)
            .fechaEmision(UPDATED_FECHA_EMISION)
            .tipoPago(UPDATED_TIPO_PAGO)
            .fechaVencimiento(UPDATED_FECHA_VENCIMIENTO)
            .decripcion(UPDATED_DECRIPCION)
            .subtotal(UPDATED_SUBTOTAL)
            .total(UPDATED_TOTAL)
            .importePagado(UPDATED_IMPORTE_PAGADO)
            .importeAPagar(UPDATED_IMPORTE_A_PAGAR);
        FacturaDTO facturaDTO = facturaMapper.toDto(updatedFactura);

        restFacturaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, facturaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facturaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
        Factura testFactura = facturaList.get(facturaList.size() - 1);
        assertThat(testFactura.getNumeroFactura()).isEqualTo(UPDATED_NUMERO_FACTURA);
        assertThat(testFactura.getFechaEmision()).isEqualTo(UPDATED_FECHA_EMISION);
        assertThat(testFactura.getTipoPago()).isEqualTo(UPDATED_TIPO_PAGO);
        assertThat(testFactura.getFechaVencimiento()).isEqualTo(UPDATED_FECHA_VENCIMIENTO);
        assertThat(testFactura.getDecripcion()).isEqualTo(UPDATED_DECRIPCION);
        assertThat(testFactura.getSubtotal()).isEqualTo(UPDATED_SUBTOTAL);
        assertThat(testFactura.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testFactura.getImportePagado()).isEqualTo(UPDATED_IMPORTE_PAGADO);
        assertThat(testFactura.getImporteAPagar()).isEqualTo(UPDATED_IMPORTE_A_PAGAR);
    }

    @Test
    @Transactional
    void putNonExistingFactura() throws Exception {
        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();
        factura.setId(count.incrementAndGet());

        // Create the Factura
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacturaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, facturaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facturaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFactura() throws Exception {
        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();
        factura.setId(count.incrementAndGet());

        // Create the Factura
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facturaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFactura() throws Exception {
        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();
        factura.setId(count.incrementAndGet());

        // Create the Factura
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facturaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFacturaWithPatch() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();

        // Update the factura using partial update
        Factura partialUpdatedFactura = new Factura();
        partialUpdatedFactura.setId(factura.getId());

        partialUpdatedFactura
            .tipoPago(UPDATED_TIPO_PAGO)
            .fechaVencimiento(UPDATED_FECHA_VENCIMIENTO)
            .decripcion(UPDATED_DECRIPCION)
            .subtotal(UPDATED_SUBTOTAL)
            .importeAPagar(UPDATED_IMPORTE_A_PAGAR);

        restFacturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFactura.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFactura))
            )
            .andExpect(status().isOk());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
        Factura testFactura = facturaList.get(facturaList.size() - 1);
        assertThat(testFactura.getNumeroFactura()).isEqualTo(DEFAULT_NUMERO_FACTURA);
        assertThat(testFactura.getFechaEmision()).isEqualTo(DEFAULT_FECHA_EMISION);
        assertThat(testFactura.getTipoPago()).isEqualTo(UPDATED_TIPO_PAGO);
        assertThat(testFactura.getFechaVencimiento()).isEqualTo(UPDATED_FECHA_VENCIMIENTO);
        assertThat(testFactura.getDecripcion()).isEqualTo(UPDATED_DECRIPCION);
        assertThat(testFactura.getSubtotal()).isEqualTo(UPDATED_SUBTOTAL);
        assertThat(testFactura.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testFactura.getImportePagado()).isEqualTo(DEFAULT_IMPORTE_PAGADO);
        assertThat(testFactura.getImporteAPagar()).isEqualTo(UPDATED_IMPORTE_A_PAGAR);
    }

    @Test
    @Transactional
    void fullUpdateFacturaWithPatch() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();

        // Update the factura using partial update
        Factura partialUpdatedFactura = new Factura();
        partialUpdatedFactura.setId(factura.getId());

        partialUpdatedFactura
            .numeroFactura(UPDATED_NUMERO_FACTURA)
            .fechaEmision(UPDATED_FECHA_EMISION)
            .tipoPago(UPDATED_TIPO_PAGO)
            .fechaVencimiento(UPDATED_FECHA_VENCIMIENTO)
            .decripcion(UPDATED_DECRIPCION)
            .subtotal(UPDATED_SUBTOTAL)
            .total(UPDATED_TOTAL)
            .importePagado(UPDATED_IMPORTE_PAGADO)
            .importeAPagar(UPDATED_IMPORTE_A_PAGAR);

        restFacturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFactura.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFactura))
            )
            .andExpect(status().isOk());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
        Factura testFactura = facturaList.get(facturaList.size() - 1);
        assertThat(testFactura.getNumeroFactura()).isEqualTo(UPDATED_NUMERO_FACTURA);
        assertThat(testFactura.getFechaEmision()).isEqualTo(UPDATED_FECHA_EMISION);
        assertThat(testFactura.getTipoPago()).isEqualTo(UPDATED_TIPO_PAGO);
        assertThat(testFactura.getFechaVencimiento()).isEqualTo(UPDATED_FECHA_VENCIMIENTO);
        assertThat(testFactura.getDecripcion()).isEqualTo(UPDATED_DECRIPCION);
        assertThat(testFactura.getSubtotal()).isEqualTo(UPDATED_SUBTOTAL);
        assertThat(testFactura.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testFactura.getImportePagado()).isEqualTo(UPDATED_IMPORTE_PAGADO);
        assertThat(testFactura.getImporteAPagar()).isEqualTo(UPDATED_IMPORTE_A_PAGAR);
    }

    @Test
    @Transactional
    void patchNonExistingFactura() throws Exception {
        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();
        factura.setId(count.incrementAndGet());

        // Create the Factura
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, facturaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(facturaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFactura() throws Exception {
        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();
        factura.setId(count.incrementAndGet());

        // Create the Factura
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(facturaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFactura() throws Exception {
        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();
        factura.setId(count.incrementAndGet());

        // Create the Factura
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(facturaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFactura() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        int databaseSizeBeforeDelete = facturaRepository.findAll().size();

        // Delete the factura
        restFacturaMockMvc
            .perform(delete(ENTITY_API_URL_ID, factura.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
