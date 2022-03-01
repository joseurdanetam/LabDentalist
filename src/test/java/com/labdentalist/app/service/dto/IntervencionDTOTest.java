package com.labdentalist.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.labdentalist.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IntervencionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IntervencionDTO.class);
        IntervencionDTO intervencionDTO1 = new IntervencionDTO();
        intervencionDTO1.setId(1L);
        IntervencionDTO intervencionDTO2 = new IntervencionDTO();
        assertThat(intervencionDTO1).isNotEqualTo(intervencionDTO2);
        intervencionDTO2.setId(intervencionDTO1.getId());
        assertThat(intervencionDTO1).isEqualTo(intervencionDTO2);
        intervencionDTO2.setId(2L);
        assertThat(intervencionDTO1).isNotEqualTo(intervencionDTO2);
        intervencionDTO1.setId(null);
        assertThat(intervencionDTO1).isNotEqualTo(intervencionDTO2);
    }
}
