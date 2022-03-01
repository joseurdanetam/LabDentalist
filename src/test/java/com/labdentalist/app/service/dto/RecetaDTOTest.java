package com.labdentalist.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.labdentalist.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RecetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecetaDTO.class);
        RecetaDTO recetaDTO1 = new RecetaDTO();
        recetaDTO1.setId(1L);
        RecetaDTO recetaDTO2 = new RecetaDTO();
        assertThat(recetaDTO1).isNotEqualTo(recetaDTO2);
        recetaDTO2.setId(recetaDTO1.getId());
        assertThat(recetaDTO1).isEqualTo(recetaDTO2);
        recetaDTO2.setId(2L);
        assertThat(recetaDTO1).isNotEqualTo(recetaDTO2);
        recetaDTO1.setId(null);
        assertThat(recetaDTO1).isNotEqualTo(recetaDTO2);
    }
}
