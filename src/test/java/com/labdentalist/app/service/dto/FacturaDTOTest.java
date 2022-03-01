package com.labdentalist.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.labdentalist.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FacturaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FacturaDTO.class);
        FacturaDTO facturaDTO1 = new FacturaDTO();
        facturaDTO1.setId(1L);
        FacturaDTO facturaDTO2 = new FacturaDTO();
        assertThat(facturaDTO1).isNotEqualTo(facturaDTO2);
        facturaDTO2.setId(facturaDTO1.getId());
        assertThat(facturaDTO1).isEqualTo(facturaDTO2);
        facturaDTO2.setId(2L);
        assertThat(facturaDTO1).isNotEqualTo(facturaDTO2);
        facturaDTO1.setId(null);
        assertThat(facturaDTO1).isNotEqualTo(facturaDTO2);
    }
}
