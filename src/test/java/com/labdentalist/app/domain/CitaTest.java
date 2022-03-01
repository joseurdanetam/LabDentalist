package com.labdentalist.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.labdentalist.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CitaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cita.class);
        Cita cita1 = new Cita();
        cita1.setId(1L);
        Cita cita2 = new Cita();
        cita2.setId(cita1.getId());
        assertThat(cita1).isEqualTo(cita2);
        cita2.setId(2L);
        assertThat(cita1).isNotEqualTo(cita2);
        cita1.setId(null);
        assertThat(cita1).isNotEqualTo(cita2);
    }
}
