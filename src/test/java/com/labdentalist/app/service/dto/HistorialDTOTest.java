package com.labdentalist.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.labdentalist.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HistorialDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistorialDTO.class);
        HistorialDTO historialDTO1 = new HistorialDTO();
        historialDTO1.setId(1L);
        HistorialDTO historialDTO2 = new HistorialDTO();
        assertThat(historialDTO1).isNotEqualTo(historialDTO2);
        historialDTO2.setId(historialDTO1.getId());
        assertThat(historialDTO1).isEqualTo(historialDTO2);
        historialDTO2.setId(2L);
        assertThat(historialDTO1).isNotEqualTo(historialDTO2);
        historialDTO1.setId(null);
        assertThat(historialDTO1).isNotEqualTo(historialDTO2);
    }
}
